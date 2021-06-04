package com.yalta.telegram.handler;

import com.yalta.telegram.command.Command;
import com.yalta.telegram.handler.interfaces.Handler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Queue;

@Service
@RequiredArgsConstructor
public class CallbackQueryHandler implements Handler<EditMessageText> {

    private final Queue<EditMessageText> editQueue;

    @Override
    public void handle(Update update) {
        Message message = update.getCallbackQuery().getMessage();
        String data = update.getCallbackQuery().getData();
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setText("oOOooOO");
        editMessageText.setMessageId(message.getMessageId());

        editQueue.add(editMessageText);
    }

    @Override
    public EditMessageText operate(Command command) {
        return null;
    }
}
