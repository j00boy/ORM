package orm.orm_backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import orm.orm_backend.dto.response.LoginResponseDto;
import orm.orm_backend.service.UserService;
import orm.orm_backend.util.JwtUtil;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    JwtUtil jwtUtil;

    @MockBean
    UserService userService;

    int userId;
    String accessToken;

    @Autowired
    private UserController userController;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(userController, "HEADER_AUTH", "Authorization");
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