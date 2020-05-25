package nsu.lsprod.database.repository;

import nsu.lsprod.database.entity.Cinema;
import nsu.lsprod.database.entity.Film;
import nsu.lsprod.database.entity.MovieSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface MovieScheduleRepository extends JpaRepository<MovieSchedule, Long> {
    List<MovieSchedule> findAllByDate(Date date);

    List<MovieSchedule> findAllByDateAndCinema(Date date, Cinema cinema);

    MovieSchedule findByDateAndCinemaAndFilm(Date date, Cinema cinema, Film film);
}
