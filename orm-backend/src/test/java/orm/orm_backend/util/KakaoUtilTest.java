package orm.orm_backend.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import orm.orm_backend.entity.User;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class KakaoUtilTest {

    @Autowired
    private KakaoUtil kakaoUtil;

    @Value("${kakao.example-refreshToken}")
    private String kakaoRefreshToken;

    private User user;

    @BeforeEach
    void init() {
        user = User.builder().kakaoRefreshToken(kakaoRefreshToken).build();
    }

    @Test
    void refreshAccessTokenTest() throws JsonProcessingException {
        kakaoUtil.refreshAccessToken(kakaoRefreshToken, user);
        assertThat(user.getKakaoRefreshToken()).isNotNull();
        assertThat(user.getKakaoAccessToken()).isNotNull();
    }
}
