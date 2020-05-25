package nsu.lsprod.database.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class UsersInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private boolean isNeedNotification;

    @ManyToMany
    private List<Cinema> selectedCinemas;

}
