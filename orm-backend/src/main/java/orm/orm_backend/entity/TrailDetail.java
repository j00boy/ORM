package orm.orm_backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TrailDetail {

    @Id @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "trail_id")
    private Trail trail;

    private String latitude;
    private String longitude;

}
