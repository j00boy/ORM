package orm.orm_backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Builder
    public Trail(Mountain mountain, Float distance, Float heuristic, String startLatitude, String startLongitude, String peakLatitude, String peakLongitude) {
        this.mountain = mountain;
        this.distance = distance;
        this.heuristic = heuristic;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.peakLatitude = peakLatitude;
        this.peakLongitude = peakLongitude;
    }
}
