package orm.orm_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import orm.orm_backend.entity.User;
import orm.orm_backend.service.UserService;
import orm.orm_backend.util.JwtUtil;
import orm.orm_backend.util.KakaoUtil;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.app-key}")
    private String appKey;

    private final KakaoUtil kakaoUtil;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @GetMapping("/login/kakao")
    public ResponseEntity<?> tryKakaoLogin() {
        String url = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + appKey + "&redirect_uri=" + redirectUri;
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping("/login/kakao/auth")
    public ResponseEntity<User> kakaoLogin(String code) throws JsonProcessingException {
        String kakaoTokens = kakaoUtil.getKakaoTokens(code);
        User user = userService.kakaoLogin(kakaoTokens);
        HttpHeaders headers = new HttpHeaders();
        headers.add("accessToken", jwtUtil.createAccessToken(user.getId()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(user);
    }
}
