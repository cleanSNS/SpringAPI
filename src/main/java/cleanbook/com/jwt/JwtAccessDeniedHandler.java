package cleanbook.com.jwt;

import cleanbook.com.exception.Response;
import cleanbook.com.exception.exceptions.NoAuthroizationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

   @Override
   public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {

      //필요한 권한이 없이 접근하려 할때 403
      response.resetBuffer();
      response.setStatus(HttpStatus.FORBIDDEN.value());
      response.setContentType("application/json; charset=UTF-8");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().print(new ObjectMapper().writeValueAsString(new Response("권한이 없습니다.")));
      response.flushBuffer();
   }
}
