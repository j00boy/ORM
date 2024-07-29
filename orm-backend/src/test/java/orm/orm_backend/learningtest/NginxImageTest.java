package orm.orm_backend.learningtest;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class NginxImageTest {

    private static final String SFTP_HOST = "i11A709.p.ssafy.io";
    private static final int SFTP_PORT = 22;
    private static final String SFTP_USER = "ubuntu";
    private static final String PEM_FILE_PATH = "src/main/resources/I11A709T.pem";
    private static final String REMOTE_UPLOAD_DIR = "/home/upload/orm/";

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
