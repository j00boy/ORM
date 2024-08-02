package orm.orm_backend.learningtest;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@TestPropertySource(locations = "classpath:test-application.properties")
@ExtendWith(SpringExtension.class)
@Slf4j
public class NginxImageTest {

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

    @Test
    void imageUploadTest() throws IOException {
        MultipartFile imageFile = readTestImage();
        Assertions.assertThat(imageFile).isNotNull();
        log.info("original fileName={}", imageFile.getOriginalFilename());
        log.info("name={}", imageFile.getName());

        try (InputStream inputStream = imageFile.getInputStream();
             InputStream pemInputStream = new FileInputStream(PEM_FILE_PATH)) {
            JSch jsch = new JSch();
            jsch.addIdentity("I11A709T.pem", pemInputStream.readAllBytes(), null, null);
            Session session = jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();

            // 디렉토리 존재 여부 확인 및 생성
            try {
                sftpChannel.stat(REMOTE_UPLOAD_DIR + "newUser/");
            } catch (SftpException e) {
                sftpChannel.mkdir(REMOTE_UPLOAD_DIR + "newUser/");
            }

            sftpChannel.put(inputStream, REMOTE_UPLOAD_DIR + "newUser/" + imageFile.getName());

            sftpChannel.disconnect();
            session.disconnect();
        } catch (JSchException | SftpException e) {
            throw new RuntimeException(e);
        }
    }

    private MultipartFile readTestImage() throws IOException {
        Resource resource = new ClassPathResource("static/test.png");

        try (InputStream inputStream = resource.getInputStream()) {
            return new MockMultipartFile(
                    "newFile.png",
                    "test.png",
                    "image/png",
                    StreamUtils.copyToByteArray(inputStream));
        }
    }
}
