package cleanbook.com.controller;

import cleanbook.com.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AwsS3Controller {

    private final AwsS3Service awsS3Service;

    @PostMapping("/upload")
    public List<String> uploadFile(
            @RequestParam("category") String category,
            @RequestPart(value = "file") List<MultipartFile> multipartFiles) {
        return awsS3Service.uploadFiles(category, multipartFiles);
    }
}



