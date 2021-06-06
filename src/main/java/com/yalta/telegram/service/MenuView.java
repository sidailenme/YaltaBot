package com.yalta.telegram.service;

import com.yalta.telegram.entity.Cafe;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;

import static com.yalta.telegram.command.TextCommand.*;

@Setter
@Service
@RequiredArgsConstructor
@ConfigurationProperties("menu")
public class MenuView {

    public SendMessage menu() {
        SendMessage message = new SendMessage();

        KeyboardButton b1 = new KeyboardButton(CAFE.getDesc());
        KeyboardButton b2 = new KeyboardButton(RENT.getDesc());
        KeyboardRow row1 = new KeyboardRow();
        row1.add(b1);
        row1.add(b2);

        KeyboardButton b3 = new KeyboardButton(ENTERTAINMENT.getDesc());
        KeyboardButton b4 = new KeyboardButton(DELIVERY.getDesc());
        KeyboardRow row2 = new KeyboardRow();
        row2.add(b3);
        row2.add(b4);

        KeyboardButton b5 = new KeyboardButton(TAXI.getDesc());
        KeyboardButton b6 = new KeyboardButton(AUTO.getDesc());
        KeyboardRow row3 = new KeyboardRow();
        row3.add(b5);
        row3.add(b6);

        ArrayList<KeyboardRow> keyboardTable = new ArrayList<>();
        keyboardTable.add(row1);
        keyboardTable.add(row2);
        keyboardTable.add(row3);

        message.setReplyMarkup(new ReplyKeyboardMarkup(keyboardTable));
        return message;
    }

    public String cafe(Page<Cafe> page) {
        return "Cafe: \n" +
                page.getContent().stream()
                        .map(cafe -> "Cafe #" + cafe.getId() + ": " + cafe.getName())
                        .reduce((s, s2) -> s + "\n" + s2).orElse("empty");
    }
}