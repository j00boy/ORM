package orm.orm_backend.learningtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FirebaseTest {

    FcmSendDto fcmSendDto;

    @BeforeEach
    void setUp() {
        fcmSendDto = FcmSendDto.builder()
                .token("fvDschy1ROeID7oXqGpyPY:APA91bF7J-XMHYED40TWl2uY7dQPC9a7ewB0faSE2TiGawVyCBYoBL11WMC2u0kHYn7LTSJJBL3rPDiVsG86AQrYTB6xFkmlpj0kVEBNzxIxd-5PZN41sP8A_-XnXFwPpWE-uorbtv6u")
                .title("test title")
                .body("test content")
                .build();
    }

    @Test
    void pushAlertTest() throws IOException {
        String message = makeMessage(fcmSendDto);
        RestTemplate restTemplate = new RestTemplate();

        // 한글 깨짐 증상에 대한 수정
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getAccessToken());

        HttpEntity<String> entity = new HttpEntity<>(message, headers);

        String API_URL = "https://fcm.googleapis.com/v1/projects/orm-ssafy/messages:send";
        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

        System.out.println(response.getStatusCode());
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "orm-ssafy-firebase-adminsdk-r5bcu-b5d1ad1ab6.json";

        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    /**
     * FCM 전송 정보를 기반으로 메시지를 구성합니다. (Object -> String)
     *
     * @param fcmSendDto FcmSendDto
     * @return String
     */
    private String makeMessage(FcmSendDto fcmSendDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        FcmMessageDto.Notification notification = FcmMessageDto.Notification.builder()
                .title("test notification title")
                .body("test notification body")
                .image("test notification image url")
                .build();
        FcmMessageDto.Data data = FcmMessageDto.Data.builder()
                .name("test name")
                .address("test address")
                .build();
        FcmMessageDto.Message message = FcmMessageDto.Message.builder()
                .token(fcmSendDto.token)
                .notification(notification)
                .data(data)
                .build();
        FcmMessageDto fcmMessageDto = FcmMessageDto.builder()
                .message(message)
                .validateOnly(false)
                .build();

        return objectMapper.writeValueAsString(fcmMessageDto);
    }

    /**
     * 모바일에서 전달받은 객체
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class FcmSendDto {
        private String token;

        private String title;

        private String body;

        @Builder(toBuilder = true)
        public FcmSendDto(String token, String title, String body) {
            this.token = token;
            this.title = title;
            this.body = body;
        }
    }

    /**
    * FCM 전송 Format DTO
    */
    @Getter
    @Builder
    static class FcmMessageDto {
        private boolean validateOnly;
        private FcmMessageDto.Message message;

        @Builder
        @AllArgsConstructor
        @Getter
        public static class Message {
            private FcmMessageDto.Notification notification;
            private String token;
            private Data data;
        }

        @Builder
        @AllArgsConstructor
        @Getter
        public static class Notification {
            private String title;
            private String body;
            private String image;
        }

        @Builder
        @AllArgsConstructor
        @Getter
        public static class Data {
            private String name;
            private String address;
        }
    }
}
