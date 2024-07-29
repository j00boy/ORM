package orm.orm_backend.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import orm.orm_backend.entity.Club;
import orm.orm_backend.entity.Mountain;
import orm.orm_backend.entity.User;

@Data
@Builder
public class ClubRequestDto {
    private Integer mountainId;
    private String clubName;
    private String description;

    public Club toEntity(User user, Mountain mountain, String imageSrc) {
        return Club.builder()
                .manager(user)
                .mountain(mountain)
                .clubName(clubName)
                .description(description)
                .imageSrc(imageSrc)
                .build();
    }
}
