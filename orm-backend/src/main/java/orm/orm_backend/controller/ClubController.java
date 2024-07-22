package orm.orm_backend.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import orm.orm_backend.dto.request.ClubRequestDto;
import orm.orm_backend.dto.request.ClubSearchRequestDto;
import orm.orm_backend.dto.response.ClubResponseDto;
import orm.orm_backend.service.ClubService;
import orm.orm_backend.util.JwtUtil;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs")
public class ClubController {
    private final ClubService clubService;
    private final JwtUtil jwtUtil;
    private final String HEADER_AUTH = "Authorization";

    @PostMapping("/create")
    public ResponseEntity<?> createClub(ClubRequestDto clubRequestDto, HttpServletRequest request) {
        String accessToken = request.getHeader(HEADER_AUTH);
        Integer userId = jwtUtil.getUserIdFromAccessToken(accessToken);
        Integer clubId = clubService.createClub(clubRequestDto, userId);
        return ResponseEntity.ok().body(clubId);
    }

    @GetMapping("")
    public ResponseEntity<?> findClubs(ClubSearchRequestDto clubSearchRequestDto, HttpServletRequest request) {
        String accessToken = request.getHeader(HEADER_AUTH);
        Integer userId = jwtUtil.getUserIdFromAccessToken(accessToken);
        List<ClubResponseDto> clubs = clubService.getAllClubs(clubSearchRequestDto, userId);
        return ResponseEntity.ok().body(clubs);
    }


}

