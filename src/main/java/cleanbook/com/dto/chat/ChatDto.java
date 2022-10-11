package cleanbook.com.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class ChatDto {
    private String sender;
    private String message;
    private LocalDateTime createdDate;

}
