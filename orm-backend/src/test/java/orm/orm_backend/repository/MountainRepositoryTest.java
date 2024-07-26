package orm.orm_backend.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;
import orm.orm_backend.dto.request.MountainSearchRequestDto;
import orm.orm_backend.entity.Mountain;

@DataJpaTest
@AutoConfigureTestDatabase
@Slf4j
public class MountainRepositoryTest {

    @Autowired
    private MountainRepository mountainRepository;

    private Mountain mountain;
    private MountainSearchRequestDto mountainSearchRequestDto;

    @BeforeEach
    public void setUp() {
        System.err.println("=======SetUp=======");
        mountain = Mountain.builder()
                .mountainName("테스트산")
                .mountainCode("12345678")
                .address("테스트도 테스트시 테스트구")
                .imageSrc("test.com/upload/test")
                .altitude(200F)
                .description("테스트 설명")
                .build();

        mountainSearchRequestDto = MountainSearchRequestDto.builder()
                .pgno(1)
                .recordSize(2)
                .keyword("테스트")
                .build();
    }

    @Test
    @DisplayName("검색으로 산 가져오기")
    

}
