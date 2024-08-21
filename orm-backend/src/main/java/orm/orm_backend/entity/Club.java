package orm.orm_backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import orm.orm_backend.dto.request.ClubRequestDto;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Club extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn
    private User manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Mountain mountain;

    @Column(nullable = false)
    private String clubName;

    private String description;
    private String imageSrc;

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY)
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY)
    private List<Applicant> applicants;

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY)
    private List<Board> boards;


    @Builder
    public Club(User manager, Mountain mountain, String clubName, String description, String imageSrc) {
        this.mountain = mountain;
        this.manager = manager;
        this.clubName = clubName;
        this.description = description;
        this.imageSrc = imageSrc;
    }

    public Boolean isManager(Integer userId){
        Integer managerId = manager.getId();
        return managerId != null && managerId.equals(userId);
    }

    public void update(ClubRequestDto clubRequestDto, Mountain mountain, String imageSrc) {
        this.clubName = clubRequestDto.getClubName();
        this.description = clubRequestDto.getDescription();
        this.mountain = mountain;
        this.imageSrc = imageSrc == null ? this.imageSrc : imageSrc;
    }

    @PreRemove
    private void preRemove() {
        members.forEach(Member::removeClub);
        applicants.forEach(Applicant::removeClub);
        boards.forEach(Board::removeClub);
    }
}
