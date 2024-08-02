package orm.orm_backend.dto.fcmalert;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FcmChatDto implements FcmAlertData {
    private String chatRoomId;
    private String senderId;
    private String senderName;
    private String chatMessage;
    private final AlertType alertType = AlertType.CHAT;

    @Override
    public String getMessage() {
        return senderName + ": " + chatMessage;
    }
}
