package orm.orm_backend.dto.common;

import lombok.Builder;
import lombok.Getter;
import orm.orm_backend.entity.Trace;

import java.util.List;

@Getter
public class TraceDto {
    private Integer id;
    private String title;
    private String mountainName;
    private Integer mountainId;
    private String hikingDate;
    private Integer trailId;
    private String hikingStartedAt;
    private String hikingEndedAt;
    private Float maxHeight;
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

    @Builder
    public TraceDto(String title, String hikingStartedAt, String hikingEndedAt, Float maxHeight, String hikingDate) {
        this.title = title;
        this.hikingDate = hikingDate;
        this.hikingStartedAt = hikingStartedAt;
        this.hikingEndedAt = hikingEndedAt;
        this.maxHeight = maxHeight;
    }
}
