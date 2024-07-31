package orm.orm_backend.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import orm.orm_backend.entity.Mountain;
import orm.orm_backend.entity.Trail;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
public class TrailRepositoryTest {

    @Autowired
    private TrailRepository trailRepository;

    @Autowired
    private MountainRepository mountainRepository;

    private Mountain mountain;
    private Trail trail1;
    private Trail trail2;
    private List<Trail> trails;

    @BeforeEach
    void setUp() throws Exception {
        mountain = Mountain.builder()
                .mountainCode("11111111")
                .mountainName("Mountain 1")
                .address("테스트도 테스트시 테스트구")
                .build();

        mountainRepository.save(mountain);

        trail1 = Trail.builder()
                .mountain(mountain)
                .distance(100F)
                .heuristic(10F)
                .startLatitude("12.345678")
                .startLongitude("123.456789")
                .peakLatitude("98.765432")
                .peakLongitude("987.654321")
                .build();

        trail2 = Trail.builder()
                .mountain(mountain)
                .distance(100F)
                .heuristic(10F)
                .startLatitude("12.345678")
                .startLongitude("123.456789")
                .peakLatitude("98.765432")
                .peakLongitude("987.654321")
                .build();

        trails = new ArrayList<>();
        trails.add(trail1);
        trails.add(trail2);

        trailRepository.saveAll(trails);
    }

    @Test
    @DisplayName("산 ID로 등산로 조회하기")
    void showTrailsBySameMountainId() {
        // given
        Integer id = mountain.getId();

        // when
        List<Trail> findList = trailRepository.findByMountainId(id);

        // then
        assertThat(findList.size()).isNotEqualTo(3);
        assertThat(findList).contains(trail1, trail2);
    }
}
