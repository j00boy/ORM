package orm.orm_backend.dto.response;

import lombok.Builder;
import lombok.Data;
import orm.orm_backend.entity.Mountain;

@Data
public class SearchMountainResponseDto {

    private Integer id;
    private String name;
    private String address;
    private String code;
    private String imageSrc;
    private String desc;
    private Float height;

    @Builder
    public SearchMountainResponseDto(Mountain mountain) {
        this.id = mountain.getId();
        this.name = mountain.getMountainName();
        this.address = mountain.getAddress();
        this.code = mountain.getMountainCode();
        this.imageSrc = mountain.getImageSrc();
        this.desc = mountain.getDescription();
        this.height = mountain.getAltitude();
    }
}
