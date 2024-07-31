package orm.orm_backend.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import orm.orm_backend.configuration.Mountain100Config;
import orm.orm_backend.entity.Mountain;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
public class MountainRepositoryTest {

    @Autowired
    private MountainRepository mountainRepository;

    private List<Mountain> list = new ArrayList<>();
    private Mountain mountain1;
    private Mountain mountain2;
    private Mountain mountain3;
    private List<String> famous;

    private String mountain1Name = "테스트 산1";
    private String mountain1Code = "11111111";
    private String mountain1address = "테스트도 테스트시 테스트동";
    private String mountain1imageSrc = "images/test1.jpg";
    private String mountain1description = "test1";

    private String mountain2Name = "테스트 산2";
    private String mountain2Code = "22222222";
    private String mountain2address = "테스트도 테스트시 테스트동";
    private String mountain2imageSrc = "images/test2.jpg";
    private String mountain2description = "test2";

    private String mountain3Name = "일반 산3";
    private String mountain3Code = "33333333";
    private String mountain3address = "테스트도 테스트시 테스트동";
    private String mountain3imageSrc = "images/test3.jpg";
    private String mountain3description = "test3";

    @BeforeEach
    void setUp() {
        mountain1 = Mountain.builder()
                .mountainName(mountain1Name)
                .mountainCode(mountain1Code)
                .address(mountain1address)
                .imageSrc(mountain1imageSrc)
                .description(mountain1description)
                .build();

        mountain2 = Mountain.builder()
                .mountainName(mountain2Name)
                .mountainCode(mountain2Code)
                .address(mountain2address)
                .imageSrc(mountain2imageSrc)
                .description(mountain2description)
                .build();

        mountain3 = Mountain.builder()
                .mountainName(mountain3Name)
                .mountainCode(mountain3Code)
                .address(mountain3address)
                .imageSrc(mountain3imageSrc)
                .description(mountain3description)
                .build();

        list.add(mountain1);
        list.add(mountain2);
        list.add(mountain3);

        mountainRepository.saveAll(list);
    }

    @Test
    @DisplayName("이름 검색으로 산 가져오기")
    void getMountainByName() throws Exception {
        // Given 생략

        // When
        List<Mountain> mountains = mountainRepository.findByMountainNameContaining("테스트");

        // Then
        assertThat(mountains).hasSize(2);
        assertThat(mountains).containsExactly(mountain1, mountain2);
        assertThat(mountains).doesNotContain(mountain3);
    }

    @Test
    @DisplayName("100대 명산 가져오기")
    void get100Mountains() throws Exception {
        // given
        famous = Mountain100Config.CODE_100;

        // When
        List<Mountain> famousMountains = mountainRepository.findByMountainCodeIn(famous);

        // Then
        assertThat(famousMountains).hasSize(18);
        assertThat(famousMountains).doesNotContain(mountain1);
    }
 
}
