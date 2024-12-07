package org.example.apptranslator.service.bot;

import org.example.apptranslator.entity.UserEntity;
import org.example.apptranslator.enums.Language;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface DataProcessor {
    SendMessage processCommand(Language language, Message message, UserEntity user);

    SendMessage processText(UserEntity user, Message message);

    SendMessage translate(UserEntity user, Message message);
}
