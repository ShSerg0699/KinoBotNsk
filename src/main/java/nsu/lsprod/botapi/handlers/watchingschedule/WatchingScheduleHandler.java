package nsu.lsprod.botapi.handlers.watchingschedule;

import nsu.lsprod.botapi.BotState;
import nsu.lsprod.botapi.InputMessageHandler;
import nsu.lsprod.cache.UserDataCache;
import nsu.lsprod.database.entity.Cinema;
import nsu.lsprod.database.entity.Film;
import nsu.lsprod.database.entity.MovieSchedule;
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
    private MovieSchedule movieSchedule = new MovieSchedule();//?


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
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.ASK_DATE)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askDate");
            replyToUser.setReplyMarkup(getDateButtonsMarkup());
        }

        if (botState.equals(BotState.ASK_CINEMA )) {
            movieSchedule.setDate(Date.valueOf(usersAnswer));
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askCinema");
            replyToUser.setReplyMarkup(getCinemaButtonsMarkup(movieSchedule.getDate()));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_FILM);
        }

        if (botState.equals(BotState.ASK_FILM)) {
            movieSchedule.setCinema(movieScheduleService.findCinemaByName(usersAnswer));
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askFilm");
            replyToUser.setReplyMarkup(getFilmButtonsMarkup(movieSchedule.getDate(), movieSchedule.getCinema()));
            userDataCache.setUsersCurrentBotState(userId, BotState.WATCHING_SCHEDULE);
        }

        if (botState.equals(BotState.WATCHING_SCHEDULE)) {
            movieSchedule.setFilm(movieScheduleService.findFilmByName(usersAnswer));
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
            String reply = "";
            List<Time> timeList = movieScheduleService.getFilmTime(movieSchedule.getDate(), movieSchedule.getCinema(), movieSchedule.getFilm());
            for (Time time : timeList) {
                reply = reply + time.toString() + " ";
            }
            replyToUser = messagesService.getReplyMessage(chatId, reply);
        }

        return replyToUser;
    }

    private InlineKeyboardMarkup getDateButtonsMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<Date> dateList = movieScheduleService.getAvailableDates();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for(Date date : dateList){
            InlineKeyboardButton buttonDate = new InlineKeyboardButton().setText(date.toString());
            buttonDate.setCallbackData("buttonDate " + date.toString());
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

        for(Cinema cinema : cinemaList){
            InlineKeyboardButton buttonCinema = new InlineKeyboardButton().setText(cinema.toString());
            buttonCinema.setCallbackData("buttonCinema " + cinema.getName());
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

        for(Film film : filmList){
            InlineKeyboardButton buttonFilm = new InlineKeyboardButton().setText(film.toString());
            buttonFilm.setCallbackData("buttonFilm " + film.getName());
            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
            keyboardButtonsRow.add(buttonFilm);
            rowList.add(keyboardButtonsRow);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    public MovieSchedule getMovieSchedule() {
        return movieSchedule;
    }
}
