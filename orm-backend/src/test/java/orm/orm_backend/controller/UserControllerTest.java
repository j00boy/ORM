package orm.orm_backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
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

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(userController, "HEADER_AUTH", "Authorization");
        ReflectionTestUtils.setField(jwtUtil, "salt", "ORM-SALT-VALUE-40932999-071e-4368-a21f-6aa4b5fa16b0");
        ReflectionTestUtils.setField(jwtUtil, "accessTokenExpireTime", 360000000000L);
        userId = 2;
        accessToken = jwtUtil.createAccessToken(userId);
        log.info("accessToken={}", accessToken);
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