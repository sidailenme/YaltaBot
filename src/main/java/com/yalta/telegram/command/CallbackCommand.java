//package com.yalta.telegram.command;
//
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//
//import java.util.Arrays;
//
//@Getter
//@RequiredArgsConstructor
//public enum CallbackCommand {
//
//    NONE("none"),
//    NEXT_PAGE_OF_CAFE("nextPageOfCafe"),
//    PREVIOUS_PAGE_OF_CAFE("previousPageOfCafe");
//
//
//    private final String data;
//
//    public static CallbackCommand findByData(String data) {
//        return Arrays.stream(CallbackCommand.values())
//                .filter(c -> c.data.equals(data))
//                .findFirst().orElse(NONE);
//    }
//}
