package orm.orm_backend.dto.response;

import lombok.Builder;
import lombok.Data;
import orm.orm_backend.entity.Mountain;

import java.util.List;

@Data
@Builder
public class MountainResponseDto {

    private Integer id;
    private String name;
    private String address;
    private String code;
    private String imageSrc;
    private String desc;
    private Float height;

    private List<TrailResponseDto> trails;

    public static MountainResponseDto toMountainResponseDto(Mountain mountain, List<TrailResponseDto> trails) {
        return MountainResponseDto.builder()
                .id(mountain.getId())
                .name(mountain.getMountainName())
                .address(mountain.getAddress())
                .code(mountain.getMountainCode())
                .imageSrc(mountain.getImageSrc())
                .desc(mountain.getDescription())
                .height(mountain.getAltitude())
                .trails(trails)
                .build();
    }
}
