package orm.orm_backend.dto.request;

import lombok.Builder;
import lombok.Data;
import orm.orm_backend.entity.Club;
import orm.orm_backend.entity.Member;
import orm.orm_backend.entity.User;

@Data
@Builder
public class MemberRequestDto {
    private User user;
    private Club club;

    public Member toEntity() {
        return Member.builder()
                .user(user)
                .club(club)
                .build();
    }
}
