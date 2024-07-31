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

    private String mountainCode = "11111111";
    private String mountainName = "Mountain 1";
    private String address = "테스트도 테스트시 테스트구";

    private Float distance = 100F;
    private Float heuristic = 10F;
    private String startLatitude = "12.345678";
    private String startLongitude = "123.456789";
    private String peakLatitude = "12.345678";
    private String peakLongitude = "123.456789";

    private Integer difficulty = 1;
    private String latitude = "55.555555";
    private String longitude = "555.555555";

    @BeforeEach
    void setUp() {
        mountain = Mountain.builder()
                .mountainCode(mountainCode)
                .mountainName(mountainName)
                .address(address)
                .build();

        mountainRepository.save(mountain);

        trail = Trail.builder()
                .mountain(mountain)
                .distance(distance)
                .heuristic(heuristic)
                .startLatitude(startLatitude)
                .startLongitude(startLongitude)
                .peakLatitude(peakLatitude)
                .peakLongitude(peakLongitude)
                .build();

        trailRepository.save(trail);

        trailDetail1 = TrailDetail.builder()
                .trail(trail)
                .difficulty(difficulty)
                .latitude(latitude)
                .longitude(longitude)
                .build();

        trailDetail2 = TrailDetail.builder()
                .trail(trail)
                .difficulty(difficulty)
                .latitude(latitude)
                .longitude(longitude)
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
        assertThat(findTrailDetails).hasSize(2);
        assertThat(findTrailDetails.size()).isNotEqualTo(3);
        assertThat(findTrailDetails.get(0).getLatitude()).isEqualTo(latitude);
    }

}
