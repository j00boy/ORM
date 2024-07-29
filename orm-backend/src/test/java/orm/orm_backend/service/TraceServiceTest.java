package orm.orm_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import orm.orm_backend.dto.common.TraceDto;
import orm.orm_backend.dto.request.TraceRequestDto;
import orm.orm_backend.entity.Trail;
import orm.orm_backend.entity.User;
import orm.orm_backend.repository.TraceImageRepository;
import orm.orm_backend.repository.TraceRepository;
import orm.orm_backend.util.ImageUtil;

import java.sql.Date;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
class TraceServiceTest {

    @Mock
    TraceImageRepository traceImageRepository;

    @Mock
    TraceRepository traceRepository;

    @Mock
    UserService userService;

    @Spy
    ImageUtil imageUtil = new ImageUtil();

    @InjectMocks
    TraceService traceService;

    User user;
    Integer userId;
    Trail trail;
    TraceRequestDto traceRequestDto;
    String traceTitle = "traceTitle";

    @BeforeEach
    void init() {
        user = User.builder().build();
        userId = 2;
        trail = null;
        traceRequestDto = TraceRequestDto.builder().title(traceTitle)
                .hikingDate(new Date(System.currentTimeMillis()).toString())
                .build();
    }

    @Test
    void createTraceTest() {
        when(userService.findUserById(userId)).thenReturn(user);
        when(traceRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        TraceDto traceDto = traceService.createTrace(traceRequestDto, userId);
        isSameDay(Date.valueOf(traceDto.getHikingDate()), new Date(System.currentTimeMillis()));
        assertThat(traceDto.getTitle()).isEqualTo(traceTitle);
    }

    void isSameDay(Date date1, Date date2) {
        assertThat(date1.toLocalDate()).isEqualTo(date2.toLocalDate());
    }
}