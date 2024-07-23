package orm.orm_backend.dto.request;

import lombok.Builder;
import lombok.Data;
import orm.orm_backend.entity.Club;
import orm.orm_backend.entity.Member;
import orm.orm_backend.entity.User;

@Data
@Builder
public class MemberRequestDto {
    private Integer userId;
    private Integer clubId;

    public static Member toEntity(User user, Club club) {
        return Member.builder()
                .user(user)
                .club(club)
                .build();
    }
}
