package cleanbook.com.jwt;

import cleanbook.com.exception.Response;
import cleanbook.com.exception.exceptions.NoAuthroizationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

   @Override
   public void commence(HttpServletRequest request,
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException {

      // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
      response.resetBuffer();
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.setContentType("application/json; charset=UTF-8");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().print(new ObjectMapper().writeValueAsString(new Response("인증이 필요합니다.")));
      response.flushBuffer();

   }
}
