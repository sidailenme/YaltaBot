package com.yalta.telegram.service;

import com.yalta.telegram.Core;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Queue;

@Service
public class MessageSender implements Runnable {

    @Value("${bot.config.delay-for-send:300}")
    private int DELAY_FOR_SEND;     //todo rf

    private final Queue<Update> sendQueue;
    private final Core core;

    public MessageSender(Queue<Update> sendQueue, Core core) {
        this.sendQueue = sendQueue;
        this.core = core;

        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.setName("MessageSender");
        thread.start();
    }

    @SneakyThrows
    @Override
    public void run() {

    }
}
