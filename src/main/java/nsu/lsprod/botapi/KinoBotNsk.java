package nsu.lsprod.botapi;

import nsu.lsprod.database.service.MovieScheduleService;
import org.telegram.telegrambots.bots.DefaultBotOptions;
        import org.telegram.telegrambots.bots.TelegramWebhookBot;
        import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
        import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

public class KinoBotNsk extends TelegramWebhookBot {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    private TelegramInterface telegramInterface;


    public KinoBotNsk(DefaultBotOptions botOptions, TelegramInterface telegramInterface) {
        super(botOptions);
        this.telegramInterface = telegramInterface;
    }


    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        final BotApiMethod<?> replyMessageToUser = telegramInterface.handleUpdate(update);
        return replyMessageToUser;
    }


    public void setWebHookPath(String webHookPath) {
        this.webHookPath = webHookPath;
    }

    public void setBotUserName(String botUserName) {
        this.botUserName = botUserName;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

}