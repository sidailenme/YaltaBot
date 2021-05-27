package com.yalta.telegram;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Queue;

@Getter
@Service
@Slf4j
@RequiredArgsConstructor
public class Core extends TelegramLongPollingBot {

    @Value("${bot.telegram.name}")
    private String botUsername;
    @Value("${bot.telegram.token}")
    private String botToken;

    private final Queue<Update> receiveQueue;

    @Override
    public void onUpdateReceived(Update update) {
        receiveQueue.add(update);
        log.info("Receive >> chatId: {}, text: {}", update.getMessage().getChatId(), update.getMessage().getText());
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
