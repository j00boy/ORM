package orm.orm_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import orm.orm_backend.dto.request.ClubRequestDto;
import orm.orm_backend.dto.response.ClubResponseDto;
import orm.orm_backend.entity.Club;
import orm.orm_backend.entity.Member;
import orm.orm_backend.entity.Mountain;
import orm.orm_backend.entity.User;
import orm.orm_backend.repository.ClubRepository;
import orm.orm_backend.util.ImageUtil;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@Slf4j
@ExtendWith(MockitoExtension.class)
public class ClubServiceTest {
    @InjectMocks
    ClubService clubService;

    @Mock
    MountainService mountainService;

    @Mock
    ClubRepository clubRepository;

    @Mock
    ImageUtil imageUtil;

    @Mock
    ClubRequestDto clubRequestDto;

    @Mock
    ClubResponseDto clubResponseDto;

    @Mock
    Club club;

    @Mock
    Mountain mountain;

    @Mock
    Mountain after_mountain;

    @Mock
    List<Member> members;

    @Mock
    User user;

    @Mock
    MockMultipartFile imgFile;

    private final String IMAGE_PATH = "club/thumbnail/";

    // Club
    private Integer clubId = 1;
    private String description = "내 모임에 대한 설명입니다.";
    private String clubName = "오늘등산 산악코딩";
    private String fileName = "testImage1";
    private String originalFileName = "gwanakMountain";
    private String contentType = "img";
    private String imgSrc = "src/test/resources/testImage/" + fileName + "." + contentType;

    // Mountain
    private Integer mountainId = 2;

    // User
    private Integer userId = 2;

    // ClubRequestDto
    private Integer after_mountainId = 1;
    private String after_description = "모임 설명입니다";
    private String after_clubName = "ORM 산악코딩";

    // New File
    private String after_fileName = "testImage2";
    private String after_originalFileName = "garakMountain";
    private String after_contentType = "img";
    private String after_imgSrc = "src/test/resources/testImage/" + after_fileName + "." + after_contentType;

    @BeforeEach
    void setUp() throws IOException {
        when(user.getId()).thenReturn(userId);
        when(after_mountain.getId()).thenReturn(after_mountainId);

        club = Club.builder()
                .manager(user)
                .mountain(mountain)
                .clubName(clubName)
                .imageSrc(imgSrc)
                .description(description)
                .build();

        imgFile = new MockMultipartFile(after_fileName, after_originalFileName, after_contentType, (byte[]) null);

        clubRequestDto = ClubRequestDto
                .builder()
                .clubName(after_clubName)
                .description(after_description)
                .mountainId(after_mountainId)
                .build();
    }

    @Test
    @DisplayName("클럽 업데이트 테스트")
    void update_club() {
        assertThat(club.getClubName()).isEqualTo(clubName);
        assertThat(club.getDescription()).isEqualTo(description);
        assertThat(club.getImageSrc()).isEqualTo(imgSrc);

        // when
        when(clubRepository.findById(clubId)).thenReturn(Optional.ofNullable(club));
        when(mountainService.getMountainById(clubRequestDto.getMountainId())).thenReturn(after_mountain);

        doReturn(after_imgSrc).when(imageUtil).saveImage(imgFile, IMAGE_PATH);
        doAnswer(invocation -> {
            log.info("delete 진행");
            return null;
        }).when(imageUtil).deleteImage(imgSrc);
        clubService.updateClub(clubRequestDto, clubId, userId, imgFile);

        // then
        assertThat(club.getClubName()).isNotEqualTo(clubName);
        assertThat(club.getDescription()).isNotEqualTo(description);
        assertThat(club.getImageSrc()).isNotEqualTo(imgSrc);

        assertThat(club.getClubName()).isEqualTo(after_clubName);
        assertThat(club.getDescription()).isEqualTo(after_description);
        assertThat(club.getImageSrc()).isEqualTo(after_imgSrc);
    }
}
