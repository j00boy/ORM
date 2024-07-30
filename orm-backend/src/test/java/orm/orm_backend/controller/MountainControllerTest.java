package orm.orm_backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import orm.orm_backend.dto.response.MountainResponseDto;
import orm.orm_backend.dto.response.TrailResponseDto;
import orm.orm_backend.service.MountainService;
import orm.orm_backend.service.TrailService;
import orm.orm_backend.util.JwtUtil;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MountainController.class)
@Slf4j
@MockBean(JpaMetamodelMappingContext.class)
public class MountainControllerTest {

    private final String url = "/mountains";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MountainService mountainService;

    @MockBean
    private TrailService trailService;

    @MockBean
    private JwtUtil jwtUtil;

    @Mock
    private MountainResponseDto mountain1;

    @Mock
    private MountainResponseDto mountain2;

    @Mock
    private TrailResponseDto trail1;

    @BeforeEach
    void setUp() {
        lenient().when(mountain1.getId()).thenReturn(1);
        lenient().when(mountain1.getName()).thenReturn("테스트 산1");
        lenient().when(mountain1.getCode()).thenReturn("11111111");

        lenient().when(mountain2.getId()).thenReturn(2);
        lenient().when(mountain2.getName()).thenReturn("테스트 산2");
        lenient().when(mountain2.getCode()).thenReturn("22222222");

        lenient().when(trail1.getId()).thenReturn(1);
        lenient().when(trail1.getDistance()).thenReturn(3.0F);
    }

    @Test
    @DisplayName("100대 명산 보여주기")
    void show100() throws Exception {
        String api = "/top";
        given(mountainService.get100Mountains()).willReturn(List.of(mountain1, mountain2));

        mockMvc.perform(get(url + api))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("테스트 산1"))
                .andExpect(jsonPath("$[0].code").value("11111111"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("테스트 산2"))
                .andExpect(jsonPath("$[1].code").value("22222222"));
    }

    @Test
    @DisplayName("id로 산 조회하기")
    void getMountainById() throws Exception {
        Integer id = 1;
        String api = "/" + id;
        given(mountainService.getMountainDtoById(id)).willReturn(mountain1);

        mockMvc.perform(get(url + api))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("테스트 산1"))
                .andExpect(jsonPath("$.code").value("11111111"));
    }

    @Test
    @DisplayName("이름 검색해서 산 list 조회하기")
    void getAllMountainsBySearch() throws Exception {
        String name = "테스트";
        String api = "/search";
        given(mountainService.getAllMountains(name)).willReturn(List.of(mountain1, mountain2));

        mockMvc.perform(get(url + api + "?name=" + name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("테스트 산1"))
                .andExpect(jsonPath("$[0].code").value("11111111"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("테스트 산2"))
                .andExpect(jsonPath("$[1].code").value("22222222"));
    }

    @Test
    @DisplayName("특정 등산로 검색")
    void getTrailById() throws Exception {
        Integer id = 1;
        String api = "/trail";
        given(trailService.getTrailById(id)).willReturn(trail1);

        mockMvc.perform(get(url + api + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.distance").value(3.0F));
    }
}
