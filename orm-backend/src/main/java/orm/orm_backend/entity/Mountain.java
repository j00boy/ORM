package orm.orm_backend.entity;

import jakarta.persistence.*;

@Entity
public class Mountain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}
