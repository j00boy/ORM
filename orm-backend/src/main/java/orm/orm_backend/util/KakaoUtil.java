package orm.orm_backend.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import orm.orm_backend.dto.response.LoginResponseDto;
import orm.orm_backend.vo.KakaoInfoVo;

@Component
@Slf4j
public class KakaoUtil {

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.app-key}")
    private String appKey;

    private final String ACCESS_TOKEN = "access_token";
    private final String REFRESH_TOKEN = "refresh_token";

    public String extractToken(String kakaoResponse, String tokenType) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(kakaoResponse);
        return jsonNode.get(tokenType).asText();
    }

    public String getKakaoTokens(String code) {
        // HTTP header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        log.info("redirectURI={}", redirectUri);
        // HTTP body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", appKey);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        return response.getBody();
    }

    public KakaoInfoVo getKakaoUserInfo(String kakaoResponse) throws JsonProcessingException {
        String kakaoAccessToken = extractToken(kakaoResponse, ACCESS_TOKEN);
        String kakaoRefreshToken = extractToken(kakaoResponse, REFRESH_TOKEN);

        // Http Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoAccessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        // responseBody에 있는 정보 꺼내기
        String responseBody = response.getBody();
        return parseKakaoUserInfo(responseBody, kakaoAccessToken, kakaoRefreshToken);
    }

    private KakaoInfoVo parseKakaoUserInfo(String kakaoResponse, String accessToken, String refreshToken) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(kakaoResponse);
        JsonNode properties = jsonNode.get("properties");
        log.info("kakaoResponse={}", kakaoResponse);
        log.info("asText={}", properties.get("nickname").asText());
        log.info("toString={}", properties.get("nickname").toString());
        return KakaoInfoVo.builder()
                .kakaoId(jsonNode.get("id").asLong())
                .nickname(properties.get("nickname").asText())
                .imageSrc(properties.get("thumbnail_image").asText())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
