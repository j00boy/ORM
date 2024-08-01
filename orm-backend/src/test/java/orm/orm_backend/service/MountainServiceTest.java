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
import orm.orm_backend.dto.response.MountainDto;
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

    private Integer mountain1Id = 1;
    private String mountain1Name = "Mountain 1";
    private String mountain1Code = "11111111";

    private Integer mountain2Id = 2;
    private String mountain2Name = "Mountain 2";
    private String mountain2Code = "22222222";

    @BeforeEach
    void setUp() {
        lenient().when(mountain1.getId()).thenReturn(mountain1Id);
        lenient().when(mountain1.getMountainName()).thenReturn(mountain1Name);
        lenient().when(mountain1.getMountainCode()).thenReturn(mountain1Code);

        lenient().when(mountain2.getId()).thenReturn(mountain2Id);
        lenient().when(mountain2.getMountainName()).thenReturn(mountain2Name);
        lenient().when(mountain2.getMountainCode()).thenReturn(mountain2Code);
    }

    @Test
    @DisplayName("ID로 산 조회")
    void getMountainDtoByIdTest() {
        // given
        Integer id = 1;
        when(mountainRepository.findById(id)).thenReturn(Optional.ofNullable(mountain1));

        // when
        MountainResponseDto mountainDto = mountainService.getMountainDtoById(id);

        assertThat(mountainDto.getName()).isEqualTo(mountain1.getMountainName());
        assertThat(mountainDto.getCode()).isNotEqualTo(mountain2.getMountainCode());
    }

    @Test
    @DisplayName("이름으로 산 List 조회")
    void getAllMountainsTest() {
        // given
        List<Mountain> list = List.of(mountain1, mountain2);
        String name = "테스트";
        when(mountainRepository.findByMountainNameContaining(name)).thenReturn(list);

        // when
        List<MountainDto> allMountains = mountainService.getMountainsBySearch(name);

        // then
        assertThat(allMountains).hasSize(2);
        assertThat(allMountains.get(0).getId()).isEqualTo(mountain1.getId());
        assertThat(allMountains.get(0).getName()).isEqualTo(mountain1.getMountainName());
        assertThat(allMountains.get(0).getCode()).isEqualTo(mountain1.getMountainCode());
        assertThat(allMountains.get(1).getId()).isNotEqualTo(100);
        assertThat(allMountains.get(1).getCode()).isNotEqualTo("Error");
        assertThat(allMountains.get(1).getCode()).isNotEqualTo("99999999");
    }

    @Test
    @DisplayName("100대 명산 조회")
    void getFamousMountainsTest() {
        // given
        List<Mountain> list = List.of(mountain1, mountain2);
        List<String> theFamous = Mountain100Config.CODE_100;
        when(mountainRepository.findByMountainCodeIn(theFamous)).thenReturn(list);

        // when
        List<MountainDto> mountains100 = mountainService.get100Mountains();

        // then
        assertThat(mountains100.size()).isEqualTo(2);
        assertThat(mountains100.get(0).getId()).isEqualTo(mountain1.getId());
        assertThat(mountains100.get(0).getName()).isEqualTo(mountain1.getMountainName());
        assertThat(mountains100.get(0).getCode()).isEqualTo(mountain1.getMountainCode());
        assertThat(mountains100.get(1).getId()).isNotEqualTo(100);
        assertThat(mountains100.get(1).getName()).isNotEqualTo("Error");
        assertThat(mountains100.get(1).getCode()).isNotEqualTo("99999999");
    }
}
