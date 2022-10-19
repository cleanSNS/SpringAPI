package cleanbook.com.dto.chat;

import cleanbook.com.dto.user.UserDto;
import cleanbook.com.entity.chat.Chat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
    private Long userId;
    private String message;
    private LocalDateTime createdDate;
}
