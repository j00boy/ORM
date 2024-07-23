package orm.orm_backend.dto.response;

import lombok.Builder;
import lombok.Data;
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
    private Double height;
    private List<Trail> trails = new ArrayList<>();

}
