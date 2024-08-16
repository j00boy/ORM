package orm.orm_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import orm.orm_backend.dto.response.LoginResponseDto;
import orm.orm_backend.entity.User;
import orm.orm_backend.exception.CustomException;
import orm.orm_backend.exception.ErrorCode;
import orm.orm_backend.exception.UnAuthorizedException;
import orm.orm_backend.exception.UserWithdrawalException;
import orm.orm_backend.repository.UserRepository;
import orm.orm_backend.util.KakaoUtil;
import orm.orm_backend.vo.KakaoInfoVo;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final KakaoUtil kakaoUtil;

    private final UserRepository userRepository;

    @Transactional
    public LoginResponseDto kakaoLogin(String kakaoTokens) throws JsonProcessingException {
        KakaoInfoVo kakaoInfo = kakaoUtil.getKakaoUserInfo(kakaoTokens);

        if (!isJoined(kakaoInfo.getKakaoId())) {
            join(kakaoInfo);
        }

        // 가입 절차를 밟았기 때문에 nullPointerException이 발생하지 않음이 보장됨
        User user = userRepository.findByKakaoId(kakaoInfo.getKakaoId()).orElseThrow();
        // 재 가입하는 사람을 위해 refreshToken update
        user.refreshKakaoTokens(kakaoInfo.getAccessToken(), kakaoInfo.getRefreshToken());
        user.join();
        return new LoginResponseDto(user);
    }

    @Transactional
    public LoginResponseDto autoLogin(Integer userId) throws JsonProcessingException {
        User user = userRepository.findById(userId).orElseThrow();
        kakaoUtil.refreshAccessToken(user.getKakaoRefreshToken(), user); // 추후 리프레시 토큰 만료시 로그아웃 처리 로직 추가

        return new LoginResponseDto(user);
    }

    public User findUserById(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));
        if (user.isActiveMember()) {
            return user;
        }
        throw new CustomException(ErrorCode.WITHDRAWN_USER_ID);
    }

    @Transactional
    public void registerFirebaseToken(String firebaseToken, Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));
        if (!user.isActiveMember()) {
            throw new CustomException(ErrorCode.WITHDRAWN_USER_ID);
        }
        user.registerFirebaseToken(firebaseToken);
    }

    @Transactional
    public void leaveUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));
        if (!user.isActiveMember()) {
            throw new CustomException(ErrorCode.WITHDRAWN_USER_ID);
        }
        kakaoUtil.disconnectMembership(user.getKakaoAccessToken());
        user.leave();
    }

    private boolean isJoined(Long kakaoId) {
        return userRepository.existsByKakaoId(kakaoId);
    }

    private User join(KakaoInfoVo kakaoInfo) {
        User user = new User(kakaoInfo);
        return userRepository.save(user);
    }
}
