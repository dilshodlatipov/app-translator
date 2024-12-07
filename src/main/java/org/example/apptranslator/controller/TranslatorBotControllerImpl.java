package org.example.apptranslator.controller;

import lombok.RequiredArgsConstructor;
import org.example.apptranslator.service.bot.TranslatorBot;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequiredArgsConstructor
public class TranslatorBotControllerImpl implements TranslatorBotController {
    private final TranslatorBot bot;

    @Override
    public BotApiMethod<?> onUpdateReceived(Update update) {
        return bot.onWebhookUpdateReceived(update);
    }
}
