package orm.orm_backend.dto.response;

import lombok.Builder;
import lombok.Data;
import orm.orm_backend.entity.Member;

import java.time.LocalDateTime;

@Data
public class MemberResponseDto {
    private Integer userId;
    private Integer clubId;
    private LocalDateTime joinedAt;
    private String imgSrc;
    private String nickname;

    @Builder
    public MemberResponseDto (Member member) {
        this.userId = member.getUser().getId();
        this.clubId = member.getClub().getId();
        this.joinedAt = member.getCreatedAt();
        this.imgSrc = member.getUser().getImageSrc();
        this.nickname = member.getUser().getNickname();
    }
}
