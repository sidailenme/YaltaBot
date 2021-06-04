package com.yalta.telegram.handler.interfaces;

import com.yalta.telegram.command.Command;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Handler<T> {

    void handle(Update update);

    T operate(Command command);

}
