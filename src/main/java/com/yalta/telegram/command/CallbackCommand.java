package com.yalta.telegram.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum CallbackCommand {

    NONE("none"),

    CAFE_PAGE("cafePage"),
    RENT_PAGE("rentPage"),
    TAXI_PAGE("taxiPage");


    private final String data;

    public static CallbackCommand findByData(String command) {
        return Arrays.stream(CallbackCommand.values())
                .filter(c -> c.data.contains(command))
                .findFirst().orElse(NONE);
    }
}
