package orm.orm_backend.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import orm.orm_backend.dto.request.ClubRequestDto;
import orm.orm_backend.dto.request.ClubSearchRequestDto;
import orm.orm_backend.dto.response.ClubResponseDto;
import orm.orm_backend.service.ClubService;
import orm.orm_backend.util.JwtUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs")
public class ClubController {
    private final ClubService clubService;
    private final JwtUtil jwtUtil;
    private final String HEADER_AUTH = "Authorization";

    @PostMapping("/create")
    public ResponseEntity<Integer> createClub(ClubRequestDto clubRequestDto, HttpServletRequest request) {
        String accessToken = request.getHeader(HEADER_AUTH);
        Integer userId = jwtUtil.getUserIdFromAccessToken(accessToken);
        Integer clubId = clubService.createClub(clubRequestDto, userId);
        return ResponseEntity.ok().body(clubId);
    }

    @GetMapping
    public ResponseEntity<List<ClubResponseDto>> findClubs(ClubSearchRequestDto clubSearchRequestDto, HttpServletRequest request) {
        String accessToken = request.getHeader(HEADER_AUTH);
        Integer userId = jwtUtil.getUserIdFromAccessToken(accessToken);
        List<ClubResponseDto> clubs = clubService.getAllClubs(clubSearchRequestDto, userId);

        return ResponseEntity.ok().body(clubs);
    }

    @GetMapping("/members")
    public ResponseEntity<Map<String, Object>> findMembers(@RequestParam("clubId") Integer clubId, HttpServletRequest request) {
        String accessToken = request.getHeader(HEADER_AUTH);
        Integer userId = jwtUtil.getUserIdFromAccessToken(accessToken);
        Map<String, Object> result = clubService.getMembers(clubId, userId);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/name/check-duplicate")
    public ResponseEntity<Map<String, Boolean>> isValid(@RequestParam("name") String name) {
        Map<String, Boolean> result = new HashMap<>();
        Boolean isValid = clubService.isValid(name);
        result.put("isValid", isValid);
        return ResponseEntity.ok().body(result);
    }



}

