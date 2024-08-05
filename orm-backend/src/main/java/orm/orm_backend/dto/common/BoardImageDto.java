package orm.orm_backend.dto.common;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import orm.orm_backend.entity.Board;
import orm.orm_backend.entity.BoardImage;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardImageDto {
    private Integer boardId;
    private String imgSrc;

    public BoardImage toEntity(Board board, String imgSrc) {
        return BoardImage.builder()
                .board(board)
                .imageSrc(imgSrc)
                .build();
    }

    @Builder
    public BoardImageDto(String imgSrc) {
        this.imgSrc = imgSrc;
    }
}
