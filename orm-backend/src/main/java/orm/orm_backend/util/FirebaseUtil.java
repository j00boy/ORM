package orm.orm_backend.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import orm.orm_backend.dto.fcmalert.FcmAlertData;
import orm.orm_backend.dto.fcmalert.FcmAlertDto;
import orm.orm_backend.dto.fcmalert.FcmMessageDto;
import orm.orm_backend.dto.fcmalert.FcmNotification;
import orm.orm_backend.exception.CustomException;
import orm.orm_backend.exception.ErrorCode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class FirebaseUtil {

    @Value("${firebase.api-url}")
    private String firebaseApiUrl;

    @Value("${firebase.config-path}")
    private String firebaseConfigPath;

    public void pushAlert(FcmAlertData fcmAlertData, String receiverFcmToken, FcmNotification fcmNotification) {
        try {
            String message = createMessage(fcmAlertData, receiverFcmToken, fcmNotification);
            RestTemplate restTemplate = new RestTemplate();

            // 한글 깨짐 증상에 대한 수정
            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + getAccessToken());

            HttpEntity<String> entity = new HttpEntity<>(message, headers);
            try {
                ResponseEntity<String> response = restTemplate.exchange(firebaseApiUrl, HttpMethod.POST, entity, String.class);
            } catch (HttpClientErrorException e) {
            }
        } catch (IOException e) {
            throw new CustomException(ErrorCode.PUSH_ALERT_FAIL_ERROR);
        }

    }

    private String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    private String createMessage(FcmAlertData fcmAlertData, String receiverFcmToken, FcmNotification notification)
            throws JsonProcessingException {
        FcmMessageDto fcmMessageDto = FcmMessageDto.builder()
                .token(receiverFcmToken)
                .data(fcmAlertData)
                .notification(notification)
                .build();

        FcmAlertDto alertDto = FcmAlertDto.builder()
                .message(fcmMessageDto)
                .build();

        return new ObjectMapper().writeValueAsString(alertDto);
    }
}
