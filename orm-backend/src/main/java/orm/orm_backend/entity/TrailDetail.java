package orm.orm_backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TrailDetail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "trail_id")
    private Trail trail;

    private Integer difficulty;
    private String latitude;
    private String longitude;

    @Builder
    public TrailDetail(Trail trail, Integer difficulty, String latitude, String longitude) {
        this.trail = trail;
        this.difficulty = difficulty;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
