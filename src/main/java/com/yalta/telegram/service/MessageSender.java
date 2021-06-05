package com.yalta.telegram.service;

import com.yalta.telegram.Core;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.Queue;

@Slf4j
@Service
public class MessageSender implements Runnable {

    @Value("${bot.config.sleep-time-for-send:500}")
    private int SLEEP_TIME_FOR_SEND;     //todo rf

    private final Queue<SendMessage> sendQueue;
    private final Queue<EditMessageText> editQueue;
    private final Core core;

    public MessageSender(Queue<SendMessage> sendQueue, Core core,
                         Queue<EditMessageText> editQueue) {
        this.sendQueue = sendQueue;
        this.editQueue = editQueue;
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
            for (EditMessageText message = editQueue.poll(); message != null; message = editQueue.poll()) {
                edit(message);
            }
            Thread.sleep(SLEEP_TIME_FOR_SEND);
        }
    }

    public void send(SendMessage message) {
        try {
            core.execute(message);
            log.info("SEND >> chatId: {}, message {}", message.getChatId(), message.getText());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void edit(EditMessageText message) {
        try {
            core.execute(message);
            log.info("EDIT >> chatId: {}, message {}", message.getChatId(), message.getText());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
