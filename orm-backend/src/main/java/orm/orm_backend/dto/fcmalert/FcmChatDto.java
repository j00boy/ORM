package orm.orm_backend.dto.fcmalert;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FcmChatDto implements FcmAlertData {
    private Integer chatRoomId;
    private Integer senderId;
    private String senderName;
    private String chatMessage;

    @Override
    public String getMessage() {
        return senderName + ": " + chatMessage;
    }
}
