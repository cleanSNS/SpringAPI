package cleanbook.com.repository.chat;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.chat.ChatDto;
import cleanbook.com.entity.chat.Chat;

import java.util.List;

public interface ChatRepositoryCustom {
    ResultDto<List<ChatDto>> readChatList(Long chatRoomId, Long startId, int pageSize);
}
