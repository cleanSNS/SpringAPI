package cleanbook.com.service;

import cleanbook.com.util.MailUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
@EnableAsync
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private static final String LOCAL = "http://localhost:8080/local";
    private static final String SERVER = "http://api.cleanbook.site";

    @Async
    public void sendServer(String email, String authToken) throws MessagingException {
        MailUtils smm = new MailUtils(javaMailSender);
        smm.setTo(email);
        smm.setSubject("CleanBook 회원가입 이메일 인증");
        smm.setText(
                "<h1>메일인증</h1>" +
                        "<br/>CleanBook에 회원가입해주셔서 감사합니다."+
                        "<br/>아래 [이메일 인증 확인]을 눌러주세요."+
                        "<br/><a href=" + SERVER + "/user/auth/signup/confirm?email="+email+"&authToken="+authToken+
                        " target='_blenk'>이메일 인증 확인</a>");

        smm.send();
    }

    @Async
    public void sendLocal(String email, String authToken) throws MessagingException {
        MailUtils smm = new MailUtils(javaMailSender);
        smm.setTo(email);
        smm.setSubject("CleanBook 회원가입 이메일 인증");
        smm.setText(
                "<h1>메일인증</h1>" +
                        "<br/>CleanBook에 회원가입해주셔서 감사합니다."+
                        "<br/>아래 [이메일 인증 확인]을 눌러주세요."+
                        "<br/><a href="+LOCAL+"/user/auth/signup/confirm?email="+email+"&authToken="+authToken+
                        " target='_blenk'>이메일 인증 확인</a>");

        smm.send();
    }

    @Async
    public void sendNewPassword(String email, String password) throws MessagingException {
        MailUtils smm = new MailUtils(javaMailSender);
        smm.setTo(email);
        smm.setSubject("CleanBook 비밀번호 초기화");
        smm.setText(
                "<h1>비밀번호 초기화</h1>" +
                        "<br/>아래의 비밀번호로 로그인해주시기 바랍니다."+
                        "<br/>[" + password + "]");

        smm.send();
    }
}