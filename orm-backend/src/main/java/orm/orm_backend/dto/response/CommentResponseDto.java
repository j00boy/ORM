package orm.orm_backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import orm.orm_backend.entity.Comment;
import orm.orm_backend.entity.User;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Integer commentId;
    private Integer userId;
    private String userNickname;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    @Builder
    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        User user = comment.getUser();
        this.userId = user.getId();
        this.userNickname = user.getNickname();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.lastModifiedAt = comment.getLastModifiedAt();
    }
}
