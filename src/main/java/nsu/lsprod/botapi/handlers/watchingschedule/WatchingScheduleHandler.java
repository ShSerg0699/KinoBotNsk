package nsu.lsprod.botapi.handlers.watchingschedule;

import nsu.lsprod.botapi.BotState;
import nsu.lsprod.botapi.InputMessageHandler;
import nsu.lsprod.cache.UserDataCache;
import nsu.lsprod.database.entity.Cinema;
import nsu.lsprod.database.entity.Film;
import nsu.lsprod.database.service.MovieScheduleService;
import nsu.lsprod.service.ReplyMessagesService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Component
public class WatchingScheduleHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    private MovieScheduleService movieScheduleService;

    public WatchingScheduleHandler(UserDataCache userDataCache,
                                   ReplyMessagesService messagesService,
                                   MovieScheduleService movieScheduleService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.movieScheduleService = movieScheduleService;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.SEARCHING_SCHEDULE)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_DATE);
        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SEARCHING_SCHEDULE;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.ASK_DATE)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askDate");
            replyToUser.setReplyMarkup(getDateButtonsMarkup());
        }

        return replyToUser;
    }

    public SendMessage askCinema(int userId, String data, long chatId) {
        UserInputData userInputData = userDataCache.getUserInputData(userId);
        userInputData.setDate(Date.valueOf(data));
        userDataCache.saveUserInputData(userId, userInputData);
        userDataCache.setUsersCurrentBotState(userId, BotState.ASK_CINEMA);
        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.askCinema");
        replyToUser.setReplyMarkup(getCinemaButtonsMarkup(userInputData.getDate()));
        return replyToUser;
    }

    public SendMessage askFilm(int userId, String data, long chatId) {
        UserInputData userInputData = userDataCache.getUserInputData(userId);
        userInputData.setCinema(movieScheduleService.findCinemaByName(data));
        userDataCache.saveUserInputData(userId, userInputData);
        userDataCache.setUsersCurrentBotState(userId, BotState.ASK_FILM);
        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.askFilm");
        replyToUser.setReplyMarkup(getFilmButtonsMarkup(userInputData.getDate(), userInputData.getCinema()));
        return replyToUser;
    }

    public SendMessage watchingTable(int userId, String data, long chatId){
        UserInputData userInputData = userDataCache.getUserInputData(userId);
        userInputData.setFilm(movieScheduleService.findFilmByName(data));
        userDataCache.saveUserInputData(userId, userInputData);
        userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        String reply = userInputData.getDate().toString() + " в кинотеатре \"" + userInputData.getCinema().getName() + "\" на фильм \"" + userInputData.getFilm().getName() + "\" есть следующие сеансы: \n";
        List<Time> timeList = movieScheduleService.getFilmTime(userInputData.getDate(), userInputData.getCinema(), userInputData.getFilm());
        for (Time time : timeList) {
            reply = reply + time.toString() + "\n";
        }
        userInputData.setDate(null);
        userInputData.setCinema(null);
        userInputData.setFilm(null);

        return new SendMessage(chatId, reply);
    }


    private InlineKeyboardMarkup getDateButtonsMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<Date> dateList = movieScheduleService.getAvailableDates();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (Date date : dateList) {
            InlineKeyboardButton buttonDate = new InlineKeyboardButton().setText(date.toString());
            buttonDate.setCallbackData("buttonDate/" + date.toString());
            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
            keyboardButtonsRow.add(buttonDate);
            rowList.add(keyboardButtonsRow);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    private InlineKeyboardMarkup getCinemaButtonsMarkup(Date date) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<Cinema> cinemaList = movieScheduleService.getAvailableCinemasByDate(date);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (Cinema cinema : cinemaList) {
            InlineKeyboardButton buttonCinema = new InlineKeyboardButton().setText(cinema.getName());
            buttonCinema.setCallbackData("buttonCinema/" + cinema.getName());
            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
            keyboardButtonsRow.add(buttonCinema);
            rowList.add(keyboardButtonsRow);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    private InlineKeyboardMarkup getFilmButtonsMarkup(Date date, Cinema cinema) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<Film> filmList = movieScheduleService.getAvailableFilmsByDateAndCinema(date, cinema);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (Film film : filmList) {
            InlineKeyboardButton buttonFilm = new InlineKeyboardButton().setText(film.getName());
            buttonFilm.setCallbackData("buttonFilm/" + film.getName());
            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
            keyboardButtonsRow.add(buttonFilm);
            rowList.add(keyboardButtonsRow);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

}
