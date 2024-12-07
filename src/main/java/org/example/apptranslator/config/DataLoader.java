package org.example.apptranslator.config;

import lombok.RequiredArgsConstructor;
import org.example.apptranslator.service.bot.TranslatorBot;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final TranslatorBot translatorBot;
    private final SetWebhook setWebhook;

    @Override
    public void run(String... args) throws Exception {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(translatorBot, setWebhook);
    }
}
