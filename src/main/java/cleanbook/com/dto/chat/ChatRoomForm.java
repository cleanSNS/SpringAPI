package cleanbook.com.dto.chat;

import lombok.Data;

import java.util.List;

@Data
public class ChatRoomForm {
    private String name;
    private List<Long> userIdList;
}
