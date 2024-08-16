package orm.orm_backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import orm.orm_backend.dto.response.LoginResponseDto;
import orm.orm_backend.service.UserService;
import orm.orm_backend.util.JwtUtil;
import orm.orm_backend.util.KakaoUtil;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application.yml")
class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @SpyBean
    JwtUtil jwtUtil;

    @MockBean
    KakaoUtil kakaoUtil;

    @MockBean
    UserService userService;

    @MockBean
    JpaMetamodelMappingContext jpaMetamodelMappingContext;

    int userId;
    String accessToken;

    @Autowired
    private UserController userController;

    @Value("${orm.header.auth}")
    private String HEADER_AUTH;

    @Value("${jwt.salt}")
    private String salt;

    @Value("${jwt.access-token.expiretime}")
    private Long accessTokenExpireTime;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(userController, "HEADER_AUTH", HEADER_AUTH);
        ReflectionTestUtils.setField(jwtUtil, "salt", salt);
        ReflectionTestUtils.setField(jwtUtil, "accessTokenExpireTime", accessTokenExpireTime);
        userId = 2;
        accessToken = jwtUtil.createAccessToken(userId);
    }

    @Test
    void autoLoginTest() throws Exception {
        when(userService.autoLogin(userId)).thenReturn(LoginResponseDto.builder().build());
        String url = "/users/login/auto";
        mvc.perform(get(url).header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(header().exists("accessToken"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}