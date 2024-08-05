package orm.orm_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import orm.orm_backend.dto.common.BoardImageDto;
import orm.orm_backend.entity.Board;
import orm.orm_backend.entity.BoardImage;
import orm.orm_backend.repository.BoardImageRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardImageService {
    private BoardImageRepository boardImageRepository;

    public void saveImage(List<BoardImageDto> boardImageDtos, Board board){
        for (BoardImageDto boardImage : boardImageDtos) {
            boardImageRepository.save(boardImage.toEntity(board, boardImage.getImgSrc()));
        }
    }
}
