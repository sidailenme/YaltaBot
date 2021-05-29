package com.yalta.telegram.handler;

import com.yalta.telegram.command.Command;
import org.springframework.stereotype.Service;

@Service
public class MainHandler {

    public String operate(Command command) {
        return switch (command) {
            case NOT_A_COMMAND -> "Не является командой";
            case NONE -> "Команда не найдена";
            case START -> "tst";          //todo
        };
    }
}
