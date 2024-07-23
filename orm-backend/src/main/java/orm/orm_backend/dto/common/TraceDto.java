package orm.orm_backend.dto.common;

import lombok.Builder;
import lombok.Getter;

import java.sql.Date;
import java.time.LocalDateTime;

@Builder
@Getter
public class TraceDto {
    private final Integer id;
    private final String title;
    private final String mountainName;
    private final Integer mountainId;
    private final Date hikingDate;
    private final Integer trailId;
    private final LocalDateTime hikingStartedAt;
    private final LocalDateTime hikingEndedAt;
    private final Float maxHeight;
//    private final List<Coordinates>;
}
