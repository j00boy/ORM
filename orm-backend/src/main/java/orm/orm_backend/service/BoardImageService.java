package orm.orm_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import orm.orm_backend.dto.common.BoardImageDto;
import orm.orm_backend.entity.Board;
import orm.orm_backend.entity.BoardImage;
import orm.orm_backend.repository.BoardImageRepository;
import orm.orm_backend.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardImageService {
    private final ImageUtil imageUtil;
    private BoardImageRepository boardImageRepository;

    // 이미지 저장
    public void saveImage(List<BoardImageDto> boardImageDtos, Board board){
        for (BoardImageDto boardImage : boardImageDtos) {
            boardImageRepository.save(boardImage.toEntity(board, boardImage.getImgSrc()));
        }
    }

    // 이미지 삭제
    public void deleteImages(Integer boardId){
        List<BoardImage> preImages = boardImageRepository.findByBoardId(boardId);
        imageUtil.deleteImages(preImages.stream().map(BoardImage::getImageSrc).toList());
    }

}
