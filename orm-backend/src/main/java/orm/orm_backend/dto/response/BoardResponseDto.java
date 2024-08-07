package orm.orm_backend.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import orm.orm_backend.dto.common.BoardImageDto;
import orm.orm_backend.entity.Board;
import orm.orm_backend.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardResponseDto {
    private Integer boardId;
    private String title;
    private String content;
    private Integer userId;
    private String userNickname;
    private Integer commentCount;
    private List<CommentResponseDto> comments;
    private Integer hit;
    private LocalDateTime createdAt;
    private List<BoardImageDto> imgSrc;

    @Builder
    public BoardResponseDto(Board board, User user, List<BoardImageDto> imgSrc, List<CommentResponseDto> comments) {
        this.boardId = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.userId = user.getId();
        this.userNickname = user.getNickname();
        this.comments = comments;
        this.hit = board.getHit();
        this.createdAt = board.getCreatedAt();
        this.imgSrc = imgSrc;
    }
}
