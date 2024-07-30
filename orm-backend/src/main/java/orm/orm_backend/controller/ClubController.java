package orm.orm_backend.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import orm.orm_backend.dto.common.ApplicantDto;
import orm.orm_backend.dto.request.ClubRequestDto;
import orm.orm_backend.dto.request.ClubSearchRequestDto;
import orm.orm_backend.dto.request.MemberRequestDto;
import orm.orm_backend.dto.response.ClubResponseDto;
import orm.orm_backend.exception.UnAuthorizedException;
import orm.orm_backend.service.ClubService;
import orm.orm_backend.util.JwtUtil;

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
    public ResponseEntity<Integer> createClub(@RequestPart("createClub") ClubRequestDto clubRequestDto, @RequestPart(value = "imgFile", required = false) MultipartFile imgFile, HttpServletRequest request) {
        String accessToken = request.getHeader(HEADER_AUTH);
        Integer userId = jwtUtil.getUserIdFromAccessToken(accessToken);
        Integer clubId = clubService.createClub(clubRequestDto, imgFile, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(clubId);
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

    @GetMapping("/mountain/{mountainId}")
    public ResponseEntity<List<ClubResponseDto>> findClubsByMountain(@PathVariable Integer mountainId, HttpServletRequest request) {
        String accessToken = request.getHeader(HEADER_AUTH);
        Integer userId = jwtUtil.getUserIdFromAccessToken(accessToken);
        List<ClubResponseDto> result = clubService.getAllClubsByMountain(mountainId, userId);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/name/check-duplicate")
    public ResponseEntity<Boolean> isValid(@RequestParam("name") String name) {
        Boolean isValid = clubService.isValid(name);
        return ResponseEntity.ok().body(isValid);
    }

    @PostMapping("/members/apply")
    public ResponseEntity<Integer> joinClub(@RequestBody ApplicantDto applicantDto){
        Integer result = clubService.joinClub(applicantDto).getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/members/leave")
    public ResponseEntity<Void> deleteMember(@RequestParam("userId") Integer userId, @RequestParam("clubId") Integer clubId, HttpServletRequest request) {
        String accessToken = request.getHeader(HEADER_AUTH);
        Integer currId = jwtUtil.getUserIdFromAccessToken(accessToken);
        // 본인 아이디가 아닌 경우 탈퇴하지 못한다.
        if (!currId.equals(userId)) throw new UnAuthorizedException();
        clubService.deleteMember(userId, clubId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/members/drop")
    public ResponseEntity<Void> dropMember(@RequestParam("userId") Integer userId, @RequestParam("clubId") Integer clubId, HttpServletRequest request) {
        String accessToken = request.getHeader(HEADER_AUTH);
        Integer currId = jwtUtil.getUserIdFromAccessToken(accessToken);
        clubService.dropMember(currId, userId, clubId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/members/approve")
    public ResponseEntity<Void> approveMember(@RequestBody MemberRequestDto memberRequestDto) {
         clubService.approveMember(memberRequestDto);
         return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

