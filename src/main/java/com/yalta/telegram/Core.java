package com.yalta.telegram;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Queue;

@Slf4j
@Getter
@Service
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
//        log.info("RECEIVE >> chatId: {}, message: {}", update.getMessage().getChatId(), update.getMessage().getText()); todo
    }
}