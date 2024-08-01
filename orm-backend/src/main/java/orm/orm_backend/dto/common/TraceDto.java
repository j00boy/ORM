package orm.orm_backend.dto.common;

import lombok.Getter;
import orm.orm_backend.entity.Trace;

import java.util.List;

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
    private List<TraceCoordinateDto> coordinates;

    public TraceDto(Trace trace) {
        this.id = trace.getId();
        this.title = trace.getTitle();
        this.mountainName = trace.getMountain().getMountainName();
        this.mountainId = trace.getMountain().getId();
        this.hikingDate = trace.getHikingDate().toString();
        this.trailId = trace.getTrail().getId();
        this.hikingStartedAt = trace.getStartTime() == null ? "" : trace.getStartTime().toString();
        this.hikingEndedAt = trace.getEndTime() == null ? "" : trace.getEndTime().toString();
        this.maxHeight= trace.getMaxAltitude();
    }
}
