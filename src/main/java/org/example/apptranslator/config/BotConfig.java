package org.example.apptranslator.config;

import lombok.RequiredArgsConstructor;
import org.example.apptranslator.service.bot.BotRouter;
import org.example.apptranslator.service.bot.TranslatorBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.facilities.filedownloader.TelegramFileDownloader;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Configuration
@RequiredArgsConstructor
public class BotConfig {
    @Value("${telegram.webhook-path}")
    private String botWebhookPath;
    @Value("${telegram.bot-token}")
    private String botToken;
    @Value("${telegram.bot-name}")
    private String botName;

    @Bean
    public SetWebhook setWebhook() {
        return SetWebhook.builder().url(botWebhookPath).build();
    }

    @Bean
    public TranslatorBot translatorBot(SetWebhook setWebhook, BotRouter botRouter) throws TelegramApiException {
        return new TranslatorBot(botToken, setWebhook, botName, botWebhookPath, botRouter);
    }

    @Bean
    public TelegramFileDownloader fileDownloader() {
        return new TelegramFileDownloader((() -> botToken));
    }
}
