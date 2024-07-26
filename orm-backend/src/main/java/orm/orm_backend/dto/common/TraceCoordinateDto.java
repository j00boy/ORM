package orm.orm_backend.dto.common;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TraceCoordinateDto {

    private Integer id;
    private Integer traceId;
    private String latitude;
    private String longitude;
    private String altitude;
    private String time;
    private Integer difficulty;
}
