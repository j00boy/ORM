package orm.orm_backend.dto.request;

import lombok.*;
import orm.orm_backend.entity.Board;
import orm.orm_backend.entity.Club;
import orm.orm_backend.entity.User;

import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
public class BoardRequestDto {
    private Integer clubId;
    private String title;
    private String content;

    private Set<String> imgSrc;

    public Board toEntity(Club club, User user, BoardRequestDto boardRequestDto) {
        return Board.builder()
                .club(club)
                .user(user)
                .title(boardRequestDto.getTitle())
                .content(boardRequestDto.getContent())
                .build();
    }
}
