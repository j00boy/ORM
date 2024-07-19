package orm.orm_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import orm.orm_backend.dto.response.LoginResponseDto;
import orm.orm_backend.entity.User;
import orm.orm_backend.entity.UserStatus;
import orm.orm_backend.repository.UserRepository;
import orm.orm_backend.util.KakaoUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final KakaoUtil kakaoUtil;
    private final UserRepository userRepository;
    private final String ACCESS_TOKEN = "access_token";
    private final String REFRESH_TOKEN = "refresh_token";

    public Map<String, String> kakaoLogin(String kakaoTokens) throws JsonProcessingException {
        Map<String, String> result = new HashMap<>();
        String accessToken = kakaoUtil.extractToken(kakaoTokens, ACCESS_TOKEN);
        String refreshToken = kakaoUtil.extractToken(kakaoTokens, REFRESH_TOKEN);

        result.put(ACCESS_TOKEN, accessToken);
        result.put(REFRESH_TOKEN, refreshToken);

        LoginResponseDto userInfo = kakaoUtil.getKakaoUserInfo(accessToken);
        if (!isJoined(userInfo.getKakaoId())) {
            User joineUser = join(userInfo, accessToken, refreshToken);
        }
        return result;
    }

    private boolean isJoined(Long kakaoId) {
        // 데이터베이스에 없는 사용자 -> 가입한적 없는 사용자
        Optional<User> user = userRepository.findByKakaoId(kakaoId);
        if (user.isEmpty()) {
            return false;
        }
        return user.get().getIsActive() == UserStatus.Y;
    }

    private User join(LoginResponseDto userInfo, String kakaoAccessToken, String kakaoRefreshToken) {
        User user = User
                .builder()
                .kakaoId(userInfo.getKakaoId())
                .nickname(userInfo.getNickname())
                .imageSrc(userInfo.getImgSrc())
                .kakaoAccessToken(kakaoAccessToken)
                .kakaoRefreshToken(kakaoRefreshToken)
                .build();
        return userRepository.save(user);
    }
}
