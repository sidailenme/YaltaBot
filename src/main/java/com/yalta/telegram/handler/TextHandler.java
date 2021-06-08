package com.yalta.telegram.handler;

import com.yalta.telegram.command.TextCommand;
import com.yalta.telegram.entity.Cafe;
import com.yalta.telegram.entity.Rent;
import com.yalta.telegram.entity.Taxi;
import com.yalta.telegram.handler.interfaces.Handler;
import com.yalta.telegram.keyboard.InlineKeyboardUtils;
import com.yalta.telegram.repository.*;
import com.yalta.telegram.service.MenuView;
import com.yalta.telegram.service.PreMessage;
import com.yalta.utils.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;
import java.util.Queue;

import static com.yalta.telegram.command.CallbackCommand.*;
import static com.yalta.telegram.command.TextCommand.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TextHandler implements Handler<SendMessage, Pair<TextCommand, String>> {

    private final Queue<SendMessage> sendQueue;
    private final CafeRepository cafeRepository;
    private final TaxiRepository taxiRepository;
    private final RentRepository rentRepository;
    private final EntertainmentRepository entertainmentRepository;
    private final DeliveryRepository deliveryRepository;
    private final PreMessage preMessage;
    private final MenuView menuView;

    @Override
    public void handle(Update update) {
        try {
            String chatId = update.getMessage().getChatId().toString();
            String textRequest = update.getMessage().getText();
            TextCommand textCommand = findOnDesc(textRequest);
            Pair<TextCommand, String> pair = new Pair<>(textCommand, chatId);
            SendMessage message = operate(pair);
            message.setChatId(chatId);
            sendQueue.add(message);
        } catch (Exception e) {
            preMessage.send(ERROR, update.getMessage().getChatId().toString());
            log.error(e.getMessage());
        }
    }

    @Override
    public SendMessage operate(Pair<TextCommand, String> pair) {
        switch (pair.left()) {
            case START:
                preMessage.send(START, pair.right());
                //through
            case MENU:
                return menu();
            case INFO:
                return info();
            case CAFE:
                preMessage.send(CAFE, pair.right());
                return cafe();
            case TAXI:
                preMessage.send(TAXI, pair.right());
                return taxi();
            case RENT:
                preMessage.send(RENT, pair.right());
                return rent();
            case ENTERTAINMENT:
                preMessage.send(ENTERTAINMENT, pair.right());
                return entertainment();
            case DELIVERY:
                preMessage.send(DELIVERY, pair.right());
                return delivery();
            default:
                throw new IllegalArgumentException();
        }
    }

    private SendMessage menu() {
        return menuView.menu();
    }

    private SendMessage info() {
        return null; //todo
    }

    private SendMessage cafe() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Cafe> page = cafeRepository.findAll(pageable);

        SendMessage message = new SendMessage();
        message.setText(menuView.cafe(page));
        message.setReplyMarkup(InlineKeyboardUtils.numericPageKeyboard(page, CAFE_PAGE));
        return message;
    }

    private SendMessage taxi() {
        Pageable pageable = PageRequest.of(0, 3);
        Page<Taxi> page = taxiRepository.findAll(pageable);

        SendMessage message = new SendMessage();
        message.setText(menuView.taxi(page));
        message.setReplyMarkup(InlineKeyboardUtils.numericPageKeyboard(page, TAXI_PAGE));
        return message;
    }

    private SendMessage rent() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Rent> page = rentRepository.findAll(pageable);

        SendMessage message = new SendMessage();
        message.setText(menuView.rent(page));
        message.setReplyMarkup(InlineKeyboardUtils.numericPageKeyboard(page, RENT_PAGE));
        return message;
    }

    private SendMessage entertainment() {
        return null; //todo
    }

    private SendMessage delivery() {
        return null; //todo
    }

}
