package org.example.apptranslator.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface TranslatorBotController {
    @PostMapping("/callback")
    BotApiMethod<?> onUpdateReceived(@RequestBody Update update);
}
