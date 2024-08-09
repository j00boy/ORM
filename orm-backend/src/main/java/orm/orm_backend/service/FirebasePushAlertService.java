package orm.orm_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import orm.orm_backend.dto.fcmalert.*;
import orm.orm_backend.entity.Board;
import orm.orm_backend.entity.Club;
import orm.orm_backend.entity.User;
import orm.orm_backend.util.FirebaseUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FirebasePushAlertService {

    private final FirebaseUtil firebaseUtil;

    public void pushClubApplicationAlert(String managerToken, User applicant, Club club)  {
        FcmAlertData data = FcmClubApplicationDto.builder()
                .clubId(String.valueOf(club.getId()))
                .clubName(club.getClubName())
                .clubImageSrc(club.getImageSrc())
                .userId(String.valueOf(applicant.getId()))
                .userName(applicant.getNickname())
                .build();
        FcmNotification notification = FcmNotification.builder()
                .body(data.getMessage())
                .build();
        firebaseUtil.pushAlert(data, managerToken, notification);
    }

    public void pushClubAcceptanceAlert(String applicantToken, Club club)  {
        FcmAlertData data = FcmAcceptanceDto.builder()
                .clubId(String.valueOf(club.getId()))
                .clubName(club.getClubName())
                .clubImageSrc(club.getImageSrc())
                .isAccepted(true)
                .build();
        FcmNotification notification = FcmNotification.builder()
                .body(data.getMessage())
                .build();
        firebaseUtil.pushAlert(data, applicantToken, notification);
    }

    public void pushClubRejectionAlert(String applicantToken, Club club) {
        FcmAlertData data = FcmAcceptanceDto.builder()
                .clubId(String.valueOf(club.getId()))
                .clubName(club.getClubName())
                .clubImageSrc(club.getImageSrc())
                .isAccepted(false)
                .build();
        FcmNotification notification = FcmNotification.builder()
                .body(data.getMessage())
                .build();
        firebaseUtil.pushAlert(data, applicantToken, notification);
    }

    public void pushClubExpelAlert(String expelMemberToken, Club club) {
        FcmAlertData data = FcmExpelDto.builder()
                .clubId(String.valueOf(club.getId()))
                .clubName(club.getClubName())
                .clubImageSrc(club.getImageSrc())
                .build();
        FcmNotification notification = FcmNotification.builder()
                .body(data.getMessage())
                .build();
        firebaseUtil.pushAlert(data, expelMemberToken, notification);
    }

    public void pushNewBoardAlert(List<String> clubUserTokens, Board board) {
        FcmAlertData data = FcmBoardDto.builder()
                .boardId(String.valueOf(board.getId()))
                .title(board.getTitle())
                .clubName(board.getClub().getClubName())
                .userName(board.getUser().getNickname())
                .userId(String.valueOf(board.getUser().getId()))
                .build();
        FcmNotification notification = FcmNotification.builder()
                .body(data.getMessage())
                .build();

        for(String clubUserToken : clubUserTokens) {
            firebaseUtil.pushAlert(data, clubUserToken, notification);
        }
    }
}
