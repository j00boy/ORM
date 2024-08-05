package orm.orm_backend.dto.response;

import lombok.Builder;
import lombok.Data;
import orm.orm_backend.entity.Mountain;

@Data
public class MountainDto {

    private Integer id;
    private String name;
    private String address;
    private String address_latitude;
    private String address_longitude;
    private String code;
    private String imageSrc;
    private String desc;
    private Float height;

    @Builder
    public MountainDto(Mountain mountain) {
        this.id = mountain.getId();
        this.name = mountain.getMountainName();
        this.address = mountain.getAddress();
        this.address_latitude = mountain.getAddress_latitude();
        this.address_longitude = mountain.getAddress_longitude();
        this.code = mountain.getMountainCode();
        this.imageSrc = mountain.getImageSrc();
        this.desc = mountain.getDescription();
        this.height = mountain.getAltitude();
    }
}
