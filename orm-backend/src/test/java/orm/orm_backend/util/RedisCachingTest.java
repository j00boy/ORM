package orm.orm_backend.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.StopWatch;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
public class RedisCachingTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void cachingTest() throws Exception {
        StopWatch stopWatch = new StopWatch();

        int mountainId = 655;

        stopWatch.start("first call");
        mockMvc.perform(MockMvcRequestBuilders.get("/mountains/{mountainId}", mountainId))
                .andExpect(MockMvcResultMatchers.status().isOk());
        stopWatch.stop();

        stopWatch.start("second call");
        mockMvc.perform(MockMvcRequestBuilders.get("/mountains/{mountainId}", mountainId))
                .andExpect(MockMvcResultMatchers.status().isOk());
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

    @Test
    void mountainSearchByNameTest() throws Exception {

        StopWatch stopWatch = new StopWatch();

        stopWatch.start("first call");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/mountains/search?name=고"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        stopWatch.stop();
        assertThat(result.getResponse().getContentAsString().isEmpty()).isFalse();

        stopWatch.start("second call");
        MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.get("/mountains/search?name=고"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        stopWatch.stop();
        assertThat(result2.getResponse().getContentAsString().isEmpty()).isFalse();
        log.info(stopWatch.prettyPrint());

        assertThat(stopWatch.getTotalTimeMillis()).isGreaterThan(0);
        assertThat(stopWatch.getTaskCount()).isEqualTo(2);

        long firstCallTime = stopWatch.getTaskInfo()[0].getTimeMillis();
        long secondCallTime = stopWatch.getTaskInfo()[1].getTimeMillis();

        assertThat(secondCallTime).isLessThan(firstCallTime);
    }

    @Test
    void mountainTop100Mountain() throws Exception {

        StopWatch stopWatch = new StopWatch();

        stopWatch.start("first call");
        mockMvc.perform(MockMvcRequestBuilders.get("/mountains/top"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        stopWatch.stop();

        stopWatch.start("second call");
        mockMvc.perform(MockMvcRequestBuilders.get("/mountains/top"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        stopWatch.stop();

        log.info(stopWatch.prettyPrint());

        assertThat(stopWatch.getTotalTimeMillis()).isGreaterThan(0);
        assertThat(stopWatch.getTaskCount()).isEqualTo(2);

        long firstCallTime = stopWatch.getTaskInfo()[0].getTimeMillis();
        long secondCallTime = stopWatch.getTaskInfo()[1].getTimeMillis();

        assertThat(secondCallTime).isLessThan(firstCallTime);
    }
}
