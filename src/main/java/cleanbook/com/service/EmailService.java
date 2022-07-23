package cleanbook.com.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private static final String LOCAL_HOST = "http://localhost:8080";
    private static final String SERVER = "http://localhost:8080";

    @Async
    public void send(String email, String authToken) {
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo(email);
        smm.setSubject("회원가입 이메일 인증");
        smm.setText(LOCAL_HOST + "/user/signup/confirm?email="+email+"&authToken="+authToken);

        javaMailSender.send(smm);
    }
}