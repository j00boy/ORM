package orm.orm_backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Trail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "mountain_id")
    private Mountain mountain;

    private Float distance;
    private Float heuristic;
    private String startLatitude;
    private String startLongitude;
    private String peakLatitude;
    private String peakLongitude;

}
