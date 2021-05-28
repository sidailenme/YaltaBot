package com.yalta.telegram.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Queue;

@Slf4j
@Service
public class MessageReceiver implements Runnable {

    @Value("${bot.config.sleep-time-for-receive:500}")
    private int SLEEP_TIME_FOR_RECEIVE;

    private final Queue<Update> receiveQueue;
    private final Queue<SendMessage> sendQueue;

    public MessageReceiver(Queue<Update> receiveQueue,
                           Queue<SendMessage> sendQueue) {
        this.receiveQueue = receiveQueue;
        this.sendQueue = sendQueue;

        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.setName("MessageReceiver");
        thread.start();
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            for (Update update = receiveQueue.poll(); update != null; update = receiveQueue.poll()) {
                log.info("RECEIVE >> chatId: {}, message: {}", update.getMessage().getChatId(), update.getMessage().getText());
                //todo add handler
                sendQueue.add(new SendMessage(update.getMessage().getChatId().toString(), "hello"));
            }
            Thread.sleep(SLEEP_TIME_FOR_RECEIVE);
        }
    }
}
