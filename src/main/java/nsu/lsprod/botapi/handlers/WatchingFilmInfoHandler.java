package nsu.lsprod.botapi.handlers;

import nsu.lsprod.botapi.BotState;
import nsu.lsprod.botapi.InputMessageHandler;
import nsu.lsprod.cache.UserDataCache;
import nsu.lsprod.database.entity.Film;
import nsu.lsprod.database.service.MovieScheduleService;
import nsu.lsprod.service.ReplyMessagesService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class WatchingFilmInfoHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    private MovieScheduleService movieScheduleService;

    public WatchingFilmInfoHandler(UserDataCache userDataCache,
                                   ReplyMessagesService messagesService,
                                   MovieScheduleService movieScheduleService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.movieScheduleService = movieScheduleService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_FILM_INFO;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.ASK_FILM_INFO)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askFilmInfo");
            replyToUser.setReplyMarkup(getFilmsButtonsMarkup());
        }

        return replyToUser;
    }


    public SendMessage watchingFilmInfo(int userId, String data, long chatId){
        Film film = movieScheduleService.findFilmByName(data);
        userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        String reply = "\"" + film.getName() + "\":\n" + film.getFilmInfo();
        return new SendMessage(chatId, reply);
    }


    private InlineKeyboardMarkup getFilmsButtonsMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<Film> filmList = movieScheduleService.getAllFilm();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (Film film : filmList) {
            InlineKeyboardButton buttonFilm = new InlineKeyboardButton().setText(film.getName());
            buttonFilm.setCallbackData("buttonFilmInfo/" + film.getName());
            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
            keyboardButtonsRow.add(buttonFilm);
            rowList.add(keyboardButtonsRow);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

}
