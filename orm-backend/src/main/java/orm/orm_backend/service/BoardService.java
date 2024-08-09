package orm.orm_backend.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import orm.orm_backend.dto.common.BoardImageDto;
import orm.orm_backend.dto.request.BoardRequestDto;
import orm.orm_backend.dto.response.BoardSimpleResponseDto;
import orm.orm_backend.dto.response.BoardResponseDto;
import orm.orm_backend.dto.response.CommentResponseDto;
import orm.orm_backend.entity.*;
import orm.orm_backend.exception.CustomException;
import orm.orm_backend.exception.ErrorCode;
import orm.orm_backend.repository.BoardRepository;
import orm.orm_backend.util.CookieUtil;
import orm.orm_backend.util.ImageUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final String IMAGE_PATH_PREFIX = "club/board/";
    private final String IMAGE_PATH_POSTFIX = "/";

    private final String COOKIE_PREFIX = "board";
    private final String COOKIE_PATH = "/clubs/boards";

    private final ImageUtil imageUtil;

    private final UserService userService;
    private final ClubService clubService;
    private final BoardImageService boardImageService;

    private final BoardRepository boardRepository;
    private final MemberService memberService;
    private final FirebasePushAlertService firebasePushAlertService;

    /**
     * 게시글 작성
     * @param boardRequestDto
     * @param imgFiles
     * @param userId
     * @return
     */
    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto, List<MultipartFile> imgFiles, Integer userId) {
        // 클럽의 멤버인지 확인
        if(!memberService.isContained(userId, boardRequestDto.getClubId())) {
            throw new CustomException(ErrorCode.BOARD_FORBIDDEN);
        }

        // user 찾기
        User user = userService.findUserById(userId);

        // club 찾기
        Club club = clubService.getByClubId(boardRequestDto.getClubId());

        // 사진 업로드
        String imgSrc = null;

        String IMAGE_PATH = IMAGE_PATH_PREFIX + boardRequestDto.getClubId() + IMAGE_PATH_POSTFIX;

        // board 생성
        Board board = boardRepository.saveAndFlush(boardRequestDto.toEntity(club, user, boardRequestDto));

        List<BoardImageDto> boardImageDtos = new ArrayList<>();

        if (imgFiles != null && !imgFiles.isEmpty()) {
            for (MultipartFile imgFile : imgFiles) {
                imgSrc = imageUtil.saveImage(imgFile, IMAGE_PATH);
                boardImageDtos.add(BoardImageDto.builder().imgSrc(imgSrc).build());
            }
            boardImageService.saveImage(boardImageDtos, board);
        }

        // 클럽 멤버의 FirebaseToken 확인
        List<String> clubMembertokens = club.getMembers().stream().map(member -> member.getUser().getFirebaseToken())
                .filter(firebaseToken -> firebaseToken != null && !firebaseToken.isBlank() && !firebaseToken.equals(user.getFirebaseToken()))
                .toList();


        // 푸쉬알람
        firebasePushAlertService.pushNewBoardAlert(clubMembertokens, board);

        return new BoardResponseDto(board, user, boardImageDtos, new ArrayList<CommentResponseDto>());
    }

    /**
     * 게시글 조회
     * @param userId
     * @param clubId
     * @return
     */
    public List<BoardSimpleResponseDto> getAllBoards(Integer userId, Integer clubId) {
        // 클럽의 멤버인지 확인
        if(!memberService.isContained(userId, clubId)) {
            throw new CustomException(ErrorCode.BOARD_FORBIDDEN);
        }

        List<Board> allBoards = boardRepository.findByClubId(clubId);
        return allBoards.stream().map(BoardSimpleResponseDto::new).toList();
    }

    /**
     * 게시글 상세 조회
     * @param boardId
     * @param userId
     */
    @Transactional
    public BoardResponseDto getBoard(Integer boardId, Integer userId, Cookie[] cookies, HttpServletResponse response) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        // 클럼의 멤버인지 확인
        if(!memberService.isContained(userId, board.getClub().getId())) {
            throw new CustomException(ErrorCode.BOARD_FORBIDDEN);
        }

        // 조회수 중복처리를 위한 쿠키 검증 및 설정
        if(CookieUtil.checkCookie(COOKIE_PREFIX, boardId, cookies)) {
            incrementHits(boardId);
            CookieUtil.setCookie(COOKIE_PREFIX, COOKIE_PATH, response, boardId);
        }

        User user = userService.findUserById(userId);
        List<BoardImageDto> boardImages = boardImageService.getBoardImages(boardId);
        return new BoardResponseDto(board, user, boardImages, board.getComments().stream().map(CommentResponseDto::new).toList());
    }

    /**
     * boardId에 해당하는 게시글을 삭제한다.
     * @param boardId
     * @return
     */
    @Transactional
    public void deleteBoard(Integer boardId, Integer userId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        if(!board.hasRight(userId)) {
            throw new CustomException(ErrorCode.BOARD_FORBIDDEN);
        }

        // 1. 실제 경로, 2. DB 순서대로 사진을 지움
        boardImageService.deleteImages(boardId);

        boardRepository.delete(board);
    }

    @Transactional
    public void incrementHits(Integer boardId) {
        boardRepository.updateHits(boardId);
    }

    // Board Entity 조회하는 메서드
    public Board getBoardById(Integer boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
    }

    @Transactional
    public BoardResponseDto updateBoard(BoardRequestDto boardRequestDto, List<MultipartFile> imgFiles, Integer userId, Integer boardId) {
        // 해당 게시글 찾아오기
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        // 로그인 유저 == 작성자일 경우에만 수정 가능
        if(!board.isOwner(userId)) {
            throw new CustomException(ErrorCode.BOARD_FORBIDDEN);
        }

        // 원본에 있던 파일들
        List<String> orgImgSrcs = boardImageService.getBoardImages(boardId).stream().map(BoardImageDto::getImgSrc).toList();
        // 수정했을 때 최종적으로 저장되는 파일들
        Set<String> newImgSrcs = boardRequestDto.getImgSrc() != null ? new HashSet<>(boardRequestDto.getImgSrc()) : new HashSet<>();


        // 새로 들어온 파일을 먼저 저장
        String imgSrc = null;

        String IMAGE_PATH = IMAGE_PATH_PREFIX + boardRequestDto.getClubId() + IMAGE_PATH_POSTFIX;

        List<BoardImageDto> boardImageDtos = new ArrayList<>();

        if (imgFiles != null && !imgFiles.isEmpty()) {
            for (MultipartFile imgFile : imgFiles) {
                imgSrc = imageUtil.saveImage(imgFile, IMAGE_PATH);
                boardImageDtos.add(BoardImageDto.builder().imgSrc(imgSrc).build());
                newImgSrcs.add(imgSrc); // 새로 들어온 String 리스트에 저장
            }
            boardImageService.saveImage(boardImageDtos, board);
        }

        // 원본 파일들 중에, 새로 저장되는 파일에 이름이 없다면 1. 경로 2. DB에서 모두 사진 삭제
        for (String orgImg : orgImgSrcs) {
            if (!newImgSrcs.contains(orgImg)) {
                boardImageService.deleteImage(orgImg);
            }
        }

        // 엔티티 수정(DB 반영)
        board.update(boardRequestDto);

        List<BoardImageDto> boardImages = boardImageService.getBoardImages(boardId);

        return new BoardResponseDto(board, board.getUser(), boardImages,
                board.getComments().stream().map(CommentResponseDto::new).toList());
    }

    //    @Transactional
//    public BoardResponseDto updateBoard(BoardRequestDto boardRequestDto, List<MultipartFile> imgFiles, Integer userId, Integer boardId) {
//        // 해당 게시글 찾아오기
//        Board board = boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
//
//        // 로그인 유저 == 작성자일 경우에만 수정 가능
//        if(!board.isOwner(userId)) {
//            throw new CustomException(ErrorCode.BOARD_FORBIDDEN);
//        }
//
//        // 기존 사진들을 '경로'에서 삭제 후, 'DB'에서 삭제
//        boardImageService.deleteImages(boardId);
//
//        // 엔티티 먼저 수정(DB 반영)
//        board.update(boardRequestDto);
//
//        // 사진 재업로드
//        String imgSrc = null;
//
//        String IMAGE_PATH = IMAGE_PATH_PREFIX + boardRequestDto.getClubId() + IMAGE_PATH_POSTFIX;
//
//        List<BoardImageDto> boardImageDtos = new ArrayList<>();
//
//        if (imgFiles != null && !imgFiles.isEmpty()) {
//            for (MultipartFile imgFile : imgFiles) {
//                imgSrc = imageUtil.saveImage(imgFile, IMAGE_PATH);
//                boardImageDtos.add(BoardImageDto.builder().imgSrc(imgSrc).build());
//            }
//            boardImageService.saveImage(boardImageDtos, board);
//        }
//
//        return new BoardResponseDto(board, board.getUser(), boardImageDtos,
//                board.getComments().stream().map(CommentResponseDto::new).toList());
//    }
}
