package orm.orm_backend.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import orm.orm_backend.dto.request.ApplicantRequestDto;
import orm.orm_backend.dto.request.ClubRequestDto;
import orm.orm_backend.dto.request.ClubSearchRequestDto;
import orm.orm_backend.dto.request.MemberRequestDto;
import orm.orm_backend.dto.response.ClubResponseDto;
import orm.orm_backend.service.ClubService;
import orm.orm_backend.util.JwtUtil;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs")
public class ClubController {
    @Value("${orm.header.auth}")
    private String HEADER_AUTH;

    private final JwtUtil jwtUtil;

    private final ClubService clubService;

    @PostMapping("/create")
    public ResponseEntity<Integer> createClub(@RequestBody ClubRequestDto clubRequestDto, HttpServletRequest request) {
        String accessToken = request.getHeader(HEADER_AUTH);
        Integer userId = jwtUtil.getUserIdFromAccessToken(accessToken);
        Integer clubId = clubService.createClub(clubRequestDto, userId);
        HttpHeaders headers = jwtUtil.createTokenHeaders(userId);
        return ResponseEntity.created(URI.create("/clubs"))
                .headers(headers).body(clubId);
    }

    @GetMapping
    public ResponseEntity<List<ClubResponseDto>> findClubs(@RequestBody ClubSearchRequestDto clubSearchRequestDto, HttpServletRequest request) {
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
    public ResponseEntity<Boolean> isValid(@RequestParam("name") String name) {
        Boolean isValid = clubService.isValid(name);
        return ResponseEntity.ok().body(isValid);
    }

    @PostMapping("/members/apply")
    public ResponseEntity<Integer> joinClub(@RequestBody ApplicantRequestDto applicantRequestDto){
        Integer result = clubService.joinClub(applicantRequestDto).getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/members/leave")
    public ResponseEntity<Void> deleteMember(@RequestBody MemberRequestDto memberRequestDto) {
        clubService.deleteMember(memberRequestDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/members/approve")
    public ResponseEntity<Void> approveMember(@RequestBody MemberRequestDto memberRequestDto) {
         clubService.approveMember(memberRequestDto);
         return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

