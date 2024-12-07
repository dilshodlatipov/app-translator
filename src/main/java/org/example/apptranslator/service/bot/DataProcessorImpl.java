package org.example.apptranslator.service.bot;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.apptranslator.entity.Translation;
import org.example.apptranslator.entity.UserEntity;
import org.example.apptranslator.enums.BotStatus;
import org.example.apptranslator.enums.Language;
import org.example.apptranslator.exceptions.TelegramException;
import org.example.apptranslator.service.MessageService;
import org.example.apptranslator.service.TranslationService;
import org.example.apptranslator.service.TranslatorService;
import org.example.apptranslator.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DataProcessorImpl implements DataProcessor {
    private final UserService userService;
    private final TranslationService translationService;
    private final TranslatorService translatorService;

    @Override
    @Transactional
    public SendMessage processCommand(Language language, Message message, UserEntity user) {
        Translation translation = translationService.saveTranslation(user, Translation.builder()
                .sourceLanguage(language)
                .build()
        );

        userService.setStatus(user.getId(), BotStatus.SENDING_TEXT);
        return SendMessage.builder()
                .chatId(user.getTelegramId())
                .text(MessageService.message(user, "translate.text"))
                .build();
    }

    @Override
    @Transactional
    public SendMessage processText(UserEntity user, Message message) {
        if (!message.hasText())
            throw TelegramException.restThrow(MessageService.message(user, "invalid.input"), user.getTelegramId());
        Translation lastTranslation = translationService.getLastTranslation(user);
        lastTranslation.setText(message.getText());
        translationService.updateTranslation(lastTranslation.getId(), lastTranslation);

        userService.setStatus(user.getId(), BotStatus.SENDING_CONVERT_LANGUAGE);

        KeyboardRow english = new KeyboardRow(List.of(
                KeyboardButton.builder().text(MessageService.message(user, "language.english")).build()
        ));
        KeyboardRow russian = new KeyboardRow(List.of(
                KeyboardButton.builder().text(MessageService.message(user, "language.russian")).build()
        ));
        KeyboardRow uzbek = new KeyboardRow(List.of(
                KeyboardButton.builder().text(MessageService.message(user, "language.uzbek")).build()
        ));
        List<KeyboardRow> keyboard = new ArrayList<>(2);
        switch (lastTranslation.getSourceLanguage()) {
            case ENGLISH -> {
                keyboard.add(russian);
                keyboard.add(uzbek);
            }
            case RUSSIAN -> {
                keyboard.add(english);
                keyboard.add(uzbek);
            }
            case UZBEK -> {
                keyboard.add(english);
                keyboard.add(russian);
            }
        }
        return SendMessage.builder()
                .chatId(user.getTelegramId())
                .text(MessageService.message(user, "translate.to"))
                .replyMarkup(
                        ReplyKeyboardMarkup.builder()
                                .oneTimeKeyboard(true)
                                .resizeKeyboard(true)
                                .keyboard(keyboard)
                                .build()
                )
                .build();
    }

    @Override
    public SendMessage translate(UserEntity user, Message message) {
        if (!message.hasText())
            throw TelegramException.restThrow(MessageService.message(user, "invalid.input"), user.getTelegramId());
        Translation lastTranslation = translationService.getLastTranslation(user);
        Language targetLanguage;
        if (Objects.equals(MessageService.message(user, "language.english"), message.getText()))
            targetLanguage = Language.ENGLISH;
        else if (Objects.equals(MessageService.message(user, "language.russian"), message.getText()))
            targetLanguage = Language.RUSSIAN;
        else if (Objects.equals(MessageService.message(user, "language.uzbek"), message.getText()))
            targetLanguage = Language.UZBEK;
        else throw TelegramException.restThrow(MessageService.message(user, "invalid.input"), user.getTelegramId());

        if (Objects.equals(targetLanguage, lastTranslation.getSourceLanguage()))
            throw TelegramException.restThrow(MessageService.message(user, "invalid.input"), user.getTelegramId());

        String translated = translatorService.translate(lastTranslation.getText(), lastTranslation.getSourceLanguage(), targetLanguage);

        lastTranslation.setText(translated);
        lastTranslation.setTargetLanguage(targetLanguage);
        translationService.updateTranslation(lastTranslation.getId(), lastTranslation);

        userService.setStatus(user.getId(), BotStatus.PAGE_ZERO);

        return SendMessage.builder()
                .chatId(user.getTelegramId())
                .text(translated)
                .replyMarkup(
                        ReplyKeyboardRemove.builder()
                                .removeKeyboard(true)
                                .build()
                )
                .build();
    }
}
