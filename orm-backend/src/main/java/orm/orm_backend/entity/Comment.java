package orm.orm_backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import orm.orm_backend.dto.request.CommentRequestDto;
import orm.orm_backend.dto.response.CommentResponseDto;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(length = 100)
    private String content;

    @Builder
    public Comment(Board board, User user, String content) {
        this.board = board;
        this.user = user;
        this.content = content;
    }

    public boolean isOwnedByUser(Integer userId) {
        return this.getUser() != null && this.getUser().getId().equals(userId);
    }

    public boolean isBoardIdMatching(Integer boardId) {
        return this.getBoard() != null && this.getBoard().getId().equals(boardId);
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }

}
