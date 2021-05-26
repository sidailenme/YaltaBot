package com.yalta.telegram;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Getter
@Service
@RequiredArgsConstructor
public class Core extends TelegramLongPollingBot {

    @Value("${bot.telegram.name}")
    private String botUsername;
    @Value("${bot.telegram.token}")
    private String botToken;

    @Override
    public void onUpdateReceived(Update update) {       //todo rf
        String message = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        sendMsg(chatId.toString(), message);
        System.out.println(message);
    }

    public synchronized void sendMsg(String chatId, String text) { //todo rf
        SendMessage message = new SendMessage(chatId, text);
        message.enableMarkdown(true);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();   //todo add log
        }
    }
}
