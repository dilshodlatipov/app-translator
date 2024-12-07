package org.example.apptranslator.service.bot;


import lombok.Getter;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Getter
public class TranslatorBot extends TelegramWebhookBot {
    private final String botUsername;
    private final String botPath;
    private final BotRouter botRouter;

    public TranslatorBot(String botToken,
                         SetWebhook setWebhook,
                         String botUsername,
                         String botPath,
                         BotRouter botRouter) throws TelegramApiException {
        super(botToken);
        super.setWebhook(setWebhook);
        this.botUsername = botUsername;
        this.botPath = botPath;
        this.botRouter = botRouter;
    }

    @SneakyThrows
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return botRouter.onWebhookUpdateReceived(update);
    }
}
