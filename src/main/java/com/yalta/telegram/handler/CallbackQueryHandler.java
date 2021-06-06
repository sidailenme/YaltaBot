package com.yalta.telegram.handler;

import com.yalta.telegram.command.CallbackCommand;
import com.yalta.telegram.command.TextCommand;
import com.yalta.telegram.entity.Cafe;
import com.yalta.telegram.handler.interfaces.Handler;
import com.yalta.telegram.keyboard.InlineKeyboardUtils;
import com.yalta.telegram.repository.CafeRepository;
import com.yalta.telegram.service.MenuView;
import com.yalta.telegram.service.PreMessage;
import com.yalta.utils.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Queue;

@Slf4j
@Service
@RequiredArgsConstructor
public class CallbackQueryHandler implements Handler<EditMessageText, Pair<CallbackCommand, String>> {

    private final Queue<EditMessageText> editQueue;
    private final MenuView menuView;
    private final CafeRepository cafeRepository;
    private final PreMessage preMessage;

    @Override
    public void handle(Update update) {
        try {
            Message message = update.getCallbackQuery().getMessage();
            String data = update.getCallbackQuery().getData();
            Pair<CallbackCommand, String> pair = splitData(data);
            EditMessageText editMessage = operate(pair);
            editMessage.setChatId(message.getChatId().toString());
            editMessage.setMessageId(message.getMessageId());
            editQueue.add(editMessage);
        } catch (Exception e) {
            preMessage.send(TextCommand.ERROR, update.getCallbackQuery().getMessage().getChatId().toString());
            log.error(e.getMessage());
        }
    }

    @Override
    public EditMessageText operate(Pair<CallbackCommand, String> data) {
        return switch (data.left()) {
            case CAFE_PAGE -> cafe(data);
            case NONE -> throw new IllegalArgumentException("switch on NONE case");
        };
    }

    private Pair<CallbackCommand, String> splitData(String data) {
        String[] arr = data.split("_");
        CallbackCommand command = CallbackCommand.findByData(arr[0]);
        String payload = arr[1];
        return new Pair<>(command, payload);
    }

    private EditMessageText cafe(Pair<CallbackCommand, String> data) {
        int pageNum = Integer.parseInt(data.right());
        Pageable pageable = PageRequest.of(pageNum, 2);
        Page<Cafe> page = cafeRepository.findAll(pageable);
        String messageText = menuView.cafe(page);
        EditMessageText message = new EditMessageText();
        message.setText(messageText);
        message.setReplyMarkup(InlineKeyboardUtils.numericPageKeyboard(page, data.left()));
        return message;
    }
}
