package com.yalta.telegram.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageReceiver implements Runnable {

    @Value("${bot.config.delay-for-receive:300}")
    private int DELAY_FOR_RECEIVE; //todo rf

    MessageReceiver() {
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.setName("MessageReceiver");
        thread.start();
    }


    @SneakyThrows
    @Override
    public void run() {

    }
}
