package cleanbook.com.dto.chat;

import cleanbook.com.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {
    private UserDto userDto;
    private String message;
    private LocalDateTime createdDate;
}
