package cleanbook.com.dto.chat;

import lombok.Data;

import java.util.List;

@Data
public class ChatroomForm {
    private String name;
    private List<Long> userIdList;
}
