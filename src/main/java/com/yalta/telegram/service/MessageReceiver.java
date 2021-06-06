package com.yalta.telegram.service;

import com.yalta.telegram.handler.CallbackQueryHandler;
import com.yalta.telegram.handler.TextHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Queue;

@Slf4j
@Service
public class MessageReceiver implements Runnable {

    @Value("${bot.config.sleep-time-for-receive:500}")
    private int SLEEP_TIME_FOR_RECEIVE;

    private final TextHandler textHandler;
    private final CallbackQueryHandler callbackQueryHandler;
    private final Queue<Update> receiveQueue;

    public MessageReceiver(TextHandler textHandler,
                           CallbackQueryHandler callbackQueryHandler,
                           Queue<Update> receiveQueue) {
        this.textHandler = textHandler;
        this.callbackQueryHandler = callbackQueryHandler;
        this.receiveQueue = receiveQueue;

        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.setName("MessageReceiver");
        thread.start();
    }

    @Override
    @SneakyThrows
    public void run() {
        while (true) {
            for (Update update = receiveQueue.poll(); update != null; update = receiveQueue.poll()) {
                analyze(update);
            }
            Thread.sleep(SLEEP_TIME_FOR_RECEIVE);
        }
    }

    public void analyze(Update update) {
        if (update.getMessage() != null) {
            textHandler.handle(update);
        } else if (update.getCallbackQuery() != null) {
            callbackQueryHandler.handle(update);
        }
    }
}
