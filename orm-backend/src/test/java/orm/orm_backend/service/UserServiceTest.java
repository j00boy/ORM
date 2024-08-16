package orm.orm_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import orm.orm_backend.entity.User;
import orm.orm_backend.repository.UserRepository;
import orm.orm_backend.util.KakaoUtil;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class UserServiceTest {

    @Spy
    KakaoUtil kakaoUtil;

    @Mock
    UserRepository userRepository;

    @Mock
    User user;

    @InjectMocks
    UserService userService;

    Integer userId;
    String firebaseToken = "thisIsFirebaseToken";

    @BeforeEach
    void setUp() {
        userId = 1;
        user = User.builder().build();
    }

    @Test
    void registerFirebaseTest() {
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));
        assertThat(user.getFirebaseToken()).isNull();
        userService.registerFirebaseToken(firebaseToken, userId);
        assertThat(user.getFirebaseToken()).isEqualTo(firebaseToken);
    }
}
