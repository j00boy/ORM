package orm.orm_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> void addAllList(String key, List<T> list)  {
        try {
            String serializedList = objectMapper.writeValueAsString(list);
            redisTemplate.opsForValue().set(key, serializedList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> getList(String key) {
        String value = (String) redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.readValue(
                    value, new TypeReference<>() {
                    }
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveObject(String key, Object object) {
        try {
            String serializedObject = objectMapper.writeValueAsString(object);
            redisTemplate.opsForValue().set(key, serializedObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public <T> T getObject(String key, Class<T> type) {
        String value = (String) redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.readValue(value, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveObjectWithExpirationAt3AM(String key, Object object) {
        // 현재 시간과 새벽 3시까지 남은 시간을 계산
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next3AM = now.toLocalDate().atTime(LocalTime.of(3, 0));

        // 현재 시간이 새벽 3시 이후인 경우, 다음날 새벽 3시로 설정
        if (now.isAfter(next3AM)) {
            next3AM = next3AM.plusDays(1);
        }

        long secondsUntil3AM = Duration.between(now, next3AM).getSeconds();

        // TTL을 새벽 3시까지 남은 시간으로 설정
        try {
            String serializedObject = objectMapper.writeValueAsString(object);
            redisTemplate.opsForValue().set(key, serializedObject, secondsUntil3AM, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
