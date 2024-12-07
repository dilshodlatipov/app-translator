package org.example.apptranslator.service.bot;

import lombok.RequiredArgsConstructor;
import org.example.apptranslator.entity.UserEntity;
import org.example.apptranslator.enums.BotStatus;
import org.example.apptranslator.enums.Language;
import org.example.apptranslator.exceptions.TelegramException;
import org.example.apptranslator.service.MessageService;
import org.example.apptranslator.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBotServiceImpl implements UserBotService {
    private final String INTRODUCTION = "user.introduction";
    private final String USER_WAIT = "user.wait";
    private final String USER_HELP = "user.help";
    private final String LANGUAGE = "user.language";
    private final String RUSSIAN = "language.russian";
    private final String ENGLISH = "language.english";
    private final String UZBEK = "language.uzbek";
    private final String BACK = "user.back";

    private final UserService userService;
    private final TranslatorBot bot;

    @Override
    public SendMessage introduction(UserEntity user) {
        userService.setStatus(user.getId(), BotStatus.PAGE_ZERO);

        return SendMessage.builder()
                .chatId(user.getTelegramId())
                .text(MessageService.message(user, INTRODUCTION))
                .replyMarkup(
                        ReplyKeyboardRemove.builder()
                                .removeKeyboard(true)
                                .build())
                .build();
    }

    @Override
    public SendMessage cancelAction(UserEntity from) {
        userService.setStatus(from.getId(), BotStatus.PAGE_ZERO);
        return SendMessage.builder()
                .text("Let's party.")
                .chatId(from.getTelegramId())
                .replyMarkup(
                        ReplyKeyboardRemove.builder()
                                .removeKeyboard(true)
                                .build())
                .build();
    }

    @Override
    public SendMessage language(UserEntity user) {
        userService.setStatus(user.getId(), BotStatus.LANGUAGE);
        return SendMessage.builder()
                .chatId(user.getTelegramId())
                .text(MessageService.message(user, LANGUAGE))
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboard(List.of(
                                        getButton(user, ENGLISH),
                                        getButton(user, RUSSIAN),
                                        getButton(user, UZBEK),
                                        getButton(user, BACK)
                                )
                        )
                        .build())
                .build();
    }

    @Override
    public SendMessage waiting(UserEntity user) {
        return SendMessage.builder()
                .chatId(user.getTelegramId())
                .text(MessageService.message(user, USER_WAIT))
                .build();
    }

    @Override
    public SendMessage help(UserEntity user) {
        return SendMessage.builder()
                .chatId(user.getTelegramId())
                .text(MessageService.message(user, USER_HELP))
                .build();
    }

    @Override
    public SendMessage changeLanguage(UserEntity user, CallbackQuery query) throws TelegramApiException {
        Integer messageId = query.getMessage().getMessageId();
        String data = query.getData();
        switch (data) {
            case ENGLISH -> user.setLanguage(Language.ENGLISH);
            case RUSSIAN -> user.setLanguage(Language.RUSSIAN);
            case UZBEK -> user.setLanguage(Language.UZBEK);
            case BACK -> {
                userService.setStatus(user.getId(), BotStatus.PAGE_ZERO);
                DeleteMessage deleteMessage = DeleteMessage.builder()
                        .chatId(user.getTelegramId())
                        .messageId(messageId)
                        .build();
                bot.execute(deleteMessage);
                return introduction(user);
            }
            default -> throw TelegramException.restThrow("Wrong callback query", user.getTelegramId());
        }
        userService.save(user);
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId(user.getTelegramId())
                .messageId(messageId)
                .text(MessageService.message(user, LANGUAGE))
                .replyMarkup(
                        InlineKeyboardMarkup.builder()
                                .keyboard(
                                        List.of(
                                                getButton(user, ENGLISH),
                                                getButton(user, RUSSIAN),
                                                getButton(user, UZBEK),
                                                getButton(user, BACK)
                                        )
                                )
                                .build()
                )
                .build();
        bot.execute(editMessageText);
        return null;
    }

    private List<InlineKeyboardButton> getButton(UserEntity user, String command) {
        return List.of(
                InlineKeyboardButton.builder()
                        .text(MessageService.message(user, command))
                        .callbackData(command)
                        .build()
        );
    }
}
