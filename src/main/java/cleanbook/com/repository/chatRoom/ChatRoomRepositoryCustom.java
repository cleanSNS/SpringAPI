package cleanbook.com.repository.chatRoom;

import cleanbook.com.dto.chat.ChatRoomDto;

import java.util.List;

public interface ChatRoomRepositoryCustom {
    List<ChatRoomDto> readChatRoomList(Long userId);
}
