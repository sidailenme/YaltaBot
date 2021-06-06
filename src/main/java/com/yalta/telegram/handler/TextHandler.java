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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Queue;

import static com.yalta.telegram.command.TextCommand.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TextHandler implements Handler<SendMessage, Pair<TextCommand, String>> {

    private final Queue<SendMessage> sendQueue;
    private final CafeRepository cafeRepository;
    private final PreMessage preMessage;
    private final MenuView menuView;

    @Override
    public void handle(Update update) {
        try {
            String chatId = update.getMessage().getChatId().toString();
            String textRequest = update.getMessage().getText();
            TextCommand textCommand = findOnDesc(textRequest);
            Pair<TextCommand, String> pair = new Pair<>(textCommand, chatId);
            SendMessage message = operate(pair);
            message.setChatId(chatId);
            sendQueue.add(message);
        } catch (Exception e) {
            preMessage.send(ERROR, update.getCallbackQuery().getMessage().getChatId().toString());
            log.error(e.getMessage());
        }
    }

    @Override
    public SendMessage operate(Pair<TextCommand, String> pair) {
        SendMessage message = new SendMessage();
        switch (pair.left()) {
            case NONE:
                message.setText("Команда не найдена");
                break;
            case START:
                preMessage.send(START, pair.right());
                //through
            case MENU:
                return menu();
            case INFO:
                message.setText("info");
                break;
            case FEEDBACK:
                message.setText("обратная связь");
                break;
            case CAFE:
                preMessage.send(CAFE, pair.right());
                return cafe();
        }
        return message;
    }

    private SendMessage cafe() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Cafe> page = cafeRepository.findAll(pageable);

        SendMessage message = new SendMessage();
        message.setText(menuView.cafe(page));
        message.setReplyMarkup(InlineKeyboardUtils.numericPageKeyboard(page, CallbackCommand.CAFE_PAGE));

        return message;
    }

    private SendMessage menu() {
        return menuView.menu();
    }
}
