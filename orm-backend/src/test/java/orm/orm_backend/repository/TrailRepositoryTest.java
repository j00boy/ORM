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

    private String mountainCode = "11111111";
    private String mountainName = "Mountain 1";
    private String address = "테스트도 테스트시 테스트구";

    private Float distance = 100F;
    private Float heuristic = 10F;
    private String startLatitude = "12.345678";
    private String startLongitude = "123.456789";
    private String peakLatitude = "12.345678";
    private String peakLongitude = "123.456789";

    @BeforeEach
    void setUp() throws Exception {
        mountain = Mountain.builder()
                .mountainCode(mountainCode)
                .mountainName(mountainName)
                .address(address)
                .build();

        mountainRepository.save(mountain);

        trail1 = Trail.builder()
                .mountain(mountain)
                .distance(distance)
                .heuristic(heuristic)
                .startLatitude(startLatitude)
                .startLongitude(startLongitude)
                .peakLatitude(peakLatitude)
                .peakLongitude(peakLongitude)
                .build();

        trail2 = Trail.builder()
                .mountain(mountain)
                .distance(distance)
                .heuristic(heuristic)
                .startLatitude(startLatitude)
                .startLongitude(startLongitude)
                .peakLatitude(peakLatitude)
                .peakLongitude(peakLongitude)
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
        assertThat(findList).hasSize(2);
        assertThat(findList.size()).isNotEqualTo(3);
        assertThat(findList).containsExactly(trail1, trail2);
    }
}
