package orm.orm_backend.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import orm.orm_backend.entity.Mountain;
import orm.orm_backend.entity.Trail;

import java.util.ArrayList;
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

    private List<Trail> trails = new ArrayList<>();

    public MountainResponseDto(Integer id, String name, String address, String code, String imageSrc, String desc, Float height, List<Trail> trails) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.code = code;
        this.imageSrc = imageSrc;
        this.desc = desc;
        this.height = height;
        this.trails = trails;
    }

    public MountainResponseDto(Mountain mountain, List<Trail> trails) {
        this.id = mountain.getId();
        this.name = mountain.getMountainName();
        this.address = mountain.getAddress();
        this.code = mountain.getMountainCode();
        this.imageSrc = mountain.getImageSrc();
        this.desc = mountain.getDescription();
        this.height = mountain.getAltitude();
        this.trails = trails;
    }
}
