package orm.orm_backend.dto.common;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class TraceDto {
    private final Integer id;
    private final String title;
    private final String mountainName;
    private final Integer mountainId;
    private final String hikingDate;
    private final Integer trailId;
    private final String hikingStartedAt;
    private final String hikingEndedAt;
    private final Float maxHeight;
    private final List<TraceCoordinateDto> coordinates;
}
