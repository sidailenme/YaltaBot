package com.yalta.telegram.handler;

import com.yalta.telegram.command.Command;
import com.yalta.telegram.handler.interfaces.Handler;
import com.yalta.telegram.service.MessageParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@Service
@RequiredArgsConstructor
public class TextHandler implements Handler<SendMessage> {

    private final MessageParser messageParser;
    private final Queue<SendMessage> sendQueue;

    public void handle(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String textRequest = update.getMessage().getText();

        Command parsedCommand = messageParser.parse(textRequest);
        SendMessage message = operate(parsedCommand);
        message.setChatId(chatId);

        if (textRequest.equals("1")) {
            InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup();
            ArrayList<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            ArrayList<InlineKeyboardButton> e = new ArrayList<>();
            e.add(new InlineKeyboardButton("HUJK", null, "qq",
                    null, null, null, null, null));
            keyboard.add(e);
            replyMarkup.setKeyboard(keyboard);
            message.setReplyMarkup(replyMarkup);
        }

        sendQueue.add(message);
    }

    public SendMessage operate(Command command) {
        SendMessage message = new SendMessage();
        switch (command) {
            case NONE:
            case NOT_A_COMMAND:
                message.setText("Команда не найдена");
                break;
            case START:
                break;
            case INFO:
                message.setText("info");
                break;
            case FEEDBACK:
                message.setText("обратная связь");
                break;
            case CATALOG:
                break;
            case CAFE:
                break;
        }
        return message;
    }
}
