package orm.orm_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import orm.orm_backend.entity.Board;
import orm.orm_backend.entity.Club;
import orm.orm_backend.entity.Comment;
import orm.orm_backend.entity.User;
import orm.orm_backend.util.FirebaseUtil;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:test-application.properties")
public class FirebaseServiceTest {

    @InjectMocks
    FirebasePushAlertService firebasePushAlertService;

    @Spy
    FirebaseUtil firebaseUtil;

    @Value("${firebase.test-token}")
    private String testFirebaseToken;

    @Value("${firebase.test-token2}")
    private String testFirebaseToken2;

    List<String> testFirebaseTokens;
    Set<String> testFBTokens;

    @Value("${firebase.api-url}")
    private String firebaseApiUrl;

    @Value("${firebase.config-path}")
    private String firebaseConfigPath;

    @Value("${test.image-src}")
    private String testImageSrc;

    @Mock
    User user;

    @Mock
    Club club;

    @Mock
    Board board;

    @Mock
    Comment comment;

    Integer userId = 6;
    Integer clubId = 1;
    Integer boardId = 1;
    Integer commentId = 1;
    String userName = "김세진";
    String clubName = "백승헌 클럽";
    String boardTitle = "백승헌 게시판";


    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(firebaseUtil, "firebaseApiUrl", firebaseApiUrl);
        ReflectionTestUtils.setField(firebaseUtil, "firebaseConfigPath", firebaseConfigPath);
        testFirebaseTokens = List.of(testFirebaseToken, testFirebaseToken2);
        testFBTokens = Set.of(testFirebaseToken, testFirebaseToken2);

        lenient().when(comment.getId()).thenReturn(commentId);
        lenient().when(comment.getBoard()).thenReturn(board);
        lenient().when(comment.getUser()).thenReturn(user);
        lenient().when(board.getUser()).thenReturn(user);
        lenient().when(board.getClub()).thenReturn(club);
        lenient().when(board.getTitle()).thenReturn(boardTitle);
        lenient().when(board.getId()).thenReturn(boardId);
        lenient().when(user.getId()).thenReturn(userId);
        lenient().when(club.getId()).thenReturn(clubId);
        lenient().when(user.getNickname()).thenReturn(userName);
        lenient().when(club.getClubName()).thenReturn(clubName);
        lenient().when(club.getImageSrc()).thenReturn(testImageSrc);
    }

    @Test
    void pushClubApplicationAlertTest() {
        firebasePushAlertService.pushClubApplicationAlert(testFirebaseToken, user, club);
    }

    @Test
    void pushClubAcceptanceAlertTest() {
        firebasePushAlertService.pushClubAcceptanceAlert(testFirebaseToken, club);
    }

    @Test
    void pushClubRejectionAlertTest() {
        firebasePushAlertService.pushClubRejectionAlert(testFirebaseToken, club);
    }

    @Test
    void pushClubExpelAlertTest() {
        firebasePushAlertService.pushClubExpelAlert(testFirebaseToken, club);
    }

    @Test
    void pushNewBoardAlertTest() {
        firebasePushAlertService.pushNewBoardAlert(testFirebaseTokens, board);
    }

    @Test
    void pushNewCommentAlertTest() {
        firebasePushAlertService.pushNewCommentAlert(testFBTokens, comment);
    }
}
