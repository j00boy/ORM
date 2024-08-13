package orm.orm_backend.dto.response;

import lombok.*;
import orm.orm_backend.entity.Club;

@Data
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
    private Long applicantCount;

    public ClubResponseDto(Club club) {
        this.id = club.getId();
        this.managerId = club.getManager().getId();
        this.managerName = club.getManager().getNickname();
        this.memberCount = club.getMembers().size();
        this.clubName = club.getClubName();
        this.description = club.getDescription();
        this.imgSrc = club.getImageSrc();
        this.mountainId = club.getMountain().getId();
        this.mountainName = club.getMountain().getMountainName();
        this.isMember = Boolean.TRUE;
        this.isApplied = Boolean.FALSE;
    }

    public ClubResponseDto(Club club, long applicantCount) {
        this(club);
        this.applicantCount = applicantCount;
    }

    public ClubResponseDto(Club club, Boolean isMember, Boolean isApplied) {
        this.id = club.getId();
        this.managerId = club.getManager().getId();
        this.managerName = club.getManager().getNickname();
        this.memberCount = club.getMembers().size();
        this.clubName = club.getClubName();
        this.description = club.getDescription();
        this.imgSrc = club.getImageSrc();
        this.mountainId = club.getMountain().getId();
        this.mountainName = club.getMountain().getMountainName();
        this.isMember = isMember;
        this.isApplied = isApplied;
    }
}
