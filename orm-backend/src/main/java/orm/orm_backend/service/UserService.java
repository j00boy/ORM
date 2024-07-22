package orm.orm_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import orm.orm_backend.dto.response.LoginResponseDto;
import orm.orm_backend.entity.User;
import orm.orm_backend.entity.UserStatus;
import orm.orm_backend.exception.UnAuthorizedException;
import orm.orm_backend.exception.UserWithdrawalException;
import orm.orm_backend.repository.UserRepository;
import orm.orm_backend.util.JwtUtil;
import orm.orm_backend.util.KakaoUtil;
import orm.orm_backend.vo.KakaoInfoVo;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final KakaoUtil kakaoUtil;
    private final JwtUtil jwtUtil;
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

    @Transactional
    public LoginResponseDto autoLogin(String accessToken) throws JsonProcessingException {
        Integer userId = jwtUtil.getUserIdFromAccessToken(accessToken);
        Optional<User> userById = userRepository.findById(userId);
        if (userById.isEmpty()) {
            throw new UnAuthorizedException();
        }

        User user = userById.get();
        kakaoUtil.refreshAccessToken(user.getKakaoRefreshToken(), user); // 추후 리프레시 토큰 만료시 로그아웃 처리 로직 추가

        return user.toLoginResponseDto();
    }

    public User findUserById(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent() && user.get().isActiveMember()) {
            return user.get();
        }
        throw new UserWithdrawalException();
    }

    private boolean isJoined(Long kakaoId) {
        // 데이터베이스에 없는 사용자 -> 가입한적 없는 사용자
        Optional<User> user = userRepository.findByKakaoId(kakaoId);
        if (user.isEmpty()) {
            return false;
        }
        return user.get().isActiveMember();
    }

    private User join(KakaoInfoVo kakaoInfo) {
        User user = new User(kakaoInfo);
        return userRepository.save(user);
    }
}
