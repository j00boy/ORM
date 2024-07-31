package orm.orm_backend.service;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import orm.orm_backend.dto.common.ApplicantDto;
import orm.orm_backend.dto.request.*;
import orm.orm_backend.dto.response.ClubResponseDto;
import orm.orm_backend.dto.response.MemberResponseDto;
import orm.orm_backend.entity.*;
import orm.orm_backend.exception.CustomException;
import orm.orm_backend.exception.ErrorCode;
import orm.orm_backend.exception.UnAuthorizedException;
import orm.orm_backend.repository.ClubRepository;
import orm.orm_backend.util.ImageUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
        Member member = MemberRequestDto.toEntity(user, club);
        memberService.saveMember(member);

        return club.getId();
    }

    public ClubResponseDto updateClub(ClubRequestDto clubRequestDto, Integer clubId, Integer userId, MultipartFile imgFile) {
        Optional<Club> opt = clubRepository.findById(clubId);

        if(opt.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_CLUB_ID);
        }
        Club club = opt.get();

        if (!club.getManager().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        // 사진 업로드
        String imageSrc = null;

        if (imgFile != null) {
            if (club.getImageSrc() != null) {
                imageUtil.deleteImage(club.getImageSrc());
            }
            imageSrc = imageUtil.saveImage(imgFile, IMAGE_PATH);
        }

        Mountain mountain = mountainService.getMountainById(clubRequestDto.getMountainId());
        club.update(clubRequestDto, mountain, imageSrc);
        return ClubResponseDto.toMyDto(club);
    }

    // Club 조회
    public List<ClubResponseDto> getAllClubs(ClubSearchRequestDto clubSearchRequestDto, Integer userId) {
        Boolean isMyClub = clubSearchRequestDto.getIsMyClub();
        Pageable pageable = PageRequest.of(clubSearchRequestDto.getPgno(), clubSearchRequestDto.getRecordSize());
        List<ClubResponseDto> clubs = new ArrayList<>();

        // 내 모임 검색이면
        if (isMyClub) {
            Page<Member> members = memberService.getPageableMembers(pageable, userId);
            for (Member m : members) {
                clubRepository.findById(m.getClub().getId())
                        .ifPresent(club -> clubs.add(ClubResponseDto.toMyDto(club)));
            }
        } else {
            Page<Club> results = clubRepository.findAllByClubNameContaining(pageable, clubSearchRequestDto.getKeyword());
            Set<Integer> clubMap = memberService.getClubs(userId);
            Set<Integer> applicantMap = applicantService.getApplicants(userId);

            for (Club c : results) {
                Boolean isMember = clubMap.contains(c.getId());
                Boolean isApplied = applicantMap.contains(c.getId());
                clubs.add(ClubResponseDto.toDto(c, isMember, isApplied));
            }
        }
        return clubs;
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
            clubs.add(ClubResponseDto.toDto(c, isMember, isApplied));
        }

        return clubs;
    }

    // 회원 목록 조회
    public Map<String, Object> getMembers(Integer clubId, Integer userId) {
        Club club = clubRepository.findById(clubId).orElse(null);
        if (club == null) {
            throw new NoResultException("id에 해당하는 클럽이 없습니다.");
        }

        List<MemberResponseDto> members = memberService.getMembersInClub(clubId).stream().map(MemberResponseDto::toDto).toList();
        List<ApplicantDto> applicants = (!club.isManager(userId)) ? null : applicantService.getApplicantsInClub(clubId).stream().map(ApplicantDto::toDto).toList();

        Map<String, Object> result = new HashMap<>();
        result.put("members", members);
        result.put("requestMembers", applicants);

        return result;
    }

    // 중복 체크
    public Boolean isDuplicated(String clubName) {
        return clubRepository.existsByClubName(clubName);
    }

    // 가입 신청
    public Applicant joinClub(ApplicantDto applicantDto) {
        // user 찾기
        User user = userService.findUserById(applicantDto.getUserId());
        // club 찾기
        Club club = clubRepository.findById(applicantDto.getClubId()).orElseThrow(NoResultException::new);
        // applicant 저장
        Applicant applicant = applicantDto.toEntity(user, club);
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
        if (!club.getManager().getId().equals(userId)) {
            throw new UnAuthorizedException();
        }
        memberService.delete(userId, clubId);
    }

    // 가입 수락/거절
    public void approveMember(MemberRequestDto memberRequestDto) {
        if (memberRequestDto.getIsApproved()) {
            User user = userService.findUserById(memberRequestDto.getUserId());
            Club club = clubRepository.findById(memberRequestDto.getClubId()).orElseThrow();
            memberService.saveMember(MemberRequestDto.toEntity(user, club));
        }
        applicantService.deleteApplicant(memberRequestDto);
    }
}
