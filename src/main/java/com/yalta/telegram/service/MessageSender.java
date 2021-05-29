package com.yalta.telegram.service;

import com.yalta.telegram.Core;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Queue;

@Slf4j
@Service
public class MessageSender implements Runnable {

    @Value("${bot.config.sleep-time-for-send:500}")
    private int SLEEP_TIME_FOR_SEND;     //todo rf

    private final Queue<SendMessage> sendQueue;
    private final Core core;

    public MessageSender(Queue<SendMessage> sendQueue, Core core) {
        this.sendQueue = sendQueue;
        this.core = core;

        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.setName("MessageSender");
        thread.start();
    }

    @Override
    @SneakyThrows
    public void run() {
        while (true) {
            for (SendMessage message = sendQueue.poll(); message != null; message = sendQueue.poll()) {
                send(message);
            }
            Thread.sleep(SLEEP_TIME_FOR_SEND);
        }
    }

    @SneakyThrows
    public void send(SendMessage message) {
        message.enableMarkdown(true);
        core.execute(message);
        log.info("SEND >> chatId: {}, message {}", message.getChatId(), message.getText());
    }
}
