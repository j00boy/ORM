package orm.orm_backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {

    private final String PREFIX_DIR = "src/main/resources/static";

    @GetMapping
    public ResponseEntity<Resource> getImage(String fileName) {

        Resource resource = new FileSystemResource(PREFIX_DIR + fileName);
        if (!resource.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            Path path = Paths.get(PREFIX_DIR + fileName);
            HttpHeaders header = new HttpHeaders();
            header.add("Content-Type", Files.probeContentType(path));
            return new ResponseEntity<>(resource, header, HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
