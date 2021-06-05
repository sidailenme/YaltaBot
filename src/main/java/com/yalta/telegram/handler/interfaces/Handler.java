package com.yalta.telegram.handler.interfaces;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Handler<T> {

    void handle(Update update);

}
