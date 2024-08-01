package orm.orm_backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import orm.orm_backend.dto.common.TraceDto;
import orm.orm_backend.dto.request.TraceRequestDto;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Trace extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Trail trail;

    @ManyToOne
    private User user;

    @ManyToOne
    private Mountain mountain;

    @Column(length = 30)
    private String title;

    private Date hikingDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Float maxAltitude;

    @Builder
    public Trace(TraceRequestDto traceRequestDto, Mountain mountain, Trail trail, User user) {
        this.title = traceRequestDto.getTitle();
        this.hikingDate = Date.valueOf(traceRequestDto.getHikingDate());
        this.mountain = mountain;
        this.trail = trail;
        this.user = user;
    }

    public void update(TraceRequestDto traceRequestDto, Mountain mountain, Trail trail) {
        this.title = traceRequestDto.getTitle();
        this.hikingDate = Date.valueOf(traceRequestDto.getHikingDate());
        this.mountain = mountain;
        this.trail = trail;
    }

    public boolean isOwner(Integer userId) {
        Integer ownerId = user.getId();
        return ownerId != null && ownerId == userId;
    }

    public void completeMeasure(TraceDto traceDto) {
        this.startTime = LocalDateTime.parse(traceDto.getHikingStartedAt());
        this.endTime = LocalDateTime.parse(traceDto.getHikingEndedAt());
        this.maxAltitude = traceDto.getMaxHeight();
    }
}
