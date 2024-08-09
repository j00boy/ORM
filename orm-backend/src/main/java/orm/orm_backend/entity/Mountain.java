package orm.orm_backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static jakarta.persistence.FetchType.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mountain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String mountainName;
    private String mountainCode;
    private String address;
    private String addressLatitude;
    private String addressLongitude;
    private String imageSrc;
    private Float altitude;
    private String description;

    @OneToMany(mappedBy = "mountain", fetch = LAZY)
    private List<Trail> trails;

    @Builder
    public Mountain(String mountainName, String mountainCode, String address, String addressLatitude,
                    String addressLongitude, String imageSrc, Float altitude, String description) {
        this.mountainName = mountainName;
        this.mountainCode = mountainCode;
        this.address = address;
        this.addressLatitude = addressLatitude;
        this.addressLongitude = addressLongitude;
        this.imageSrc = imageSrc;
        this.altitude = altitude;
        this.description = description;
    }

}
