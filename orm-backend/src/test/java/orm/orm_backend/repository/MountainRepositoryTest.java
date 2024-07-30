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

    @BeforeEach
    void setUp() {
        mountain1 = Mountain.builder()
                .mountainName("테스트 산1")
                .mountainCode("11111111")
                .address("테스트도 테스트시 테스트동")
                .imageSrc("images/mountain1.jpg")
                .description("test1")
                .build();

        mountain2 = Mountain.builder()
                .mountainName("테스트 산2")
                .mountainCode("22222222")
                .address("테스트도 테스트시 테스트동")
                .imageSrc("images/mountain2.jpg")
                .description("test2")
                .build();

        mountain3 = Mountain.builder()
                .mountainName("test 3")
                .mountainCode("33333333")
                .address("test test test")
                .imageSrc("images/mountain3.jpg")
                .description("test3")
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
        assertThat(mountains).contains(mountain1, mountain2);
        assertThat(mountains).doesNotContain(mountain3);

        mountains.forEach(mountain -> log.info(mountain.getMountainName()));
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
