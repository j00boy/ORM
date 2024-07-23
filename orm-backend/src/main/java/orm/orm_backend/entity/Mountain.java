package orm.orm_backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


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
    private String imageSrc;
    private Double altitude;
    private String description;

    @Builder
    public Mountain(String mountainName, String mountainCode, String address, String imageSrc, Double altitude, String description) {
        this.mountainName = mountainName;
        this.mountainCode = mountainCode;
        this.address = address;
        this.imageSrc = imageSrc;
        this.altitude = altitude;
        this.description = description;
    }

}
