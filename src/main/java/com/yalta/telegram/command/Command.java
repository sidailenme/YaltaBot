package com.yalta.telegram.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum Command {

    NONE("/none"),
    NOT_A_COMMAND("/not_a_command"),

    START("/start"),
    INFO("/info"),
    FEEDBACK("/feedback"),

//    TODAY("/today"),

    CATALOG("/catalog"),

    CAFE("/cafe");
//    ENTERTAINMENT("/entertainment"),
//    SIGHTS("/sights"),
//    DELIVERY("/delivery"),
//    RENT("/rent"),
//    TAXI("/taxi"),
//    AUTO("/auto"),
//
//    CINEMA("/cinema");


    private final String desc;

    public static Command findOnDesc(String desc) {
        Optional<Command> optionalCommand = Arrays.stream(Command.values())
                .filter(c -> c.getDesc().equals(desc))
                .findFirst();
        return optionalCommand.orElse(NONE);
    }
}