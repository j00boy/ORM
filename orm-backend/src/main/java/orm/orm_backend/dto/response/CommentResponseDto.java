package orm.orm_backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import orm.orm_backend.entity.Comment;
import orm.orm_backend.entity.User;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private String userNickname;
    private String content;
    private LocalDateTime createdAt;

    @Builder
    public CommentResponseDto(Comment comment) {
        User user = comment.getUser();
        this.userNickname = user.getNickname();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }
}
