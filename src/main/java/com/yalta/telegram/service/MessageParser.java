package com.yalta.telegram.service;

import com.yalta.telegram.command.Command;
import org.springframework.stereotype.Service;

@Service
public class MessageParser {

    public Command parse(String cmd) {
        cmd = cmd.strip();
        if (cmd.contains(" ")) {
            cmd = cmd.substring(0, cmd.indexOf(" "));
        }
        return isCommand(cmd) ? Command.findOnDesc(cmd) : Command.NOT_A_COMMAND;
    }

    private boolean isCommand(String text) {
        return text.startsWith("/");
    }
}