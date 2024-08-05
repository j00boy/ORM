package orm.orm_backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Trail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mountain_id")
    private Mountain mountain;

    private Float distance;
    private Float heuristic;
    private Integer time;
    private String startLatitude;
    private String startLongitude;
    private String peakLatitude;
    private String peakLongitude;

    @OneToMany(mappedBy = "trail", fetch = FetchType.LAZY)
    private List<TrailDetail> trailDetails;

    @Builder
    public Trail(Mountain mountain, Float distance, Float heuristic, Integer time, String startLatitude, String startLongitude, String peakLatitude, String peakLongitude) {
        this.mountain = mountain;
        this.distance = distance;
        this.heuristic = heuristic;
        this.time = time;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.peakLatitude = peakLatitude;
        this.peakLongitude = peakLongitude;
    }
}
