package cleanbook.com.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class ChatDto {
    private String sender;
    private String message;
    private LocalDateTime createdDate;

    public ChatDto() {
    }

    public ChatDto(String sender, String message, LocalDateTime createdDate) {
        this.sender = sender;
        this.message = message;
        this.createdDate = createdDate;
    }
}
