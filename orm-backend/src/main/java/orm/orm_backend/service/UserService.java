package orm.orm_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import orm.orm_backend.dto.response.LoginResponseDto;
import orm.orm_backend.entity.User;
import orm.orm_backend.entity.UserStatus;
import orm.orm_backend.repository.UserRepository;
import orm.orm_backend.util.KakaoUtil;
import orm.orm_backend.vo.KakaoInfoVo;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final KakaoUtil kakaoUtil;
    private final UserRepository userRepository;


    public LoginResponseDto kakaoLogin(String kakaoTokens) throws JsonProcessingException {
        KakaoInfoVo kakaoInfo = kakaoUtil.getKakaoUserInfo(kakaoTokens);

        if (!isJoined(kakaoInfo.getKakaoId())) {
            join(kakaoInfo);
        }

        // 가입 절차를 밟았기 때문에 nullPointerException이 발생하지 않음이 보장됨
        User user = userRepository.findByKakaoId(kakaoInfo.getKakaoId()).get();
        return user.toLoginResponseDto();
    }

    private boolean isJoined(Long kakaoId) {
        // 데이터베이스에 없는 사용자 -> 가입한적 없는 사용자
        Optional<User> user = userRepository.findByKakaoId(kakaoId);
        if (user.isEmpty()) {
            return false;
        }
        return user.get().getIsActive() == UserStatus.Y;
    }

    private User join(KakaoInfoVo kakaoInfo) {
        User user = new User(kakaoInfo);
        return userRepository.save(user);
    }
}
