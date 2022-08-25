package cleanbook.com.repository.chat;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.chat.ChatDto;
import cleanbook.com.entity.chat.Chat;
import cleanbook.com.entity.chat.QChat;
import cleanbook.com.exception.exceptions.NoMoreDataException;
import cleanbook.com.exception.exceptions.NoMorePageException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static cleanbook.com.entity.chat.QChat.chat;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    // 채팅방의 채팅, 최신순으로 100개씩
    @Transactional
    public ResultDto<List<ChatDto>> readChatList(Long chatRoomId, Long startId, int pageSize) {

        List<Chat> chatList = queryFactory.query()
                .select(chat)
                .from(chat)
                .where(chat.chatRoom.id.eq(chatRoomId), chat.id.loe(startId))
                .orderBy(chat.createdDate.desc())
                .limit(pageSize)
                .fetch();

        // 조회를 전부 완료했을때
        if (chatList.isEmpty()) throw new NoMoreDataException("채팅");

        Long nextStartId = chatList.stream().mapToLong(Chat::getId).min().getAsLong()-1;

        List<ChatDto> chatDtoList = chatList.stream()
                .map(c -> new ChatDto(c.getMessage(), c.getUser().getUserProfile().getNickname(), c.getCreatedDate()))
                .collect(Collectors.toList());

        return new ResultDto<>(chatDtoList, nextStartId);
    }
}
