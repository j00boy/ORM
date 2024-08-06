package orm.orm_backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import orm.orm_backend.dto.common.BoardImageDto;
import orm.orm_backend.dto.request.BoardRequestDto;
import orm.orm_backend.dto.response.BoardListResponseDto;
import orm.orm_backend.dto.response.BoardResponseDto;
import orm.orm_backend.dto.response.CommentResponseDto;
import orm.orm_backend.entity.*;
import orm.orm_backend.exception.CustomException;
import orm.orm_backend.exception.ErrorCode;
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
    private final MemberService memberService;

    /**
     * 게시글 작성
     * @param boardRequestDto
     * @param imgFiles
     * @param userId
     * @return
     */
    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto, List<MultipartFile> imgFiles, Integer userId) {
        // 클럼의 멤버인지 확인
        if(!memberService.isContained(userId, boardRequestDto.getClubId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

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

    /**
     * 게시글 조회
     * @param userId
     * @param clubId
     * @return
     */
    public List<BoardListResponseDto> getAllBoards(Integer userId, Integer clubId) {
        // 클럼의 멤버인지 확인
        if(!memberService.isContained(userId, clubId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        List<Board> allBoards = boardRepository.findByClubId(clubId);
        return allBoards.stream().map(BoardListResponseDto::new).toList();
    }

    /**
     * 게시글 상세 조회
     * @param boardId
     * @param userId
     */
    public BoardResponseDto getBoard(Integer boardId, Integer userId) {
        Board board = boardRepository.findById(boardId).orElseThrow();

        // 클럼의 멤버인지 확인
        if(!memberService.isContained(userId, board.getClub().getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        User user = userService.findUserById(userId);
        List<BoardImageDto> boardImages = boardImageService.getBoardImages(boardId);

        return new BoardResponseDto(board, user, boardImages);
    }

    /**
     * boardId에 해당하는 게시글을 삭제한다.
     * @param boardId
     * @return
     */
    @Transactional
    public void deleteBoard(Integer boardId, Integer userId) {
        Board board = boardRepository.findById(boardId).orElseThrow();

        if(board.isOwner(userId) && board.getClub().isManager(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        boardRepository.delete(board);
    }


    // TODO: Update 다시 한 번 체크 필요
    @Transactional
    public BoardResponseDto updateBoard(BoardRequestDto boardRequestDto, List<MultipartFile> imgFiles, Integer userId, Integer boardId) {
        // 해당 게시글 찾아오기
        Board board = boardRepository.findById(boardId).orElseThrow();

        // 로그인 유저 == 작성자일 경우에만 수정 가능
        if(!board.isOwner(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        // 기존 사진들을 '경로'에서 삭제 후, 'DB'에서 삭제
        boardImageService.deleteImages(boardId);

        // 엔티티 먼저 수정(DB 반영)
        board.update(boardRequestDto);

        // 사진 재업로드
        String imgSrc = null;

        String IMAGE_PATH = IMAGE_PATH_PREFIX + boardRequestDto.getClubId() + IMAGE_PATH_POSTFIX;

        List<BoardImageDto> boardImageDtos = new ArrayList<>();

        if (imgFiles != null && !imgFiles.isEmpty()) {
            for (MultipartFile imgFile : imgFiles) {
                imgSrc = imageUtil.saveImage(imgFile, IMAGE_PATH);
                boardImageDtos.add(BoardImageDto.builder().imgSrc(imgSrc).build());
            }
            boardImageService.saveImage(boardImageDtos, board);
        }

        return new BoardResponseDto(board, board.getUser(), boardImageDtos);
    }

}
