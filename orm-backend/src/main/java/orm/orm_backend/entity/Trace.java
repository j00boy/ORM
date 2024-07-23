package orm.orm_backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import orm.orm_backend.dto.request.TraceRequestDto;
import orm.orm_backend.dto.response.TraceResponseDto;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Trace extends BaseEntity {

    // 주석으로 처리된 부분은 추 후 마운틴 부분 DB 및 Entity생성이 완료된 후 대체할 예정

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Trail trail;

    @ManyToOne
    private User user;

//    @ManyToOne
//    private Mountain mountain;

    @Column(length = 30)
    private String title;

    private Date hikingDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Float maxAltitude;
    private String Course;

//    @Builder
//    public Trace(TraceCreationRequestDto traceCreationRequestDto, Mountain mountain, Trail trail, User user) {
//        this.title = traceCreationRequestDto.getTitle();
//        this.hikingDate = traceCreationRequestDto.getHikingDate();
//        this.mountain = mountain;
//        this.trail = trail;
//        this.user = user;
//    }

    @Builder
    public Trace(TraceRequestDto traceRequestDto, Trail trail, User user) {
        this.title = traceRequestDto.getTitle();
        this.hikingDate = traceRequestDto.getHikingDate();
        this.trail = trail;
        this.user = user;
    }

    public TraceResponseDto toResponseDto() {
        return TraceResponseDto.builder()
                .id(id)
                .title(title)
                .hikingDate(hikingDate)
                .hikingStartedAt(startTime)
                .hikingEndedAt(endTime)
                .maxHeight(maxAltitude).build();
    }

    public void update(TraceRequestDto traceRequestDto) {
        this.title = traceRequestDto.getTitle();
        this.hikingDate = traceRequestDto.getHikingDate();
    }

    public boolean isOwner(Integer userId) {
        Integer ownerId = user.getId();
        return ownerId != null && ownerId == userId;
    }

    public void completeMeasure(TraceResponseDto traceResponseDto) {
        this.startTime = traceResponseDto.getHikingStartedAt();
        this.endTime = traceResponseDto.getHikingEndedAt();
    }
}
