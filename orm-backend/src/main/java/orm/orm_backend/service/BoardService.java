package orm.orm_backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import orm.orm_backend.dto.common.BoardImageDto;
import orm.orm_backend.dto.request.BoardRequestDto;
import orm.orm_backend.dto.response.BoardResponseDto;
import orm.orm_backend.dto.response.CommentResponseDto;
import orm.orm_backend.entity.*;
import orm.orm_backend.repository.BoardRepository;
import orm.orm_backend.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final String IMAGE_PATH_PREFIX = "club/board/";
    private final String IMAGE_PATH_POSTFIX = "/";


    private final ImageUtil imageUtil;

    private final UserService userService;
    private final ClubService clubService;
    private final CommentService commentService;
    private final BoardImageService boardImageService;

    private final BoardRepository boardRepository;

    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto, List<MultipartFile> imgFiles, Integer userId) {
        // user 찾기
        User user = userService.findUserById(userId);

        // club 찾기
        Club club = clubService.getByClubId(boardRequestDto.getClubId());

        // 사진 업로드
        String imgSrc = null;

        String IMAGE_PATH = IMAGE_PATH_PREFIX + boardRequestDto.getClubId() + IMAGE_PATH_POSTFIX;

        // board 생성
        Board board = boardRepository.save(boardRequestDto.toEntity(club, user, boardRequestDto));

        List<BoardImageDto> boardImageDtos = new ArrayList<>();

        if (imgFiles != null && !imgFiles.isEmpty()) {
            for (MultipartFile imgFile : imgFiles) {
                imgSrc = imageUtil.saveImage(imgFile, IMAGE_PATH);
                boardImageDtos.add(BoardImageDto.builder().imgSrc(imgSrc).build());
            }
            boardImageService.saveImage(boardImageDtos, board);
        }

        return new BoardResponseDto(board, user, boardImageDtos);
    }
}
