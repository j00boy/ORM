package orm.orm_backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import orm.orm_backend.dto.common.TraceCoordinateDto;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TraceCoordinate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Trace trace;
    private String latitude;
    private String longitude;
    private String altitude;
    private String time;
    private Integer difficulty;

    public TraceCoordinate(TraceCoordinateDto traceCoordinateDto, Trace trace) {
        this.trace = trace;
        this.latitude = traceCoordinateDto.getLatitude();
        this.longitude = traceCoordinateDto.getLongitude();
        this.altitude = traceCoordinateDto.getAltitude();
        this.time = traceCoordinateDto.getTime();
        this.difficulty = traceCoordinateDto.getDifficulty();
    }
}
