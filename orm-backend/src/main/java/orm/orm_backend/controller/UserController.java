package orm.orm_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import orm.orm_backend.dto.response.LoginResponseDto;
import orm.orm_backend.service.UserService;
import orm.orm_backend.util.JwtUtil;
import orm.orm_backend.util.KakaoUtil;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.app-key}")
    private String appKey;

    private final KakaoUtil kakaoUtil;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final String HEADER_AUTH = "Authorization";

    @GetMapping("/login/kakao")
    public ResponseEntity<?> tryKakaoLogin() {
        String url = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + appKey + "&redirect_uri=" + redirectUri;
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping("/login/kakao/auth")
    public ResponseEntity<LoginResponseDto> kakaoLogin(String code) throws JsonProcessingException {
        String kakaoTokens = kakaoUtil.getKakaoTokens(code);
        LoginResponseDto loginResponseDto = userService.kakaoLogin(kakaoTokens);
        HttpHeaders headers = jwtUtil.createTokenHeaders(loginResponseDto.getUserId());

        return ResponseEntity.ok()
                .headers(headers)
                .body(loginResponseDto);
    }

    @GetMapping("/login/auto")
    public ResponseEntity<LoginResponseDto> autoLogin(HttpServletRequest request) throws JsonProcessingException {
        String accessToken = request.getHeader(HEADER_AUTH);
        LoginResponseDto loginResponseDto = userService.autoLogin(accessToken);
        HttpHeaders headers = jwtUtil.createTokenHeaders(loginResponseDto.getUserId());
        return ResponseEntity.ok().headers(headers).body(loginResponseDto);
    }

    @PostMapping("/login/register-firebase")
    public ResponseEntity<Void> registerFirebase(HttpServletRequest request, @RequestBody String firebaseToken) {
        String accessToken = request.getHeader(HEADER_AUTH);
        Integer userIdFromAccessToken = jwtUtil.getUserIdFromAccessToken(accessToken);
        userService.registerFirebaseToken(firebaseToken, userIdFromAccessToken);
        return ResponseEntity.ok().build();
    }
}
