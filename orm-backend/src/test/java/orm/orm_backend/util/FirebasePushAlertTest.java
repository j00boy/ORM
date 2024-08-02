package orm.orm_backend.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import orm.orm_backend.dto.fcmalert.*;

import java.io.IOException;

@TestPropertySource(locations = "classpath:test-application.properties")
@ExtendWith(SpringExtension.class)
@Slf4j
public class FirebasePushAlertTest {

    @Value("${firebase.test-token}")
    private String testFirebaseToken;

    @Value("${firebase.api-url}")
    private String firebaseApiUrl;

    @Value("${firebase.config-path}")
    private String firebaseConfigPath;

    @Value("${test.image-src}")
    private String testImageSrc;

    private FirebaseUtil firebaseUtil = new FirebaseUtil();

    private FcmNotification notification;
    private FcmAlertData fcmAlertData;
    private String notificationBody;

    private Integer clubId = 1;
    private String clubName = "백승헌 클럽333";
    private Integer userId = 1;
    private String userName = "백승헌222";

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(firebaseUtil, "firebaseApiUrl", firebaseApiUrl);
        ReflectionTestUtils.setField(firebaseUtil, "firebaseConfigPath", firebaseConfigPath);
    }

    @Test
    void clubApplicationPushAlertTest() throws IOException {
        fcmAlertData = FcmClubApplicationDto.builder()
                .clubId(String.valueOf(clubId))
                .clubName(clubName)
                .clubImageSrc(testImageSrc)
                .userId(String.valueOf(userId))
                .userName(userName)
                .build();
        notificationBody = fcmAlertData.getMessage();
        notification = FcmNotification.builder()
                .body(notificationBody)
                .build();
        firebaseUtil.pushAlert(fcmAlertData, testFirebaseToken, notification);
    }

    @Test
    void clubAcceptancePushAlertTest() {
        fcmAlertData = FcmAcceptanceDto.builder()
                .clubId(String.valueOf(clubId))
                .clubName(clubName)
                .clubImageSrc(testImageSrc)
                .isAccepted(true)
                .build();
        notificationBody = fcmAlertData.getMessage();
        notification = FcmNotification.builder()
                .body(notificationBody)
                .build();
        firebaseUtil.pushAlert(fcmAlertData, testFirebaseToken, notification);
    }

    @Test
    void clubRejectPushAlertTest() {
        fcmAlertData = FcmAcceptanceDto.builder()
                .clubId(String.valueOf(clubId))
                .clubName(clubName)
                .clubImageSrc(testImageSrc)
                .isAccepted(false)
                .build();
        notificationBody = fcmAlertData.getMessage();
        notification = FcmNotification.builder()
                .body(notificationBody)
                .build();
        firebaseUtil.pushAlert(fcmAlertData, testFirebaseToken, notification);
    }

    @Test
    void clubExpelPushAlertTest() {
        fcmAlertData = FcmExpelDto.builder()
                .clubId(String.valueOf(clubId))
                .clubName(clubName)
                .clubImageSrc(testImageSrc)
                .build();
        notificationBody = fcmAlertData.getMessage();
        notification = FcmNotification.builder()
                .body(notificationBody)
                .build();
        firebaseUtil.pushAlert(fcmAlertData, testFirebaseToken, notification);
    }
}
