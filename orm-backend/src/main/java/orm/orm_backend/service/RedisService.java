package orm.orm_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
