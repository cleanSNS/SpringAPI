package cleanbook.com.repository.chatroom;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.chat.ChatroomDto;

import java.util.List;

public interface ChatroomRepositoryCustom {
    ResultDto<List<ChatroomDto>> readChatroomList(Long userId);
}
