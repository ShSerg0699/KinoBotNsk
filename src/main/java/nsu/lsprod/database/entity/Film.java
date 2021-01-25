package nsu.lsprod.database.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String filmInfo;
}
