package orm.orm_backend.dto.response;

import lombok.*;
import orm.orm_backend.entity.Club;

@Getter
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

    public static ClubResponseDto toMyDto(Club club) {
        ClubResponseDto clubResponseDto = new ClubResponseDto();
        clubResponseDto.id = club.getId();
        clubResponseDto.managerId = club.getManager().getId();
        clubResponseDto.managerName = club.getManager().getNickname();
        clubResponseDto.memberCount = club.getMembers().size();
        clubResponseDto.clubName = club.getClubName();
        clubResponseDto.description = club.getDescription();
        clubResponseDto.imgSrc = club.getImageSrc();
        clubResponseDto.mountainId = club.getMountain().getId();
        clubResponseDto.mountainName = club.getMountain().getMountainName();
        clubResponseDto.isMember = Boolean.TRUE;
        clubResponseDto.isApplied = Boolean.FALSE;
        return clubResponseDto;
    }

    public static ClubResponseDto toDto(Club club, Boolean isMember, Boolean isApplied) {
        ClubResponseDto clubResponseDto = new ClubResponseDto();
        clubResponseDto.id = club.getId();
        clubResponseDto.managerId = club.getManager().getId();
        clubResponseDto.managerName = club.getManager().getNickname();
        clubResponseDto.memberCount = club.getMembers().size();
        clubResponseDto.clubName = club.getClubName();
        clubResponseDto.description = club.getDescription();
        clubResponseDto.imgSrc = club.getImageSrc();
        clubResponseDto.mountainId = club.getMountain().getId();
        clubResponseDto.mountainName = club.getMountain().getMountainName();
        clubResponseDto.isMember = isMember;
        clubResponseDto.isApplied = isApplied;
        return clubResponseDto;
    }
}
