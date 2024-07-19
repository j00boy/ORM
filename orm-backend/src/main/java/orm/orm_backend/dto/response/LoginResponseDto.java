package orm.orm_backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@Getter
public class LoginResponseDto {

    private final Integer userId;
    private final Long kakaoId;
    private final String accessToken;
    private final String imageSrc;
    private final String nickname;
}
