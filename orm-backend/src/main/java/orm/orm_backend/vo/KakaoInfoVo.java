package orm.orm_backend.vo;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class KakaoInfoVo {
    private Long kakaoId;
    private String accessToken;
    private String refreshToken;
    private String imageSrc;
    private String nickname;
}
