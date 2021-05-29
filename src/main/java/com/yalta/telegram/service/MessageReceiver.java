package com.yalta.telegram.service;

import com.yalta.telegram.command.Command;
import com.yalta.telegram.handler.MainHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Queue;

@Service
public class MessageReceiver implements Runnable {

    @Value("${bot.config.sleep-time-for-receive:500}")
    private int SLEEP_TIME_FOR_RECEIVE;

    private final MessageParser messageParser;
    private final MainHandler mainHandler;
    private final Queue<Update> receiveQueue;
    private final Queue<SendMessage> sendQueue;

    public MessageReceiver(MessageParser messageParser,
                           MainHandler mainHandler,
                           Queue<Update> receiveQueue,
                           Queue<SendMessage> sendQueue) {
        this.messageParser = messageParser;
        this.mainHandler = mainHandler;
        this.receiveQueue = receiveQueue;
        this.sendQueue = sendQueue;

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
                SendMessage responseMessage = analyze(update);
                sendQueue.add(responseMessage);
            }
            Thread.sleep(SLEEP_TIME_FOR_RECEIVE);
        }
    }

    public SendMessage analyze(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String textRequest = update.getMessage().getText();
        Command parsedCommand = messageParser.parse(textRequest);
        String textResponse = mainHandler.operate(parsedCommand);
        return new SendMessage(chatId, textResponse);
    }
}
