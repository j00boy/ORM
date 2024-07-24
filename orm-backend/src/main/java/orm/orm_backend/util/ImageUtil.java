package orm.orm_backend.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Component
public class ImageUtil {

    private final String UPLOAD_DIR = "src/main/resources/static/uploads/image";
    private final String PREFIX_DIR = "src/main/resources/static";

    // 이미지 파일을 저장하는 메서드
    public String saveImage(MultipartFile image, String directoryPath) {
        // 파일 이름 생성
        String fileName = UUID.randomUUID().toString().replace("-", "") + "_" + image.getOriginalFilename();
        // 실제 파일이 저장될 경로
        String filePath = UPLOAD_DIR + directoryPath + "/" + fileName;

        // DB에 저장할 경로 문자열
        String dbFilePath = "/uploads/image" + directoryPath + "/" +fileName;

        Path path = Paths.get(filePath); // Path 객체 생성

        try {
            // 디렉토리 없다면 생성
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
            }
            Files.write(path, image.getBytes()); // 디렉토리에 파일 저장
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return dbFilePath;
    }

    public void deleteImage(String fileName) {
        fileName = PREFIX_DIR + fileName;
        File file = new File(fileName);
        if (!file.delete()) {
            throw new IllegalArgumentException(fileName + "의 이름으로 된 파일이 존재하지 않습니다.");
        }
    }

    public void deleteImages(List<String> fileNames) {
        fileNames.forEach(this::deleteImage);
    }
}
