package orm.orm_backend.dto.response;

import lombok.Builder;
import lombok.Data;
import orm.orm_backend.entity.Mountain;

@Data
@Builder
public class MountainDto {

    private Integer id;
    private String name;
    private String address;
    private String code;
    private String imageSrc;
    private String desc;
    private Float height;

    public static MountainDto toMountainDto(Mountain mountain) {
        return MountainDto.builder()
                .id(mountain.getId())
                .name(mountain.getMountainName())
                .address(mountain.getAddress())
                .code(mountain.getMountainCode())
                .imageSrc(mountain.getImageSrc())
                .desc(mountain.getDescription())
                .height(mountain.getAltitude())
                .build();
    }
}
