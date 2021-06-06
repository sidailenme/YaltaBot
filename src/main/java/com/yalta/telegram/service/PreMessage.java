package com.yalta.telegram.service;

import com.yalta.telegram.command.TextCommand;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;
import java.util.Queue;

@Setter
@Service
@RequiredArgsConstructor
@ConfigurationProperties("pre-messages")
public class PreMessage {

    private final Queue<SendMessage> sendQueue;
    private Map<TextCommand, String> preMessageMap;

    public void send(TextCommand textCommand, String chatId) {
        SendMessage message = new SendMessage(chatId, preMessageMap.get(textCommand));
        sendQueue.add(message);
    }

}
