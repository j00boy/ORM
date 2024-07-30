package orm.orm_backend.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import orm.orm_backend.dto.common.TraceDto;
import orm.orm_backend.dto.request.TraceRequestDto;

import java.lang.reflect.Field;
import java.sql.Date;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class TraceTest {

    Integer userId;
    User user;
    Trace trace;
    Mountain mountain;
    Trail trail;
    TraceRequestDto traceRequestDto;
    String traceTitle = "traceTitle";

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        userId = 1;
        user = new User();
        Field userIdField = User.class.getDeclaredField("id");
        userIdField.setAccessible(true);
        userIdField.set(user, userId);
        mountain = new Mountain();
        trail = new Trail();

        traceRequestDto = TraceRequestDto.builder().title(traceTitle)
                .hikingDate(new Date(System.currentTimeMillis()).toString()).build();

        trace = Trace.builder().user(user).trail(trail).mountain(mountain).traceRequestDto(traceRequestDto).build();
    }

    @Test
    void isOwnerTest() {
        assertThat(trace.isOwner(userId)).isEqualTo(true);
        assertThat(trace.isOwner(userId + 1)).isEqualTo(false);
        assertThat(trace.isOwner(null)).isEqualTo(false);
    }

    @Test
    void updateTraceTest() {
        trace = new Trace();
        assertThat(trace.getTitle()).isNull();
        assertThat(trace.getMountain()).isNull();
        assertThat(trace.getTrail()).isNull();

        trace.update(traceRequestDto, mountain, trail);
        assertThat(trace.getMountain()).isEqualTo(mountain);
        assertThat(trace.getTrail()).isEqualTo(trail);
        assertThat(trace.getTitle()).isEqualTo(traceTitle);
    }

    @Test
    void completeMeasureTest() {
        LocalDateTime startedAt = LocalDateTime.now().minusDays(1);
        LocalDateTime endedAt = LocalDateTime.now();
        Float maxHeight = 100.2f;
        TraceDto traceDto = TraceDto.builder()
                .hikingStartedAt(startedAt.toString())
                .hikingEndedAt(endedAt.toString())
                .maxHeight(maxHeight)
                .build();

        assertThat(trace.getStartTime()).isNull();
        assertThat(trace.getEndTime()).isNull();
        assertThat(trace.getMaxAltitude()).isNull();
        trace.completeMeasure(traceDto);
        assertThat(trace.getStartTime()).isEqualTo(startedAt);
        assertThat(trace.getEndTime()).isEqualTo(endedAt);
        assertThat(trace.getMaxAltitude()).isEqualTo(maxHeight);
    }
}
