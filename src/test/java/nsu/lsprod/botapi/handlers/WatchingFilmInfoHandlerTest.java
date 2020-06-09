package nsu.lsprod.botapi.handlers;

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

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class WatchingFilmInfoHandlerTest extends TestCase {
    @Mock
    UserDataCache userDataCache;

    @Mock
    ReplyMessagesService messagesService;

    @Mock
    MovieScheduleService movieScheduleService;

    @Mock
    Message message;

    @Mock
    User user;

    @InjectMocks
    WatchingFilmInfoHandler watchingFilmInfoHandler = new WatchingFilmInfoHandler(userDataCache, messagesService, movieScheduleService);

    @Test
    public void testHandle() {
        SendMessage sendMessage = new SendMessage(01L, "");
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
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        Mockito.when(message.getFrom()).thenReturn(user);
        Mockito.when(user.getId()).thenReturn(1);
        Mockito.when(userDataCache.getUsersCurrentBotState(1)).thenReturn(BotState.ASK_FILM_INFO);
        Mockito.when(movieScheduleService.getAllFilm()).thenReturn(filmList);
        Mockito.when(messagesService.getReplyMessage(0, "reply.askFilmInfo")).thenReturn(sendMessage);
        Assert.assertEquals(sendMessage, watchingFilmInfoHandler.handle(message));
    }

    @Test
    public void testWatchingFilmInfo() {
        SendMessage sendMessage = new SendMessage(01L, "\"Джентельмены\":\n" +
                "Оригинальное название: “The Gentlemen”\n" +
                "Жанр: боевик, комедия, криминал\n" +
                "Страна: США\n" +
                "Режиссер: Гай Ричи\n" +
                "Актеры: Мэттью МакКонахи, Чарли Ханнэм, Джереми Стронг, Мишель Докери, Колин Фаррелл\n" +
                "Рейтинг: 8.4\n" +
                "\n" +
                "По сюжету главный герой – обладающий непревзойденным умом и не менее непревзойденной " +
                "дерзостью выпускник самого Оксфорда – решает применить все свои качества не совсем стандартно " +
                "и совсем уж незаконно. Он придумывает уникальную нелегальную схему обогащения с помощью роскошных особняков " +
                "и поместий обанкротившихся английских аристократов. Но со временем он решает продать свой прибыльный, " +
                "но не совсем законный бизнес и вот тут-то и начинаются проблемы, которые выливаются в яркий, " +
                "но в то же время жесткий обмен любезностями между настоящими английским джентльменами.");

        Film film = new Film();
        film.setName("Джентельмены");
        film.setFilmInfo("Оригинальное название: “The Gentlemen”\n" +
                "Жанр: боевик, комедия, криминал\n" +
                "Страна: США\n" +
                "Режиссер: Гай Ричи\n" +
                "Актеры: Мэттью МакКонахи, Чарли Ханнэм, Джереми Стронг, Мишель Докери, Колин Фаррелл\n" +
                "Рейтинг: 8.4\n" +
                "\n" +
                "По сюжету главный герой – обладающий непревзойденным умом и не менее непревзойденной " +
                "дерзостью выпускник самого Оксфорда – решает применить все свои качества не совсем стандартно " +
                "и совсем уж незаконно. Он придумывает уникальную нелегальную схему обогащения с помощью роскошных особняков " +
                "и поместий обанкротившихся английских аристократов. Но со временем он решает продать свой прибыльный, " +
                "но не совсем законный бизнес и вот тут-то и начинаются проблемы, которые выливаются в яркий, " +
                "но в то же время жесткий обмен любезностями между настоящими английским джентльменами.");
        Mockito.when(movieScheduleService.findFilmByName("Джентельмены")).thenReturn(film);

        Assert.assertEquals(sendMessage, watchingFilmInfoHandler.watchingFilmInfo(1, "Джентельмены", 01L));
    }
}