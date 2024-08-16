package orm.orm_backend.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@TestPropertySource(locations = "classpath:test-application.properties")
@ExtendWith(SpringExtension.class)
class ImageUtilTest {

    private final String UPLOAD_DIR = "src/main/resources/static/uploads/image";

    static MultipartFile image;

    @InjectMocks
    ImageUtil imageUtil;

    @Value("${orm.sftp.host}")
    private String SFTP_HOST;

    @Value("${orm.sftp.port}")
    private int SFTP_PORT;

    @Value("${orm.sftp.user}")
    private String SFTP_USER;

    @Value("${orm.sftp.pem-file-path}")
    private String PEM_FILE_PATH;

    @Value("${orm.sftp.remote-upload-dir}")
    private String REMOTE_UPLOAD_DIR;

    @Value("${orm.sftp.remote-access-dir}")
    private String REMOTE_ACCESS_DIR;

    private String fileName;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(imageUtil, "SFTP_HOST", SFTP_HOST);
        ReflectionTestUtils.setField(imageUtil, "SFTP_PORT", SFTP_PORT);
        ReflectionTestUtils.setField(imageUtil, "SFTP_USER", SFTP_USER);
        ReflectionTestUtils.setField(imageUtil, "PEM_FILE_PATH", PEM_FILE_PATH);
        ReflectionTestUtils.setField(imageUtil, "REMOTE_UPLOAD_DIR", REMOTE_UPLOAD_DIR);
        ReflectionTestUtils.setField(imageUtil, "REMOTE_ACCESS_DIR", REMOTE_ACCESS_DIR);
    }

    @BeforeAll
    static void beforeAll() throws IOException {
        image = readImage("static/", "test.png", "png");
//        image = readImage("static/uploads/image/test/", "fef2cd35d23d4d16a44a39d090e8f1ae_test.png", "png");
    }

    // 눈 테스트 하기
    @Test
    void saveImageTest() throws IOException, InterruptedException {
        log.info("remoteUploadDir={}", imageUtil.getREMOTE_UPLOAD_DIR());
        String directory = "test/";
        String filePath = imageUtil.saveImage(image, directory);
        log.info("filePath={}", filePath);
    }

    // fileName은 저장 후 이름을 직접넣어줘야 한다.
    @Test
    void deleteTest() throws IOException {
        String fileName = "http://i11A709.p.ssafy.io/files/orm/test/f193052a9c2548499749ed463bbad701_test.png";
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