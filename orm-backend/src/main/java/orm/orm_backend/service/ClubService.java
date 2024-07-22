package orm.orm_backend.service;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import orm.orm_backend.dto.request.ClubRequestDto;
import orm.orm_backend.dto.request.ClubSearchRequestDto;
import orm.orm_backend.dto.response.ClubResponseDto;
import orm.orm_backend.entity.*;
import orm.orm_backend.repository.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ClubService {
    private final String UPLOADDIR = "src/main/resources/static/uploads/image";

    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final MountainRepository mountainRepository;
    private final MemberService memberService;
    private final ApplicantService applicantService;

    public Integer createClub(ClubRequestDto clubRequestDTO, Integer userId) {
        // user 찾기
        User user = userRepository.findById(userId).orElseThrow(NoResultException::new);
        // mountain 찾기
        Mountain mountain = mountainRepository.findById(clubRequestDTO.getMountainId())
                .orElseThrow(NoResultException::new);

        // 사진 업로드
        MultipartFile image = clubRequestDTO.getImgFile();
        String imageSrc = null;

        if (image != null) {
            try {
                imageSrc = saveImage(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        Club club = clubRequestDTO.toEntity(user, mountain, imageSrc);
        return clubRepository.save(club).getId();
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
                        .ifPresent(club -> clubs.add(new ClubResponseDto().toDto(club, Boolean.TRUE, Boolean.FALSE)));
            }
        } else {
            Page<Club> results = clubRepository.findAllByClubNameContaining(pageable, clubSearchRequestDto.getKeyword());
            Set<Integer> clubMap = memberService.getClubs(userId);
            Set<Integer> applicantMap = applicantService.getApplicants(userId);

            for (Club c : results) {
                Boolean isMember = clubMap.contains(c.getId()) ? Boolean.TRUE : Boolean.FALSE;
                Boolean isApplied = applicantMap.contains(c.getId()) ? Boolean.TRUE : Boolean.FALSE;
                clubs.add(new ClubResponseDto().toDto(c, isMember, isApplied));
            }
        }
        return clubs;
    }

    // 이미지 파일을 저장하는 메서드
    private String saveImage(MultipartFile image) throws IOException {
        // 파일 이름 생성
        String fileName = UUID.randomUUID().toString().replace("-", "") + "_" + image.getOriginalFilename();
        // 실제 파일이 저장될 경로
        String filePath = UPLOADDIR + fileName;

        // DB에 저장할 경로 문자열
        String dbFilePath = "/uploads/image/" + fileName;

        Path path = Paths.get(filePath); // Path 객체 생성

        // 디렉토리 없다면 생성
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
        }
        Files.write(path, image.getBytes()); // 디렉토리에 파일 저장

        return dbFilePath;
    }
}
