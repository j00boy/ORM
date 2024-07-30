package orm.orm_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import orm.orm_backend.configuration.Mountain100Config;
import orm.orm_backend.dto.response.MountainResponseDto;
import orm.orm_backend.entity.Mountain;
import orm.orm_backend.repository.MountainRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;


@Slf4j
@ExtendWith(MockitoExtension.class)
public class MountainServiceTest {

    @Mock
    MountainRepository mountainRepository;

    @Mock
    TrailService trailService;

    @InjectMocks
    MountainService mountainService;

    @Mock
    private Mountain mountain1;

    @Mock
    private Mountain mountain2;

    @BeforeEach
    void setUp() {
        lenient().when(mountain1.getId()).thenReturn(1);
        lenient().when(mountain1.getMountainName()).thenReturn("테스트 산1");
        lenient().when(mountain1.getMountainCode()).thenReturn("11111111");

        lenient().when(mountain2.getId()).thenReturn(2);
        lenient().when(mountain2.getMountainName()).thenReturn("테스트 산2");
        lenient().when(mountain2.getMountainCode()).thenReturn("22222222");
    }

    @Test
    @DisplayName("ID로 산 조회")
    void getMountainDtoByIdTest() {
        // given
        Integer id = 1;
        when(mountainRepository.findById(id)).thenReturn(Optional.ofNullable(mountain1));

        // when
        MountainResponseDto mountainDto = mountainService.getMountainDtoById(id);

        assertThat(mountainDto.getName()).isEqualTo("테스트 산1");
        assertThat(mountainDto.getCode()).isNotEqualTo("22222222");
    }

    @Test
    @DisplayName("이름으로 산 List 조회")
    void getAllMountainsTest() {
        // given
        List<Mountain> list = List.of(mountain1, mountain2);
        String name = "테스트";
        when(mountainRepository.findByMountainNameContaining(name)).thenReturn(list);

        // when
        List<MountainResponseDto> allMountains = mountainService.getAllMountains(name);

        // then
        assertThat(allMountains.size()).isEqualTo(2);
        assertThat(allMountains.get(0).getId()).isEqualTo(1);
        assertThat(allMountains.get(0).getName()).isEqualTo("테스트 산1");
        assertThat(allMountains.get(0).getCode()).isEqualTo("11111111");
        assertThat(allMountains.get(1).getId()).isEqualTo(2);
        assertThat(allMountains.get(1).getName()).isEqualTo("테스트 산2");
        assertThat(allMountains.get(1).getCode()).isEqualTo("22222222");
    }

    @Test
    @DisplayName("100대 명산 조회")
    void getFamousMountainsTest() {
        // given
        List<Mountain> list = List.of(mountain1, mountain2);
        List<String> theFamous = Mountain100Config.CODE_100;
        when(mountainRepository.findByMountainCodeIn(theFamous)).thenReturn(list);

        // when
        List<MountainResponseDto> mountains100 = mountainService.get100Mountains();

        // then
        assertThat(mountains100.get(0).getId()).isEqualTo(1);
        assertThat(mountains100.get(0).getName()).isEqualTo("테스트 산1");
        assertThat(mountains100.get(0).getCode()).isEqualTo("11111111");
        assertThat(mountains100.get(1).getId()).isEqualTo(2);
        assertThat(mountains100.get(1).getName()).isEqualTo("테스트 산2");
        assertThat(mountains100.get(1).getCode()).isEqualTo("22222222");
    }
}
