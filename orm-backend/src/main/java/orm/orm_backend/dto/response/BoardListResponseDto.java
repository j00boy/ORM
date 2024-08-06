package orm.orm_backend.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import orm.orm_backend.entity.Board;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardListResponseDto {
    private Integer boardId;
    private String title;
    private Integer userId;
    private String userNickname;
    private Integer commentCount;
    private Integer hit;
    private LocalDateTime createdAt;

    @Builder
    public BoardListResponseDto(Board board) {
        this.boardId = board.getId();
        this.title = board.getTitle();
        this.userId = board.getUser().getId();
        this.userNickname = board.getUser().getNickname();
        this.commentCount = board.getComments().size();
        this.hit = board.getHit();
        this.createdAt = board.getCreatedAt();
    }
}
