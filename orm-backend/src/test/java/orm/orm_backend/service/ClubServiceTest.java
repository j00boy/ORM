package orm.orm_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import orm.orm_backend.dto.request.ClubRequestDto;
import orm.orm_backend.entity.Club;
import orm.orm_backend.entity.Mountain;
import orm.orm_backend.entity.User;
import orm.orm_backend.repository.ClubRepository;
import orm.orm_backend.util.ImageUtil;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;


//@Slf4j
//@ExtendWith(MockitoExtension.class)
//public class ClubServiceTest {
//    @InjectMocks
//    ClubService clubService;
//
//    @Mock
//    MountainService mountainService;
//
//    @Mock
//    ClubRepository clubRepository;
//
//    @Mock
//    ImageUtil imageUtil;
//
//    @Mock
//    ClubRequestDto clubRequestDto;
//
//    @Mock
//    Club club;
//
//    @Mock
//    Mountain mountain;
//
//    @Mock
//    Mountain after_mountain;
//
//
//    @Mock
//    User user;
//
//    // Club
//    private Integer clubId = 1;
//    private String description = "내 모임에 대한 설명입니다.";
//    private String clubName = "오늘등산 산악코딩";
//    final String fileName = "testImage1";
//    final String originalFileName = "gwanakMountain";
//    final String contentType = "img";
//
//    private String imgSrc = "src/test/resources/testImage/" + fileName + "." + contentType;
//
//    // Mountain
//    private Integer mountainId = 2;
//
//    // User
//    private Integer userId = 2;
//
//    // ClubRequestDto
//    private Integer after_mountainId = 1;
//    private String after_description = "모임 설명입니다";
//    private String after_clubName = "ORM 산악코딩";
//
//    // New File
//    private MockMultipartFile imgFile;
//    final String after_fileName = "testImage2";
//    final String after_originalFileName = "garakMountain";
//    final String after_contentType = "img";
//    final String after_imgSrc = "src/test/resources/testImage/" + after_fileName + "." + after_contentType;
//
//    @BeforeEach
//    void setUp() throws IOException {
//        when(club.getId()).thenReturn(clubId);
//        when(mountain.getId()).thenReturn(mountainId);
//
//        when(club.getManager()).thenReturn(user);
//        when(user.getId()).thenReturn(userId);
//
//        when(after_mountain.getId()).thenReturn(after_mountainId);
//
//        club = Club.builder()
//                .manager(user)
//                .mountain(mountain)
//                .clubName(clubName)
//                .imageSrc(imgSrc)
//                .description(description)
//                .build();
//
//        imgFile = new MockMultipartFile(after_fileName, after_originalFileName, after_contentType, (byte[]) null);
//
//        clubRequestDto = ClubRequestDto
//                .builder()
//                .clubName(after_clubName)
//                .description(after_description)
//                .mountainId(after_mountainId)
//                .build();
//    }
//
//    @Test
//    @DisplayName("클럽 업데이트 테스트")
//    void update_club() {
//        when(clubRepository.findById(clubId)).thenReturn(Optional.ofNullable(club));
//        when(mountainService.getMountainById(clubRequestDto.getMountainId())).thenReturn(after_mountain);
//        verify(imageUtil).deleteImage(imgSrc);
//        clubService.updateClub(clubRequestDto, clubId, userId, imgFile);
//
//        assertThat(club.getClubName()).isEqualTo(after_clubName);
//        assertThat(club.getDescription()).isEqualTo(after_description);
//        assertThat(club.getImageSrc()).isEqualTo(after_imgSrc);
//    }
//}
