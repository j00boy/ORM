package orm.orm_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import orm.orm_backend.dto.common.TraceDto;
import orm.orm_backend.dto.request.TraceRequestDto;
import orm.orm_backend.service.TraceService;
import orm.orm_backend.util.JwtUtil;

import java.sql.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TraceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@TestPropertySource(locations = "classpath:application.yml")
@Slf4j
public class TraceControllerTest {

    @SpyBean
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TraceService traceService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${test.user-access-token}")
    private String userAccessToken;

    @Value("${orm.header.auth}")
    private String authHeader;

    private Integer userId = 2;
    private TraceRequestDto traceRequestDto;
    private TraceDto traceDto;
    private String traceTitle = "traceTitle";
    private String hikingDate = new Date(System.currentTimeMillis()).toString();
    private String url = "/trace";

    @BeforeEach
    void setUp() throws Exception {
        traceRequestDto = TraceRequestDto.builder()
                .hikingDate(hikingDate)
                .title(traceTitle)
                .build();
        traceDto = TraceDto.builder()
                .title(traceTitle)
                .hikingDate(hikingDate)
                .build();
    }

    @Test
    public void createTraceTest() throws Exception {
        when(traceService.createTrace(any(TraceRequestDto.class), any(Integer.class))).thenReturn(traceDto);
        TraceDto result = traceService.createTrace(traceRequestDto, userId);
        mockMvc.perform(post("/trace/create")
                        .header(authHeader, userAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(traceRequestDto)))
                .andExpect(jsonPath("$.title").value(traceTitle))
                .andExpect(status().isCreated());
    }
}
