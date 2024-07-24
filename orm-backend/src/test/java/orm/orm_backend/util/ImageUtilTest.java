package orm.orm_backend.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class ImageUtilTest {

    private final String UPLOAD_DIR = "src/main/resources/static/uploads/image";

    static MultipartFile image;
    ImageUtil imageUtil = new ImageUtil();
    private String fileName;

    @BeforeAll
    static void beforeAll() throws IOException {
        image = readImage("static/", "test.png", "png");
//        image = readImage("static/uploads/image/test/", "fef2cd35d23d4d16a44a39d090e8f1ae_test.png", "png");
    }

    // 눈 테스트 하기
    @Test
    void saveImageTest() throws IOException, InterruptedException {
        String directory = "/test";
        assertThat(image).isNotNull();
        String fileName = imageUtil.saveImage(image, directory);
        String[] tmp = fileName.split("/");
        fileName = tmp[tmp.length - 1];
    }

    // fileName은 저장 후 이름을 직접넣어줘야 한다.
    @Test
    void deleteTest() throws IOException {
        String fileName = "/uploads/image/test/b50d02f475e143f38f04a89f9230cf77_test.png";
        imageUtil.deleteImage(fileName);
    }

    private static MultipartFile readImage(String directory, String fileName, String type) throws IOException {
        Resource resource = new ClassPathResource(directory + fileName);

        try (InputStream inputStream = resource.getInputStream()) {
            return new MockMultipartFile(
                    "file",
                    fileName,
                    "image/" + type,
                    StreamUtils.copyToByteArray(inputStream));
        }
    }
}