package orm.orm_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import orm.orm_backend.dto.response.TrailDetailResponseDto;
import orm.orm_backend.dto.response.TrailResponseDto;
import orm.orm_backend.entity.Mountain;
import orm.orm_backend.entity.Trail;
import orm.orm_backend.entity.TrailDetail;
import orm.orm_backend.repository.TrailDetailRepository;
import orm.orm_backend.repository.TrailRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class TrailServiceTest {

    @Mock
    private TrailRepository trailRepository;

    @Mock
    private TrailDetailRepository trailDetailRepository;

    @InjectMocks
    private TrailService trailService;

    @Mock
    private Mountain mountain;

    @Mock
    private Trail trail1;

    @Mock
    private Trail trail2;

    @Mock
    private TrailDetail trailDetail1;

    @Mock
    private TrailDetail trailDetail2;

    private Integer mountainId = 1;
    private String mountainName = "Mountain 1";
    private String mountainCode = "11111111";

    private Integer trail1Id = 2;
    private Integer trail2Id = 3;

    private Integer trailDetail1Id = 4;
    private Integer trailDetail1Difficulty = 1;

    private Integer trailDetail2Id = 5;
    private Integer trailDetail2Difficulty = 2;

    @BeforeEach
    public void setUp() {
        lenient().when(mountain.getId()).thenReturn(mountainId);
        lenient().when(mountain.getMountainName()).thenReturn(mountainName);
        lenient().when(mountain.getMountainCode()).thenReturn(mountainCode);

        lenient().when(trail1.getId()).thenReturn(trail1Id);
        lenient().when(trail1.getMountain()).thenReturn(mountain);

        lenient().when(trail2.getId()).thenReturn(trail2Id);
        lenient().when(trail2.getMountain()).thenReturn(mountain);

        lenient().when(trailDetail1.getId()).thenReturn(trailDetail1Id);
        lenient().when(trailDetail1.getTrail()).thenReturn(trail1);
        lenient().when(trailDetail1.getDifficulty()).thenReturn(trailDetail1Difficulty);

        lenient().when(trailDetail2.getId()).thenReturn(trailDetail2Id);
        lenient().when(trailDetail2.getDifficulty()).thenReturn(trailDetail2Difficulty);
    }

    @Test
    @DisplayName("산 ID로 등산로 List 가져오기")
    void showTrails() {
        // given
        List<Trail> list = List.of(trail1, trail2);
        when(trailRepository.findByMountainId(mountainId)).thenReturn(list);

        // when
        List<TrailResponseDto> trailsByMountainId = trailService.getTrailsByMountainId(mountainId);

        // then
        assertThat(trailsByMountainId).hasSize(2);
        assertThat(trailsByMountainId.get(0).getId()).isNotEqualTo(trail2.getId());
    }

    @Test
    @DisplayName("등산로 ID로 등산롸 좌표 List 가져오기")
    void showTrailDetails() {
        // given
        List<TrailDetail> list = List.of(trailDetail1, trailDetail2);
        when(trailDetailRepository.findTrailDetailsByTrailId(trail1Id)).thenReturn(list);

        // when
        List<TrailDetailResponseDto> allTrailDetailsByTrailId = trailService.getAllTrailDetailsByTrailId(trail1Id);

        // then
        assertThat(allTrailDetailsByTrailId).hasSize(2);
        assertThat(allTrailDetailsByTrailId.get(0).getDifficulty()).isEqualTo(trailDetail1Difficulty);
    }
}
