package orm.orm_backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import orm.orm_backend.dto.request.CommentRequestDto;
import orm.orm_backend.dto.response.CommentResponseDto;
import orm.orm_backend.service.CommentService;
import orm.orm_backend.util.JwtUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs/boards")
public class CommentController {

    @Value("${orm.header.auth}")
    private String HEADER_AUTH;

    private final JwtUtil jwtUtil;

    private final CommentService commentService;

    @PostMapping("/{boardId}/comments/create")
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable("boardId") Integer boardId,
                                                            @RequestBody CommentRequestDto commentRequestDto,
                                                            HttpServletRequest request) {
        String accessToken = request.getHeader(HEADER_AUTH);
        Integer userId = jwtUtil.getUserIdFromAccessToken(accessToken);
        CommentResponseDto commentResponseDto = commentService.createComment(userId, boardId, commentRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponseDto);
    }

    @DeleteMapping("/{boardId}/comments/delete")
    public ResponseEntity<Void> deleteComment(@PathVariable("boardId") Integer boardId,
                                              Integer commentId,
                                              HttpServletRequest request) {
        String accessToken = request.getHeader(HEADER_AUTH);
        Integer userId = jwtUtil.getUserIdFromAccessToken(accessToken);
        commentService.deleteComment(userId, boardId, commentId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{boardId}/comments/update/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable("boardId") Integer boardId,
                                                            @PathVariable("commentId") Integer commentId,
                                                            @RequestBody CommentRequestDto commentRequestDto,
                                                            HttpServletRequest request) {
        String accessToken = request.getHeader(HEADER_AUTH);
        Integer userId = jwtUtil.getUserIdFromAccessToken(accessToken);
        CommentResponseDto commentResponseDto = commentService.updateComment(userId, boardId, commentId, commentRequestDto);
        return ResponseEntity.ok().body(commentResponseDto);
    }

}
