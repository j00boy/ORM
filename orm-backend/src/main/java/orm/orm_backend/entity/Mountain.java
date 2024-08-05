package orm.orm_backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


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
    private String address_latitude;
    private String address_longitude;
    private String imageSrc;
    private Float altitude;
    private String description;

    @OneToMany(mappedBy = "mountain", fetch = FetchType.EAGER)
    private List<Trail> trails;

    @Builder
    public Mountain(String mountainName, String mountainCode, String address, String address_latitude,
                    String address_longitude, String imageSrc, Float altitude, String description) {
        this.mountainName = mountainName;
        this.mountainCode = mountainCode;
        this.address = address;
        this.address_latitude = address_latitude;
        this.address_longitude = address_longitude;
        this.imageSrc = imageSrc;
        this.altitude = altitude;
        this.description = description;
    }

}
