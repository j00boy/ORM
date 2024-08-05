package orm.orm_backend.dto.response;

import lombok.Builder;
import lombok.Data;
import orm.orm_backend.entity.Mountain;

import java.util.List;

@Data
public class MountainResponseDto {

    private Integer id;
    private String name;
    private String address;
    private String address_latitude;
    private String address_longitude;
    private String code;
    private String imageSrc;
    private String desc;
    private Float height;

    private List<TrailResponseDto> trails;

    @Builder
    public MountainResponseDto(Mountain mountain, List<TrailResponseDto> trails) {
        this.id = mountain.getId();
        this.name = mountain.getMountainName();
        this.address = mountain.getAddress();
        this.address_latitude = mountain.getAddress_latitude();
        this.address_longitude = mountain.getAddress_longitude();
        this.code = mountain.getMountainCode();
        this.imageSrc = mountain.getImageSrc();
        this.desc = mountain.getDescription();
        this.height = mountain.getAltitude();
        this.trails = trails;
    }
}
