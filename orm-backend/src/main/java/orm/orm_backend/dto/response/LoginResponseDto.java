package orm.orm_backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import orm.orm_backend.entity.User;

@Builder
@RequiredArgsConstructor
@Getter
public class LoginResponseDto {

    private final Integer userId;
    private final String imageSrc;
    private final String nickname;

    public LoginResponseDto(User user) {
        this.userId = user.getId();
        this.imageSrc = user.getImageSrc();
        this.nickname = user.getNickname();
    }
}
