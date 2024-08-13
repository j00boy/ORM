package orm.orm_backend.service;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import orm.orm_backend.dto.request.ApplicantRequestDto;
import orm.orm_backend.dto.request.ClubRequestDto;
import orm.orm_backend.dto.request.ClubSearchRequestDto;
import orm.orm_backend.dto.request.MemberRequestDto;
import orm.orm_backend.dto.response.ApplicantResponseDto;
import orm.orm_backend.dto.response.ClubResponseDto;
import orm.orm_backend.dto.response.MemberResponseDto;
import orm.orm_backend.entity.*;
import orm.orm_backend.exception.CustomException;
import orm.orm_backend.exception.ErrorCode;
import orm.orm_backend.exception.UnAuthorizedException;
import orm.orm_backend.repository.ClubRepository;
import orm.orm_backend.util.ImageUtil;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClubService {
    private final String IMAGE_PATH = "club/thumbnail/";

    private final ImageUtil imageUtil;

    private final UserService userService;
    private final MountainService mountainService;
    private final MemberService memberService;
    private final ApplicantService applicantService;
    private final FirebasePushAlertService firebasePushAlertService;

    private final ClubRepository clubRepository;
    public Integer createClub(ClubRequestDto clubRequestDTO, MultipartFile imgFile, Integer userId) {
        // user 찾기
        User user = userService.findUserById(userId);
        // mountain 찾기
        Mountain mountain = mountainService.getMountainById(clubRequestDTO.getMountainId());

        // 사진 업로드
        String imageSrc = null;

        if (imgFile != null) {
            imageSrc = imageUtil.saveImage(imgFile, IMAGE_PATH);
        }

        // club 생성
        Club club = clubRequestDTO.toEntity(user, mountain, imageSrc);
        clubRepository.save(club);

        // club 생성 이후 해당 user 를 member table에 추가 (관리자도 회원)
        memberService.saveMember(Member.builder().user(user).club(club).build());

        return club.getId();
    }

    public ClubResponseDto updateClub(ClubRequestDto clubRequestDto, Integer clubId, Integer userId, MultipartFile imgFile) {
        Club club = clubRepository.findById(clubId).orElseThrow(() -> new CustomException(ErrorCode.INVALID_CLUB_ID));

        if (!club.getManager().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        // 사진 업로드
        String imageSrc = null;

        if (imgFile != null) {
            imageSrc = imageUtil.saveImage(imgFile, IMAGE_PATH);
            // 새로운 사진 파일이 왔으니 기존에 이미지가 있다면 삭제한다.
            if (club.getImageSrc() != null) {
                imageUtil.deleteImage(club.getImageSrc());
            }
        }

        Mountain mountain = mountainService.getMountainById(clubRequestDto.getMountainId());
        club.update(clubRequestDto, mountain, imageSrc);
        return new ClubResponseDto(club);
    }

    // Club 조회
    public List<ClubResponseDto> getAllClubs(ClubSearchRequestDto clubSearchRequestDto, Integer userId) {
        Boolean isMyClub = clubSearchRequestDto.getIsMyClub();

        // 내 모임 검색이면
        if (isMyClub) {
            List<Member> members = memberService.getMemberList(userId);
            // club Id 조회
            List<Integer> clubIds = members.stream()
                    .filter(member -> member.getClub() != null) // null 제외
                    .map(member -> member.getClub().getId())
                    .toList();

            List<Club> clubs = clubRepository.findAllByIdIn(clubIds);
            Map<Integer, Long> applicantCounts = clubIds.stream()
                    .map(clubId -> Map.entry(clubId, applicantService.getApplicantCountOfClub(clubId)))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            return clubs.stream()
                    .map(club -> new ClubResponseDto(club, club.isManager(userId) ? applicantCounts.get(club.getId()) : 0))
                    .toList();
        }
        List<Club> clubs = clubRepository.findAllByClubNameContaining(clubSearchRequestDto.getKeyword());
        Set<Integer> clubMap = memberService.getClubs(userId);
        Set<Integer> applicantMap = applicantService.getApplicants(userId);

        return clubs.stream()
                .map(club -> new ClubResponseDto(club, clubMap.contains(club.getId()), applicantMap.contains(club.getId())))
                .toList();
    }

    // 특정 산을 기반으로 하는 모임 찾기
    public List<ClubResponseDto> getAllClubsByMountain(Integer mountainId, Integer userId) {
        List<Club> result = clubRepository.findAllByMountainId(mountainId);

        Set<Integer> clubMap = memberService.getClubs(userId);
        Set<Integer> applicantMap = applicantService.getApplicants(userId);

        List<ClubResponseDto> clubs = new ArrayList<>();

        for (Club c : result) {
            Boolean isMember = clubMap.contains(c.getId());
            Boolean isApplied = applicantMap.contains(c.getId());
            clubs.add(new ClubResponseDto(c, isMember, isApplied));
        }

        return clubs;
    }

    // 클럽 아이디를 기반으로 모임 찾기
    public ClubResponseDto getClubById(Integer clubId) {
        return new ClubResponseDto(clubRepository.findById(clubId).orElseThrow(() ->new CustomException(ErrorCode.INVALID_CLUB_ID)));
    }

    // 회원 목록 조회
    public Map<String, Object> getMembers(Integer clubId, Integer userId) {
        Club club = clubRepository.findById(clubId).orElse(null);
        if (club == null) {
            throw new NoResultException("id에 해당하는 클럽이 없습니다.");
        }

        List<MemberResponseDto> members = memberService.getMembersInClub(clubId).stream().map(MemberResponseDto::new).toList();
        List<ApplicantResponseDto> applicants = (!club.isManager(userId)) ? null : applicantService.getApplicantsInClub(clubId).stream().map(ApplicantResponseDto::new).toList();

        Map<String, Object> result = new HashMap<>();
        result.put("members", members);
        result.put("applicants", applicants);

        return result;
    }

    // 중복 체크
    public Boolean isDuplicated(String clubName) {
        return clubName.isBlank() || clubRepository.existsByClubName(clubName);
    }

    // 가입 신청
    public Applicant joinClub(ApplicantRequestDto applicantRequestDto) {
        Integer userId = applicantRequestDto.getUserId();
        Integer clubId = applicantRequestDto.getClubId();

        if (applicantService.isContained(userId, clubId)) {
            throw new CustomException(ErrorCode.ALREADY_APPLIED);
        }

        if (memberService.isContained(userId, clubId)) {
            throw new CustomException(ErrorCode.ALREADY_JOINED);
        }

        // user 찾기
        User user = userService.findUserById(applicantRequestDto.getUserId());
        // club 찾기
        Club club = clubRepository.findById(applicantRequestDto.getClubId()).orElseThrow(NoResultException::new);
        // applicant 저장
        Applicant applicant = applicantRequestDto.toEntity(user, club);

        // manager 조회
        User manager = club.getManager();
        firebasePushAlertService.pushClubApplicationAlert(manager.getFirebaseToken(), user, club);

        return applicantService.saveApplicant(applicant);
    }

    // 탈퇴
    public void deleteMember(Integer userId, Integer clubId) {
        Club club = clubRepository.findById(clubId).orElseThrow();
        // 클럽의 매니저인 경우 탈퇴가 불가
        if (club.getManager().getId().equals(userId)) {
            throw new UnAuthorizedException();
        }
        memberService.delete(userId, clubId);
    }

    // 추방
    public void dropMember(Integer currId, Integer userId, Integer clubId) {
        Club club = clubRepository.findById(clubId).orElseThrow();
        // 클럽의 매니저가 아닌 경우 추방 불가함.
        if (!club.getManager().getId().equals(currId)) {
            throw new UnAuthorizedException();
        }
        // 본인은 추방 불가능함
        if (club.getManager().getId().equals(userId)) {
            throw new UnAuthorizedException();
        }
        memberService.delete(userId, clubId);
        User user = userService.findUserById(userId);
        firebasePushAlertService.pushClubExpelAlert(user.getFirebaseToken(), club);
    }

    // 가입 수락/거절
    public void approveMember(MemberRequestDto memberRequestDto) {
        User user = userService.findUserById(memberRequestDto.getUserId());
        Club club = clubRepository.findById(memberRequestDto.getClubId()).orElseThrow();
        if (memberRequestDto.getIsApproved()) {
            memberService.saveMember(memberRequestDto.toEntity(user, club));
            firebasePushAlertService.pushClubAcceptanceAlert(user.getFirebaseToken(), club);
        } else { // 가입거절 시 푸쉬 alert 보내기
            firebasePushAlertService.pushClubRejectionAlert(user.getFirebaseToken(), club);
        }
        applicantService.deleteApplicant(memberRequestDto);
    }

    // 클럽 찾기
    public Club getByClubId(Integer clubId) {
        return clubRepository.findById(clubId).orElseThrow(() -> new CustomException(ErrorCode.INVALID_CLUB_ID));
    }

    // 신청한 모임 불러오기
    public List<ClubResponseDto> getAppliedClubs(Integer userId) {
        // 신청한 클럽 List
        return applicantService.getApplicantsById(userId).stream().filter(applicant -> applicant.getClub() != null)
                .map(applicant -> new ClubResponseDto(applicant.getClub(), false, true)).toList();
    }

    public void cancelApply(Integer userId, Integer clubId) {
        applicantService.deleteApplicant(userId, clubId);
    }
}
