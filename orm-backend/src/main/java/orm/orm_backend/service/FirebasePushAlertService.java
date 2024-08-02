package orm.orm_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import orm.orm_backend.dto.fcmalert.FcmAcceptanceDto;
import orm.orm_backend.dto.fcmalert.FcmAlertData;
import orm.orm_backend.dto.fcmalert.FcmClubApplicationDto;
import orm.orm_backend.dto.fcmalert.FcmNotification;
import orm.orm_backend.entity.Club;
import orm.orm_backend.entity.User;
import orm.orm_backend.util.FirebaseUtil;

import java.io.IOException;

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
}
