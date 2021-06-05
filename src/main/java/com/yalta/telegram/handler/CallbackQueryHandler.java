package com.yalta.telegram.handler;

import com.yalta.telegram.entity.Cafe;
import com.yalta.telegram.handler.interfaces.Handler;
import com.yalta.telegram.keyboard.InlineKeyboardUtils;
import com.yalta.telegram.repository.CafeRepository;
import com.yalta.telegram.service.MenuView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Queue;

@Service
@RequiredArgsConstructor
public class CallbackQueryHandler implements Handler<EditMessageText> {

    private final Queue<EditMessageText> editQueue;
    private final MenuView menuView;
    private final CafeRepository cafeRepository;
    private Pageable pageable = PageRequest.of(0, 2);


    @Override
    public void handle(Update update) {
        Message message = update.getCallbackQuery().getMessage();
        String data = update.getCallbackQuery().getData();

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setMessageId(message.getMessageId());
        if (data.contains("buttonPage_")) {
            int pageNum = Integer.parseInt(data.subSequence(11, 12).toString());
            pageable = PageRequest.of(pageNum, 2);
            Page<Cafe> page = cafeRepository.findAll(pageable);
            String text = menuView.cafe(page);
            editMessageText.setText(text);
        editMessageText.setReplyMarkup(InlineKeyboardUtils.numericPageKeyboard(page));
        }


        editQueue.add(editMessageText);
    }

//    @Override
//    public EditMessageText operate(TextCommand textCommand) {
//
//        return null;
//    }
}
