package orm.orm_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import orm.orm_backend.dto.request.CommentRequestDto;
import orm.orm_backend.dto.response.CommentResponseDto;
import orm.orm_backend.entity.Board;
import orm.orm_backend.entity.Comment;
import orm.orm_backend.entity.User;
import orm.orm_backend.exception.CustomException;
import orm.orm_backend.exception.ErrorCode;
import orm.orm_backend.repository.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final MemberService memberService;
    private final BoardService boardService;
    private final UserService userService;

    /**
     * boardId에 해당하는 게시판에 userId를 가진 사용자가 댓글을 생성합니다.
     * @param userId
     * @param boardId
     * @param commentRequestDto
     * @return
     */
    @Transactional
    public CommentResponseDto createComment(Integer userId, Integer boardId, CommentRequestDto commentRequestDto) {
        // board가 있는지 확인
        Board board = boardService.getBoardById(boardId);

        // 클럽의 멤버인지 확인
        if(!memberService.isContained(userId, board.getClub().getId())) {
            throw new CustomException(ErrorCode.BOARD_FORBIDDEN);
        }

        User user = userService.findUserById(userId);

        Comment comment = commentRequestDto.toEntity(board, user, commentRequestDto);
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    /**
     * 유효성 검증 후 댓글을 삭제합니다.
     * @param userId
     * @param boardId
     * @param commentId
     */
    public void deleteComment(Integer userId, Integer boardId, Integer commentId) {
        // comment가 있는지 확인
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        if(!comment.isBoardIdMatching(boardId) || !comment.isOwnedByUser(userId)) {
            throw new CustomException(ErrorCode.COMMENT_FORBIDDEN);
        }

        commentRepository.deleteById(commentId);
    }

    /**
     * 유효성 검증 후 댓글을 수정합니다.
     * @param userId
     * @param boardId
     * @param commentId
     * @param commentRequestDto
     * @return
     */
    @Transactional
    public CommentResponseDto updateComment(Integer userId, Integer boardId, Integer commentId, CommentRequestDto commentRequestDto) {
        // 해당 댓글 가져오기
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        // 게시글에 속한 댓글이 아니거나, 작성자가 아니라면
        if(!comment.isBoardIdMatching(boardId) || !comment.isOwnedByUser(userId)) {
            throw new CustomException(ErrorCode.COMMENT_FORBIDDEN);
        }

        comment.update(commentRequestDto);

        return new CommentResponseDto(comment);
    }
}
