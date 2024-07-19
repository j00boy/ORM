package orm.orm_backend.dto.response;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class LoginResponseDto {

    private final String accessToken;
    private final String imgSrc;
    private final String nickname;
}
