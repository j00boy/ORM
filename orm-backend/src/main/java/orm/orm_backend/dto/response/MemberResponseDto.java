package orm.orm_backend.dto.response;

import lombok.Builder;
import lombok.Data;
import orm.orm_backend.entity.Member;

import java.time.LocalDateTime;

@Data
@Builder
public class MemberResponseDto {
    private Integer userId;
    private Integer clubId;
    private LocalDateTime joinedAt;
    private String imgSrc;
    private String nickname;

    public static MemberResponseDto toDto(Member member) {
        return MemberResponseDto.builder()
                .userId(member.getUser().getId())
                .clubId(member.getClub().getId())
                .joinedAt(member.getCreatedAt())
                .imgSrc(member.getUser().getImageSrc())
                .nickname(member.getUser().getNickname())
                .build();
    }
}
