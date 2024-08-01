package orm.orm_backend.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import orm.orm_backend.entity.Club;
import orm.orm_backend.entity.Member;
import orm.orm_backend.entity.User;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRequestDto {
    private Integer userId;
    private Integer clubId;
    private Boolean isApproved;

    @Builder
    public MemberRequestDto(User user, Club club) {
        this.userId = user.getId();
        this.clubId = club.getId();
    }

    public Member toEntity(User user, Club club) {
        return Member.builder()
                .user(user)
                .club(club)
                .build();
    }


}
