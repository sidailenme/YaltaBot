package com.yalta.telegram.service;

import com.yalta.telegram.command.Command;
import com.yalta.telegram.handler.CallbackQueryHandler;
import com.yalta.telegram.handler.TextHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@Service
@Slf4j
public class MessageReceiver implements Runnable {

    @Value("${bot.config.sleep-time-for-receive:500}")
    private int SLEEP_TIME_FOR_RECEIVE;

    private final TextHandler textHandler;
    private final CallbackQueryHandler callbackQueryHandler;
    private final Queue<Update> receiveQueue;
    private final Queue<SendMessage> sendQueue;
    private final Queue<EditMessageText> editQueue;

    public MessageReceiver(TextHandler textHandler,
                           CallbackQueryHandler callbackQueryHandler,
                           Queue<Update> receiveQueue,
                           Queue<SendMessage> sendQueue,
                           Queue<EditMessageText> editQueue) {
        this.textHandler = textHandler;
        this.callbackQueryHandler = callbackQueryHandler;
        this.receiveQueue = receiveQueue;
        this.sendQueue = sendQueue;
        this.editQueue = editQueue;

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
