package com.yalta.telegram.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum TextCommand {

    NONE("/none"),
    NOT_A_COMMAND("/not_a_command"),

    START("/start"),
    INFO("/info"),
    FEEDBACK("/feedback"),
    MENU("/menu"),

    CAFE("Кафе / Рестораны"),
    RENT("Гостиницы / Квартиры"),
    ENTERTAINMENT("Развлечения"),
    DELIVERY("Службы доставки"),
    TAXI("Службы Такси"),
    AUTO("Авто / СТО / Шиномонтаж");

//    SIGHTS("/sights"),
//    TODAY("/today");
//    CINEMA("/cinema");


    private Map<String, String> map;
    private final String desc;

    public static TextCommand findOnDesc(String desc) {
     return Arrays.stream(TextCommand.values())
                .filter(c -> c.getDesc().equals(desc))
                .findFirst().orElse(NONE);
    }
}