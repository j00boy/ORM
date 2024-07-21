package orm.orm_backend.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    JwtUtil jwtUtil;

    int userId = 10;
    String accessToken;

    @BeforeEach
    void init() {
        accessToken = jwtUtil.createAccessToken(userId);
    }

    @Test
    void checkTokenTest() {
        assertThat(jwtUtil.checkToken(accessToken)).isTrue();
        String otherServiceToken = "askdjsflk32jlvksdml2k2l3";
        assertThat(jwtUtil.checkToken(otherServiceToken)).isFalse();
    }
}