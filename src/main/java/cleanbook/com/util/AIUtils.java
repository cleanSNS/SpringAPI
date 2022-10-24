package cleanbook.com.util;

import cleanbook.com.dto.AI.AIRequestDto;
import cleanbook.com.dto.AI.AIResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
public class AIUtils {

    private final static String AI_SERVER = "http://15.165.69.10:8000";

    // 인공지능 욕설 필터링
    public static String filterContent(String content) {
        AIResponseDto aiResponseDto = WebClient.create()
                .post()
                .uri(AI_SERVER)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new AIRequestDto(content))
                .retrieve()
                .bodyToMono(AIResponseDto.class)
                .block();
        String filteredContent = aiResponseDto.getRes();
        log.info("content : {}", content);
        log.info("filteredContent : {}", filteredContent);
        return filteredContent;
    }
}
