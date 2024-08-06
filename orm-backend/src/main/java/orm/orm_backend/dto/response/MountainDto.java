package orm.orm_backend.dto.response;

import lombok.Builder;
import lombok.Data;
import orm.orm_backend.entity.Mountain;

@Data
public class MountainDto {

    private Integer id;
    private String name;
    private String address;
    private String addressLatitude;
    private String addressLongitude;
    private String code;
    private String imageSrc;
    private String desc;
    private Float height;

    @Builder
    public MountainDto(Mountain mountain) {
        this.id = mountain.getId();
        this.name = mountain.getMountainName();
        this.address = mountain.getAddress();
        this.addressLatitude = mountain.getAddressLatitude();
        this.addressLongitude = mountain.getAddressLongitude();
        this.code = mountain.getMountainCode();
        this.imageSrc = mountain.getImageSrc();
        this.desc = mountain.getDescription();
        this.height = mountain.getAltitude();
    }
}
