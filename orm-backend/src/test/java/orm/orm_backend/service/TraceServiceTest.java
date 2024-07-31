package orm.orm_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import orm.orm_backend.dto.common.TraceDto;
import orm.orm_backend.dto.request.TraceRequestDto;
import orm.orm_backend.entity.Mountain;
import orm.orm_backend.entity.Trace;
import orm.orm_backend.entity.Trail;
import orm.orm_backend.entity.User;
import orm.orm_backend.exception.UnAuthorizedException;
import orm.orm_backend.repository.TraceImageRepository;
import orm.orm_backend.repository.TraceRepository;
import orm.orm_backend.util.ImageUtil;

import java.lang.reflect.Field;
import java.sql.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
class TraceServiceTest {

    @Mock
    TraceImageRepository traceImageRepository;

    @Mock
    TraceRepository traceRepository;

    @Mock
    MountainService mountainService;

    @Mock
    UserService userService;

    @Mock
    TrailService trailService;

    @Spy
    ImageUtil imageUtil = new ImageUtil();

    @InjectMocks
    TraceService traceService;

    @Mock
    User user;
    Integer userId;

    @Mock
    Mountain mountain;

//    @Mock
    Trace trace;

    TraceRequestDto traceRequestDto;
    String traceTitle = "traceTitle";
    Trail trail;
    Integer mountainId;
    Integer trailId;
    Integer traceId;

    @BeforeEach
    void init() throws NoSuchFieldException, IllegalAccessException {
        userId = 2;
        lenient().when(user.getId()).thenReturn(userId);
        mountainId = 1;
        trailId = 1;
        trail = null;
        traceRequestDto = TraceRequestDto.builder().title(traceTitle)
                .mountainId(mountainId)
                .trailId(trailId)
                .hikingDate(new Date(System.currentTimeMillis()).toString())
                .build();

        lenient().when(mountain.getId()).thenReturn(mountainId);
        trace = Trace.builder().traceRequestDto(traceRequestDto).mountain(mountain).trail(trail).user(user).build();
        traceId = 1;
//        when(trace.getId()).thenReturn(traceId);
    }

    @Test
    void createTraceTest() {
        when(userService.findUserById(userId)).thenReturn(user);
        when(traceRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(mountainService.getMountainById(mountainId)).thenReturn(mountain);
        when(trailService.getTrailEntityById(trailId)).thenReturn(trail);
        TraceDto traceDto = traceService.createTrace(traceRequestDto, userId);
        isSameDay(Date.valueOf(traceDto.getHikingDate()), new Date(System.currentTimeMillis()));
        assertThat(traceDto.getTitle()).isEqualTo(traceTitle);
    }

    @Test
    void updateTraceTest() {
        when(traceRepository.findById(traceId)).thenReturn(Optional.ofNullable(trace));
        when(mountainService.getMountainById(mountainId)).thenReturn(mountain);
        when(trailService.getTrailEntityById(trailId)).thenReturn(trail);
        assertThatThrownBy(() -> traceService.updateTrace(traceId, traceRequestDto, userId + 1))
                .isInstanceOf(UnAuthorizedException.class);

        String updateTitle = "updateTitle";
        Date updateDate = new Date(System.currentTimeMillis());
        TraceRequestDto updateTraceDto = TraceRequestDto.builder().id(traceId).title(updateTitle).hikingDate(updateDate.toString())
                .mountainId(mountainId).trailId(trailId).build();

        TraceDto traceDto = traceService.updateTrace(traceId, updateTraceDto, userId);
        assertThat(traceDto.getTitle()).isEqualTo(updateTitle);
        assertThat(traceDto.getHikingDate()).isEqualTo(updateDate.toString());
    }

    void isSameDay(Date date1, Date date2) {
        assertThat(date1.toLocalDate()).isEqualTo(date2.toLocalDate());
    }
}