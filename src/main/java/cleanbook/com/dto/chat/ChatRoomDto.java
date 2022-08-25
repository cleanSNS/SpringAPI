package cleanbook.com.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDto {
    private String name;
    private List<String> userList;
    private int headCount;
    private String lastChat;
}
