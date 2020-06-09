package nsu.lsprod.database.service;

import junit.framework.TestCase;
import nsu.lsprod.database.entity.Cinema;
import nsu.lsprod.database.entity.Film;
import nsu.lsprod.database.entity.MovieSchedule;
import nsu.lsprod.database.repository.CinemaRepository;
import nsu.lsprod.database.repository.FilmRepository;
import nsu.lsprod.database.repository.MovieScheduleRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class MovieScheduleServiceTest extends TestCase {
    @Mock
    MovieScheduleRepository movieScheduleRepository;

    @Mock
    CinemaRepository cinemaRepository;

    @Mock
    FilmRepository filmRepository;

    @Mock
    Date date;

    @Mock
    MovieSchedule movieSchedule;

    @Mock
    Cinema cinema;

    @Mock
    Film film;


    @Mock
    Time time;

    @InjectMocks
    MovieScheduleService movieScheduleService = new MovieScheduleService(movieScheduleRepository, cinemaRepository, filmRepository);

    @Test
    public void testGetAvailableDates() {
        List<Date> dateList = new ArrayList<>();
        dateList.add(Date.valueOf("2020-06-01"));

        List<MovieSchedule> movieScheduleList = new ArrayList<>();
        MovieSchedule movieSchedule1 = new MovieSchedule();
        movieSchedule1.setDate(Date.valueOf("2020-06-01"));
        movieScheduleList.add(movieSchedule1);
        MovieSchedule movieSchedule2 = new MovieSchedule();
        movieSchedule2.setDate(Date.valueOf("2020-06-01"));
        movieScheduleList.add(movieSchedule2);
        Mockito.when(movieScheduleRepository.findAll()).thenReturn(movieScheduleList);

        Assert.assertEquals(dateList, movieScheduleService.getAvailableDates());
    }

    @Test
    public void testGetAvailableCinemasByDate() {
        List<Cinema> expected = new ArrayList<>();
        List<MovieSchedule> list = new ArrayList<>();
        list.add(movieSchedule);
        Mockito.when(movieSchedule.getCinema()).thenReturn(cinema);
        expected.add(cinema);
        Mockito.when(movieScheduleRepository.findAllByDate(date)).thenReturn(list);
        Assert.assertEquals(expected, movieScheduleService.getAvailableCinemasByDate(date));
    }

    @Test
    public void testGetAvailableFilmsByDateAndCinema() {
        List<Film> filmList = new ArrayList<>();
        Cinema cinema = new Cinema();
        cinema.setName("Кронверк Синема Мегаплекс");
        List<MovieSchedule> movieScheduleList = new ArrayList<>();
        Film film = new Film();
        film.setName("Джентельмены");
        filmList.add(film);
        MovieSchedule movieSchedule = new MovieSchedule();
        movieSchedule.setFilm(film);
        movieSchedule.setCinema(cinema);
        movieScheduleList.add(movieSchedule);

        Mockito.when(movieScheduleRepository.findAllByDate(Date.valueOf("2020-06-01"))).thenReturn(movieScheduleList);
        Assert.assertEquals(filmList, movieScheduleService.getAvailableFilmsByDateAndCinema(Date.valueOf("2020-06-01"), cinema));
    }

    @Test
    public void testGetFilmTime() {
        List<Time> expected = new ArrayList<>();
        List<MovieSchedule> list = new ArrayList<>();
        list.add(movieSchedule);
        Mockito.when(movieSchedule.getCinema()).thenReturn(cinema);
        Mockito.when(movieSchedule.getFilm()).thenReturn(film);
        Mockito.when(movieSchedule.getTime()).thenReturn(time);
        Mockito.when(movieScheduleRepository.findAllByDate(date)).thenReturn(list);
        expected.add(time);
        Assert.assertEquals(expected, movieScheduleService.getFilmTime(date, cinema, film));
    }

    @Test
    public void testGetAllFilm() {
        List<Film> expected = new ArrayList<>();
        Mockito.when(filmRepository.findAll()).thenReturn(expected);
        Assert.assertEquals(expected, movieScheduleService.getAllFilm());
    }

    @Test
    public void testFindCinemaByName() {
        Optional<Cinema> optional = Optional.of(cinema);
        Cinema expected = optional.get();
        Mockito.when(cinemaRepository.findByName("test")).thenReturn(optional);
        Assert.assertEquals(expected, movieScheduleService.findCinemaByName("test"));
    }

    @Test
    public void testFindFilmByName() {
        Optional<Film> optional = Optional.of(film);
        Film expected = optional.get();
        Mockito.when(filmRepository.findByName("test")).thenReturn(optional);
        Assert.assertEquals(expected, movieScheduleService.findFilmByName("test"));
    }
}