package orm.orm_backend.util;

import com.jcraft.jsch.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Component
@Getter
public class ImageUtil {

    private final String UPLOAD_DIR = "src/main/resources/static/uploads/image";
    private final String PREFIX_DIR = "src/main/resources/static";

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

    // 이미지 파일을 저장하는 메서드
    public String saveImage(MultipartFile image, String directoryPath) {
        // 파일 이름 생성
        String fileName = UUID.randomUUID().toString().replace("-", "") + "_" + image.getOriginalFilename();

        try (InputStream inputStream = image.getInputStream(); InputStream pemInputStream = new FileInputStream(PEM_FILE_PATH)) {
            // sftp를 지원하는 라이브러리
            JSch jsch = new JSch();
            // ssh 연결을 위해 클라이언트의 인증 정보를 추가하는 과정
            jsch.addIdentity("I11A709T.pem", pemInputStream.readAllBytes(), null, null);
            Session session = jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            // sftp 채널 오픈
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();

            // 디렉토리 존재 여부 확인 및 생성
            try {
                sftpChannel.stat(REMOTE_UPLOAD_DIR + directoryPath);
            } catch (SftpException e) {
                StringBuilder directory = new StringBuilder(REMOTE_UPLOAD_DIR);
                for (String path : directoryPath.split("/")) {
                    directory.append("/").append(path);
                    sftpChannel.mkdir(directory.toString());
                }
            }

            sftpChannel.put(inputStream, REMOTE_UPLOAD_DIR + directoryPath + fileName);

        } catch (IOException | JSchException | SftpException e) {
            throw new RuntimeException(e);
        }

        // DB에 저장할 경로 문자열
        return "http://" + SFTP_HOST + REMOTE_ACCESS_DIR + directoryPath + fileName;
    }

    public void deleteImage(String fileName) {
        fileName = fileName.replace("http://", "");
        fileName = fileName.replace(SFTP_HOST, "");
        fileName = fileName.replace(REMOTE_ACCESS_DIR, REMOTE_UPLOAD_DIR);
        try {
            JSch jsch = new JSch();
            jsch.addIdentity(PEM_FILE_PATH);
            Session session = jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();

            sftpChannel.rm(fileName);

            sftpChannel.disconnect();
            session.disconnect();
        } catch (JSchException | SftpException e) {
            throw new IllegalArgumentException(fileName + "의 이름으로 된 파일이 존재하지 않습니다.");
        }
    }

    public void deleteImages(List<String> fileNames) {
        fileNames.forEach(this::deleteImage);
    }
}
