package orm.orm_backend.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import orm.orm_backend.dto.request.CommentRequestDto;
import orm.orm_backend.dto.response.BoardResponseDto;
import orm.orm_backend.dto.response.CommentResponseDto;
import orm.orm_backend.entity.Board;
import orm.orm_backend.entity.Comment;
import orm.orm_backend.entity.User;
import orm.orm_backend.exception.CustomException;
import orm.orm_backend.exception.ErrorCode;
import orm.orm_backend.repository.BoardRepository;
import orm.orm_backend.repository.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final MemberService memberService;
    private final BoardService boardService;
    private final UserService userService;

    @Transactional
    public CommentResponseDto createComment(Integer userId, Integer boardId, CommentRequestDto commentRequestDto) {

        // board가 있는지 확인
        Board board = boardService.getBoardById(boardId);

        // 클럽의 멤버인지 확인
        if(!memberService.isContained(userId, board.getClub().getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        User user = userService.findUserById(userId);

        Comment comment = commentRequestDto.toEntity(board, user, commentRequestDto);
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    public void deleteComment(Integer userId, Integer boardId, Integer commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        if(!comment.isBoardIdMatching(boardId) || !comment.isOwnedByUser(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        commentRepository.deleteById(commentId);
    }

    @Transactional
    public CommentResponseDto updateComment(Integer userId, Integer boardId, Integer commentId, CommentRequestDto commentRequestDto) {
        // 해당 댓글 가져오기
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        // 게시글에 속한 댓글이 아니거나, 작성자가 아니라면
        if(!comment.isBoardIdMatching(boardId) || !comment.isOwnedByUser(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        comment.update(commentRequestDto);

        return new CommentResponseDto(comment);
    }
}
