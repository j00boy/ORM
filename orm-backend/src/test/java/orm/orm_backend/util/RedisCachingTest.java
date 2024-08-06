package orm.orm_backend.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;
import orm.orm_backend.service.MountainService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class RedisCachingTest {

    @Autowired
    private MountainService mountainService;

    @Test
    void cachingTest() throws Exception {
        StopWatch stopWatch = new StopWatch();

        int mountainId = 655;

        stopWatch.start("first call");
        mountainService.getMountainDtoById(mountainId);
        stopWatch.stop();

        stopWatch.start("second call");
        mountainService.getMountainDtoById(mountainId);
        stopWatch.stop();

        log.info(stopWatch.prettyPrint());

        assertThat(stopWatch.getTotalTimeMillis()).isGreaterThan(0);
        assertThat(stopWatch.getTaskCount()).isEqualTo(2);

        long firstCallTime = stopWatch.getTaskInfo()[0].getTimeMillis();
        long secondCallTime = stopWatch.getTaskInfo()[1].getTimeMillis();

        log.info("first call time : {}", firstCallTime);
        log.info("second call time : {}", secondCallTime);

        assertThat(secondCallTime).isLessThan(firstCallTime);
    }
}
