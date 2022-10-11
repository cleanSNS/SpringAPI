package cleanbook.com.repository.chatroom;

import cleanbook.com.dto.chat.ChatroomDto;
import cleanbook.com.entity.chat.Chat;
import cleanbook.com.entity.chat.Chatroom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cleanbook.com.entity.chat.QChat.chat;
import static cleanbook.com.entity.chat.QChatroom.chatroom;
import static cleanbook.com.entity.chat.QUserChatroom.userChatroom;

@Repository
@RequiredArgsConstructor
public class ChatroomRepositoryImpl implements cleanbook.com.repository.chatroom.ChatroomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Transactional
    public List<ChatroomDto> readChatroomList(Long userId) {
        // 채팅방을 최신순으로 얻기
        List<Chatroom> chatRoomList = queryFactory.query()
                .select(chatroom)
                .from(userChatroom)
                .where(userChatroom.user.id.eq(userId))
                .join(userChatroom.chatroom, chatroom)
                .orderBy(chatroom.modifedDate.desc())
                .fetch();


        List<ChatroomDto> chatRoomDtoList = new ArrayList<>();
        for (Chatroom chatroom : chatRoomList) {
            List<String> userNickNameList = chatroom.getUserChatroomList().stream()
                    .map(u -> u.getUser().getUserProfile().getNickname())
                    .collect(Collectors.toList());
            int headCount = chatroom.getUserChatroomList().size();

            chatRoomDtoList.add(new ChatroomDto(chatroom.getName(), userNickNameList, headCount, getLastChat(chatroom.getId())));
        }

        return chatRoomDtoList;
    }

    public String getLastChat(Long chatroomId) {
        Chat lastChat = queryFactory.query()
                .select(chat)
                .from(chat)
                .where(chat.chatroom.id.eq(chatroomId))
                .orderBy(chat.id.desc())
                .limit(1)
                .fetchOne();

        return lastChat == null ? null : lastChat.getMessage();
    }
}
