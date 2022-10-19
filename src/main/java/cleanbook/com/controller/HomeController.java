package cleanbook.com.controller;

import cleanbook.com.repository.user.UserRepository;
import cleanbook.com.service.AwsS3Service;
import cleanbook.com.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @ResponseBody
    @GetMapping("/check_auth")
    public String hello() {
        return "hello";
    }


}

