package cleanbook.com.controller;

import cleanbook.com.repository.user.UserRepository;
import cleanbook.com.service.AwsS3Service;
import cleanbook.com.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;

    @ResponseBody
    @GetMapping("/check_auth")
    public String hello() {
        return "hello";
    }

    @ResponseBody
    @PostMapping("/upload")
    public String uploadFile(
            @RequestParam("category") String category,
            @RequestPart(value = "file") MultipartFile multipartFile) {
        return awsS3Service.uploadFile(category, multipartFile);
    }

    @ResponseBody
    @PostMapping("/delete")
    public String deleteFile(@RequestParam String fileName) {
        awsS3Service.deleteFile(fileName);
        return "success";
    }
}

