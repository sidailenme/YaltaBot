package com.yalta.telegram.config;

import com.yalta.telegram.Core;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class Config {

    @Bean
    @SneakyThrows
    public BotSession botSession(Core core) {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            return telegramBotsApi.registerBot(core);
    }
}
