package orm.orm_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import orm.orm_backend.entity.Club;
import orm.orm_backend.entity.User;
import orm.orm_backend.util.FirebaseUtil;

@Slf4j
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:test-application.properties")
public class FirebaseServiceTest {

    @InjectMocks
    FirebasePushAlertService firebasePushAlertService;

    @Spy
    FirebaseUtil firebaseUtil;

    @Value("${firebase.test-token}")
    private String testFirebaseToken;

    @Value("${firebase.api-url}")
    private String firebaseApiUrl;

    @Value("${firebase.config-path}")
    private String firebaseConfigPath;

    @Value("${test.image-src}")
    private String testImageSrc;

    @Mock
    User user;

    @Mock
    Club club;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(firebaseUtil, "firebaseApiUrl", firebaseApiUrl);
        ReflectionTestUtils.setField(firebaseUtil, "firebaseConfigPath", firebaseConfigPath);
    }
}
