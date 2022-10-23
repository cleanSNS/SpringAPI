package cleanbook.com.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomDto {
    private Long chatroomId;
    private String name;
    private List<String> userImgUrlList;
    private int headCount;
    private String lastChat;
    private int uncheckedChatCount;
}
