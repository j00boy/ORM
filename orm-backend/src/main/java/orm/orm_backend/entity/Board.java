package orm.orm_backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import orm.orm_backend.dto.request.BoardRequestDto;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn
    private Club club;

    @ManyToOne(fetch = LAZY)
    @JoinColumn
    private User user;

    @Column(length = 30)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardImage> boardImages = new ArrayList<>();

    private Integer hit;

    @Builder
    public Board(User user, Club club, String title, String content){
        this.club = club;
        this.user = user;
        this.title = title;
        this.content = content;
        this.hit = 0;
    }

    public boolean isOwner(Integer userId) {
        Integer ownerId = user.getId();
        return ownerId != null && userId.equals(ownerId);
    }

    public boolean hasRight(Integer userId) {
        boolean isManager = club.isManager(userId);
        return isOwner(userId) || isManager;
    }

    public void update(BoardRequestDto boardRequestDto) {
        this.title = boardRequestDto.getTitle();
        this.content = boardRequestDto.getContent();
    }
}
