package orm.orm_backend.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase
@Slf4j
public class MountainRepositoryTest {

    @Autowired
    private MountainRepository mountainRepository;

    @BeforeEach


    @Test
    @DisplayName("검색으로 산 가져오기")
    public void getMountainByName() throws Exception {


    }
    
}
