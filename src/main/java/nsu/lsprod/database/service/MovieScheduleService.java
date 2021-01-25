package nsu.lsprod.database.service;

import nsu.lsprod.database.entity.Cinema;
import nsu.lsprod.database.entity.Film;
import nsu.lsprod.database.entity.MovieSchedule;
import nsu.lsprod.database.repository.CinemaRepository;
import nsu.lsprod.database.repository.FilmRepository;
import nsu.lsprod.database.repository.MovieScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class MovieScheduleService {
    private MovieScheduleRepository movieScheduleRepository;
    private CinemaRepository cinemaRepository;
    private FilmRepository filmRepository;


    @Autowired
    public MovieScheduleService(MovieScheduleRepository movieScheduleRepository, CinemaRepository cinemaRepository, FilmRepository filmRepository) {
        this.movieScheduleRepository = movieScheduleRepository;
        this.cinemaRepository = cinemaRepository;
        this.filmRepository = filmRepository;
    }

    public List<Date> getAvailableDates() {
        List<MovieSchedule> movieScheduleList = movieScheduleRepository.findAll();
        List<Date> availableDateList = new ArrayList<>();
        for (MovieSchedule movieSchedule : movieScheduleList) {
            Date availableDate = movieSchedule.getDate();
            boolean isOnList = false;
            for (Date date : availableDateList) {
                if (date.equals(availableDate)) {
                    isOnList = true;
                    break;
                }
            }
            if (!isOnList) {
                availableDateList.add(availableDate);
            }
        }
        return availableDateList;
    }

    public List<Cinema> getAvailableCinemasByDate(Date date) {
        List<MovieSchedule> movieScheduleList = movieScheduleRepository.findAllByDate(date);
        List<Cinema> availableCinemaList = new ArrayList<>();
        for (MovieSchedule movieSchedule : movieScheduleList) {
            Cinema availableCinema = movieSchedule.getCinema();
            boolean isOnList = false;
            for (Cinema cinema : availableCinemaList) {
                if(cinema.equals(availableCinema)){
                    isOnList = true;
                    break;
                }
            }
            if(!isOnList){
                availableCinemaList.add(availableCinema);
            }
        }
        return availableCinemaList;
    }

    public List<Film> getAvailableFilmsByDateAndCinema(Date date, Cinema cinema){
        List<MovieSchedule> movieScheduleList = movieScheduleRepository.findAllByDate(date);
        Iterator<MovieSchedule> movieScheduleIterator = movieScheduleList.iterator();
        while (movieScheduleIterator.hasNext()){
            if(!movieScheduleIterator.next().getCinema().equals(cinema)){
                movieScheduleIterator.remove();
            }
        }
        List<Film> availableFilmList = new ArrayList<>();
        for (MovieSchedule movieSchedule : movieScheduleList){
            Film availableFilm = movieSchedule.getFilm();
            boolean isOnList = false;
            for (Film film : availableFilmList) {
                if(film.equals(availableFilm)){
                    isOnList = true;
                    break;
                }
            }
            if(!isOnList){
                availableFilmList.add(availableFilm);
            }
        }
        return availableFilmList;
    }

    public List<Time> getFilmTime(Date date, Cinema cinema, Film film){
        List<MovieSchedule> movieScheduleList = movieScheduleRepository.findAllByDate(date);
        Iterator<MovieSchedule> movieScheduleIterator = movieScheduleList.iterator();
        while (movieScheduleIterator.hasNext()){
            MovieSchedule movieSchedule = movieScheduleIterator.next();
            if(!movieSchedule.getCinema().equals(cinema)){
                movieScheduleIterator.remove();
                continue;
            }
            if(!movieSchedule.getFilm().equals(film)){
                movieScheduleIterator.remove();
                continue;
            }
        }
        List<Time> timeList = new ArrayList<>();
        for(MovieSchedule movieSchedule : movieScheduleList){
            timeList.add(movieSchedule.getTime());
        }
        return timeList;
    }


    public void addMovieSchedule(MovieSchedule movieSchedule){
        MovieSchedule newMovieSchedule = new MovieSchedule();
        Optional<Film> optionalFilm = filmRepository.findByName(movieSchedule.getFilm().getName());
        if(!optionalFilm.isPresent()){
            return;
        }
        Film newFilm = new Film();
        newFilm.setName(movieSchedule.getFilm().getName());
        newFilm.setFilmInfo(movieSchedule.getFilm().getFilmInfo());
        filmRepository.save(newFilm);
        Optional<Cinema> optionalCinema = cinemaRepository.findByName(movieSchedule.getCinema().getName());
        if (!optionalCinema.isPresent()){
            return;
        }
        Cinema newCinema = new Cinema();
        newCinema.setName(movieSchedule.getCinema().getName());
        newCinema.setAddress(movieSchedule.getCinema().getAddress());
        cinemaRepository.save(newCinema);
        newMovieSchedule.setDate(movieSchedule.getDate());
        newMovieSchedule.setCinema(newCinema);
        newMovieSchedule.setFilm(newFilm);
        newMovieSchedule.setTime(movieSchedule.getTime());
        movieScheduleRepository.save(newMovieSchedule);
    }

    public List<Film> getAllFilm(){
        return filmRepository.findAll();
    }

    public Cinema findCinemaByName(String name){
        Optional<Cinema> optionalCinema = cinemaRepository.findByName(name);
        if(!optionalCinema.isPresent()){
            return null;
        }
        return  optionalCinema.get();
    }

    public Film findFilmByName(String name){
        Optional<Film> optionalFilm = filmRepository.findByName(name);
        if(!optionalFilm.isPresent()){
            return null;
        }
        return optionalFilm.get();
    }
}
