package cleanbook.com.dto.chat;

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

    private Long roomId;
    private String sender;
    private String message;
    private LocalDateTime createdDate;

    public ChatMessage(Chat chat) {
        this.roomId = chat.getChatRoom().getId();
        this.sender = chat.getUser().getId().toString();
        this.message = chat.getMessage();
        this.createdDate = chat.getCreatedDate();
    }
}
