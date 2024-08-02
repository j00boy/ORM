package orm.orm_backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import orm.orm_backend.dto.request.BoardRequestDto;
import orm.orm_backend.dto.request.ClubRequestDto;
import orm.orm_backend.dto.response.BoardResponseDto;
import orm.orm_backend.dto.response.ClubResponseDto;
import orm.orm_backend.service.BoardService;
import orm.orm_backend.service.ClubService;
import orm.orm_backend.util.JwtUtil;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs")
public class BoardController {
    @Value("${orm.header.auth}")
    private String HEADER_AUTH;

    private final JwtUtil jwtUtil;

    private final BoardService boardService;

    @PostMapping("/boards/create")
        public ResponseEntity<BoardResponseDto> createBoard(@RequestPart("createBoard") BoardRequestDto boardRequestDto,
                                                            @RequestPart(value = "imgFile", required = false) List<MultipartFile> imgFile,
                                                            HttpServletRequest request) {
        String accessToken = request.getHeader(HEADER_AUTH);
        Integer userId = jwtUtil.getUserIdFromAccessToken(accessToken);
        BoardResponseDto boardResponseDto = boardService.createBoard(boardRequestDto, imgFile, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(boardResponseDto);
    }
}
