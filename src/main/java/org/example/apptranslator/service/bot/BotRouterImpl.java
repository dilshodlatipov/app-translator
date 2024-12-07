package org.example.apptranslator.service.bot;

import org.example.apptranslator.entity.UserEntity;
import org.example.apptranslator.enums.BotStatus;
import org.example.apptranslator.enums.Language;
import org.example.apptranslator.exceptions.TelegramException;
import org.example.apptranslator.service.MessageService;
import org.example.apptranslator.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;

@Service
public class BotRouterImpl implements BotRouter {
    private final UserBotService userBotService;
    private final UserService userService;
    private final DataProcessor dataProcessor;

    public BotRouterImpl(@Lazy UserBotService userBotService,
                         @Lazy UserService userService,
                         @Lazy DataProcessor dataProcessor) {
        this.userBotService = userBotService;
        this.userService = userService;
        this.dataProcessor = dataProcessor;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            UserEntity user = userService.checkUser(message.getFrom());
            if (message.isCommand() && Objects.equals(message.getText(), "/start"))
                return userBotService.introduction(user);

            return switch (user.getStatus()) {
                case PAGE_ZERO -> isCommand(user, message);
                case LANGUAGE -> null;
                case SENDING_TEXT -> dataProcessor.processText(user, message);
                case SENDING_CONVERT_LANGUAGE -> dataProcessor.translate(user, message);
            };
        } else if (update.hasCallbackQuery()) {
            CallbackQuery query = update.getCallbackQuery();
            UserEntity user = userService.checkUser(query.getFrom());
            if (Objects.equals(user.getStatus(), BotStatus.LANGUAGE)) {
                try {
                    return userBotService.changeLanguage(user, query);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

    private SendMessage isCommand(UserEntity user, Message message) {
        if (message.isCommand()) {
            String command = message.getText();
            return switch (command) {
                case "/uzbek" -> dataProcessor.processCommand(Language.UZBEK, message, user);
                case "/russian" -> dataProcessor.processCommand(Language.RUSSIAN, message, user);
                case "/english" -> dataProcessor.processCommand(Language.ENGLISH, message, user);
                case "/language" -> userBotService.language(user);
                case "/help" -> userBotService.help(user);
                default ->
                        throw TelegramException.restThrow(MessageService.message(user, "invalid.input"), user.getTelegramId());
            };
        } else return null;
    }
}
