package orm.orm_backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import orm.orm_backend.dto.request.TraceCreationRequestDto;

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
    private String Course;

    @Builder
    public Trace(Trail trail, User user, String title, Date hikingDate) {
        this.trail = trail;
        this.user = user;
        this.title = title;
        this.hikingDate = hikingDate;
    }

    public Trace(TraceCreationRequestDto traceCreationRequestDto, Mountain mountain, Trail trail) {
        this.title = traceCreationRequestDto.getTitle();
        this.hikingDate = traceCreationRequestDto.getHikingDate();
        this.mountain = mountain;
        this.trail = trail;
    }
}
