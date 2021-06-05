package com.yalta.telegram.handler;

import com.yalta.telegram.command.TextCommand;
import com.yalta.telegram.entity.Cafe;
import com.yalta.telegram.handler.interfaces.Handler;
import com.yalta.telegram.keyboard.InlineKeyboardUtils;
import com.yalta.telegram.repository.CafeRepository;
import com.yalta.telegram.service.MenuView;
import com.yalta.telegram.service.PreMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Queue;

@Service
@RequiredArgsConstructor
public class TextHandler implements Handler<SendMessage> {

    private final Queue<SendMessage> sendQueue;
    private final CafeRepository cafeRepository;
    private final PreMessage preMessage;
    private final MenuView menuView;

    @Override
    public void handle(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String textRequest = update.getMessage().getText();
        TextCommand textCommand = TextCommand.findOnDesc(textRequest);
        SendMessage message = operate(textCommand, chatId);
        message.setChatId(chatId);

        sendQueue.add(message);
    }

    public SendMessage operate(TextCommand textCommand, String chatId) {
        SendMessage message = new SendMessage();
        switch (textCommand) {
            case NONE:
            case NOT_A_COMMAND:
                message.setText("Команда не найдена");
                break;
            case START:
            case MENU:
                preMessage.send(textCommand, chatId);
                return menu();
            case INFO:
                message.setText("info");
                break;
            case FEEDBACK:
                message.setText("обратная связь");
                break;
            case CAFE:
                preMessage.send(textCommand, chatId);
                return cafe();
        }
        return message;
    }

    private SendMessage cafe() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Cafe> cafePage = cafeRepository.findAll(pageable);

        SendMessage message = new SendMessage();
        message.setText(menuView.cafe(cafePage));
        message.setReplyMarkup(InlineKeyboardUtils.numericPageKeyboard(cafePage));

        return message;
    }

    private SendMessage menu() {
        return menuView.menu();
    }
}
