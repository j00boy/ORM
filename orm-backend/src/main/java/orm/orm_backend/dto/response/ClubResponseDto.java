package orm.orm_backend.dto.response;

import lombok.*;
import orm.orm_backend.entity.Club;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubResponseDto {
    private Integer id;
    private Integer managerId;
    private String managerName;
    private Integer memberCount;

    private String clubName;
    private String description;
    private String imgSrc;

    private Integer mountainId;
    private String mountainName;

    private Boolean isMember;
    private Boolean isApplied;

    public ClubResponseDto toDto(Club club, Boolean isMember, Boolean isApplied) {
        return ClubResponseDto.builder()
                .id(club.getId())
                .managerId(club.getManager().getId())
                .managerName(club.getManager().getNickname())
                .memberCount(club.getMembers().size())
                .clubName(club.getClubName())
                .description(club.getDescription())
                .imgSrc(club.getImageSrc())
                .mountainId(club.getMountain().getId())
                .mountainName(club.getMountain().getMountainName())
                .isMember(isMember)
                .isApplied(isApplied)
                .build();
    }
}
