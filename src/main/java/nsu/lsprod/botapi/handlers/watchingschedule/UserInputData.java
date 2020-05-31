package nsu.lsprod.botapi.handlers.watchingschedule;

import lombok.Getter;
import lombok.Setter;
import nsu.lsprod.database.entity.Cinema;
import nsu.lsprod.database.entity.Film;

import java.sql.Date;

@Getter
@Setter
public class UserInputData {
    Date date;
    Cinema cinema;
    Film film;
}
