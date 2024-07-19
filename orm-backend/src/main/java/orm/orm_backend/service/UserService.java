package orm.orm_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import orm.orm_backend.dto.response.LoginResponseDto;
import orm.orm_backend.util.KakaoUtil;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final KakaoUtil kakaoUtil;
    private final String ACCESS_TOKEN = "access_token";
    private final String REFRESH_TOKEN = "refresh_token";

    public Map<String, String> kakaoLogin(String kakaoTokens) throws JsonProcessingException {
        Map<String, String> result = new HashMap<>();
        String accessToken = kakaoUtil.extractToken(kakaoTokens, ACCESS_TOKEN);
        String refreshToken = kakaoUtil.extractToken(kakaoTokens, REFRESH_TOKEN);

        result.put(ACCESS_TOKEN, accessToken);
        result.put(REFRESH_TOKEN, refreshToken);

        LoginResponseDto userInfo = kakaoUtil.getKakaoUserInfo(accessToken);

        return result;
    }
}
