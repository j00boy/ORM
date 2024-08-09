package orm.orm_backend.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import orm.orm_backend.entity.Mountain;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MountainSimpleResponseDto {

    private Integer id;
    private String name;
    private Float height;
    private String addressLatitude;
    private String addressLongitude;

    @Builder
    public MountainSimpleResponseDto(Mountain mountain) {
        this.id = mountain.getId();
        this.name = mountain.getMountainName();
        this.height = mountain.getAltitude();
        this.addressLatitude = mountain.getAddressLatitude();
        this.addressLongitude = mountain.getAddressLongitude();
    }
}
