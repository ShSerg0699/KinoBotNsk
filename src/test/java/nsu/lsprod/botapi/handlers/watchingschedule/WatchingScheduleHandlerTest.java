package nsu.lsprod.botapi.handlers.watchingschedule;

import junit.framework.TestCase;
import nsu.lsprod.botapi.BotState;
import nsu.lsprod.cache.UserDataCache;
import nsu.lsprod.database.entity.Cinema;
import nsu.lsprod.database.entity.Film;
import nsu.lsprod.database.service.MovieScheduleService;
import nsu.lsprod.service.ReplyMessagesService;
import org.junit.Assert;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.xml.crypto.Data;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class WatchingScheduleHandlerTest extends TestCase {
    @Mock
    UserDataCache userDataCache;

    @Mock
    ReplyMessagesService messagesService;

    @Mock
    MovieScheduleService movieScheduleService;

    @Mock
    UserInputData userInputData;

    @Mock
    Message message;

    @Mock
    User user;

    @InjectMocks
    WatchingScheduleHandler watchingScheduleHandler = new WatchingScheduleHandler(userDataCache,messagesService,movieScheduleService);

    @Test
    public void testHandle() {
        SendMessage sendMessage = new SendMessage(01L, "");
        List<Date> dateList = new ArrayList<>();
        dateList.add(Date.valueOf("2020-06-01"));
        dateList.add(Date.valueOf("2020-06-02"));
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        InlineKeyboardButton buttonDate = new InlineKeyboardButton().setText("2020-06-01");
        buttonDate.setCallbackData("buttonDate/" + "2020-06-01");
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(buttonDate);
        rowList.add(keyboardButtonsRow);

        InlineKeyboardButton buttonDate2 = new InlineKeyboardButton().setText("2020-06-02");
        buttonDate2.setCallbackData("buttonDate/" + "2020-06-0");
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(buttonDate2);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        Mockito.when(message.getFrom()).thenReturn(user);
        Mockito.when(user.getId()).thenReturn(1);
        Mockito.when(userDataCache.getUsersCurrentBotState(1)).thenReturn(BotState.ASK_DATE);
        Mockito.when(movieScheduleService.getAvailableDates()).thenReturn(dateList);
        Mockito.when(messagesService.getReplyMessage(0, "reply.askDate")).thenReturn(sendMessage);
        Assert.assertEquals(sendMessage, watchingScheduleHandler.handle(message));
    }

    @Test
    public void testAskCinema() {
        SendMessage replyToUser = new SendMessage(01L,"reply.askCinema");
        List<Cinema> cinemaList = new ArrayList<>();
        Cinema cinema = new Cinema();
        cinema.setName("Кронверк Синема Мегаплекс");
        cinemaList.add(cinema);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        InlineKeyboardButton buttonDate = new InlineKeyboardButton().setText("Кронверк Синема Мегаплекс");
        buttonDate.setCallbackData("buttonCinema/" + "Кронверк Синема Мегаплекс");
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(buttonDate);
        rowList.add(keyboardButtonsRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        replyToUser.setReplyMarkup(inlineKeyboardMarkup);


        Mockito.when(userDataCache.getUserInputData(1)).thenReturn(userInputData);
        Mockito.when(messagesService.getReplyMessage(01L, "reply.askCinema")).thenReturn(new SendMessage(01L,"reply.askCinema"));
        Mockito.when(userInputData.getDate()).thenReturn(Date.valueOf("2020-06-01"));
        Mockito.when(movieScheduleService.getAvailableCinemasByDate(Date.valueOf("2020-06-01"))).thenReturn(cinemaList);

        Assert.assertEquals(replyToUser, watchingScheduleHandler.askCinema(1, "2020-06-01", 01L));
    }

    @Test
    public void testAskFilm() {
        SendMessage replyToUser = new SendMessage(01L,"reply.askFilm");
        Cinema cinema = new Cinema();
        cinema.setName("Кронверк Синема Мегаплекс");
        Film film = new Film();
        film.setName("Джентельмены");
        List<Film> filmList = new ArrayList<>();
        filmList.add(film);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        InlineKeyboardButton buttonDate = new InlineKeyboardButton().setText("Джентельмены");
        buttonDate.setCallbackData("buttonFilm/" + "Джентельмены");
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(buttonDate);
        rowList.add(keyboardButtonsRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        replyToUser.setReplyMarkup(inlineKeyboardMarkup);

        Mockito.when(userDataCache.getUserInputData(1)).thenReturn(userInputData);
        Mockito.when(messagesService.getReplyMessage(01L, "reply.askFilm")).thenReturn(new SendMessage(01L,"reply.askFilm"));
        Mockito.when(userInputData.getDate()).thenReturn(Date.valueOf("2020-06-01"));

        Mockito.when(userInputData.getCinema()).thenReturn(cinema);
        Mockito.when(movieScheduleService.getAvailableFilmsByDateAndCinema(Date.valueOf("2020-06-01"), cinema)).thenReturn(filmList);

        Assert.assertEquals(replyToUser, watchingScheduleHandler.askFilm(1, "Кронверк Синема Мегаплекс", 01L));
    }

    @Test
    public void testWatchingTable() {
        SendMessage replyToUser = new SendMessage(01L,"2020-06-01 в кинотеатре \"Кронверк Синема Мегаплекс\" на фильм \"Джентельмены\" есть следующие сеансы: \n");

        Mockito.when(userDataCache.getUserInputData(1)).thenReturn(userInputData);
        Mockito.when(messagesService.getReplyMessage(01L, "reply.askFilm")).thenReturn(new SendMessage(01L,"reply.askFilm"));
        Mockito.when(userInputData.getDate()).thenReturn(Date.valueOf("2020-06-01"));
        Cinema cinema = new Cinema();
        cinema.setId(1L);
        cinema.setName("Кронверк Синема Мегаплекс");
        Mockito.when(userInputData.getCinema()).thenReturn(cinema);
        Film film = new Film();
        film.setId(1L);
        film.setName("Джентельмены");
        Mockito.when(userInputData.getFilm()).thenReturn(film);
        Assert.assertEquals(replyToUser, watchingScheduleHandler.watchingTable(1, "Джентельмены", 01L));
    }
}