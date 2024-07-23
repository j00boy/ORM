package orm.orm_backend.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import orm.orm_backend.entity.Mountain;
import orm.orm_backend.entity.Trail;

import java.util.ArrayList;
import java.util.List;

@Data
public class MountainResponseDto {

    private Integer id;
    private String name;
    private String address;
    private String code;
    private String imageSrc;
    private String desc;
    private Float height;

    private List<Trail> trails = new ArrayList<>();

    @Builder
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
