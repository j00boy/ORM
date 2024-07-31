package orm.orm_backend.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import orm.orm_backend.entity.Mountain;
import orm.orm_backend.entity.Trail;
import orm.orm_backend.entity.TrailDetail;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
public class TrailDetailRepositoryTest {

    @Autowired
    private TrailDetailRepository trailDetailRepository;

    @Autowired
    private TrailRepository trailRepository;

    @Autowired
    private MountainRepository mountainRepository;

    private Mountain mountain;
    private Trail trail;
    private TrailDetail trailDetail1;
    private TrailDetail trailDetail2;
    private List<TrailDetail> trailDetails;

    @BeforeEach
    void setUp() {
        mountain = Mountain.builder()
                .mountainCode("11111111")
                .mountainName("Mountain 1")
                .address("테스트도 테스트시 테스트구")
                .build();

        mountainRepository.save(mountain);

        trail = Trail.builder()
                .mountain(mountain)
                .distance(100F)
                .heuristic(10F)
                .startLatitude("12.345678")
                .startLongitude("123.456789")
                .peakLatitude("98.765432")
                .peakLongitude("987.654321")
                .build();

        trailRepository.save(trail);

        trailDetail1 = TrailDetail.builder()
                .trail(trail)
                .difficulty(1)
                .latitude("11.111111")
                .longitude("222.222222")
                .build();

        trailDetail2 = TrailDetail.builder()
                .trail(trail)
                .difficulty(2)
                .latitude("33.333333")
                .longitude("444.444444")
                .build();

        trailDetails = new ArrayList<>();

        trailDetails.add(trailDetail1);
        trailDetails.add(trailDetail2);

        trailDetailRepository.saveAll(trailDetails);
    }

    @Test
    @DisplayName("등산로ID로 등산로 좌표 가져오기")
    void showTrail() {
        // given
        Integer trailId = trail.getId();

        // when
        List<TrailDetail> findTrailDetails = trailDetailRepository.findTrailDetailsByTrailId(trailId);

        // then
        assertThat(findTrailDetails.size()).isNotEqualTo(3);
        assertThat(findTrailDetails.get(0).getLatitude()).isEqualTo("11.111111");
    }

}
