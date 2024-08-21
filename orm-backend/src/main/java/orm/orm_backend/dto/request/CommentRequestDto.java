package orm.orm_backend.dto.request;

import lombok.Getter;
import orm.orm_backend.entity.Board;
import orm.orm_backend.entity.Comment;
import orm.orm_backend.entity.User;

@Getter
public class CommentRequestDto {
    private String content;

    public Comment toEntity(Board board, User user, CommentRequestDto commentRequestDto) {
        return Comment.builder()
                .board(board)
                .user(user)
                .content(commentRequestDto.getContent())
                .build();
    }
}
