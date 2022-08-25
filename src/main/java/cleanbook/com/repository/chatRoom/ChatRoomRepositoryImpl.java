package cleanbook.com.repository.chatRoom;

import cleanbook.com.dto.chat.ChatRoomDto;
import cleanbook.com.entity.chat.Chat;
import cleanbook.com.entity.chat.ChatRoom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cleanbook.com.entity.chat.QChat.chat;
import static cleanbook.com.entity.chat.QChatRoom.chatRoom;
import static cleanbook.com.entity.chat.QUserChatRoom.userChatRoom;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Transactional
    public List<ChatRoomDto> readChatRoomList(Long userId) {
        // 채팅방을 최신순으로 얻기
        List<ChatRoom> chatRoomList = queryFactory.query()
                .select(chatRoom)
                .from(userChatRoom)
                .where(userChatRoom.user.id.eq(userId))
                .join(userChatRoom.chatRoom, chatRoom)
                .orderBy(chatRoom.modifedDate.desc())
                .fetch();

        for (ChatRoom room : chatRoomList) {
            System.out.println("room.getId() = " + room.getId());
            System.out.println("room.getModifedDate() = " + room.getModifedDate());
        }

        List<ChatRoomDto> chatRoomDtoList = new ArrayList<>();
        for (ChatRoom chatRoom : chatRoomList) {
            List<String> userNickNameList = chatRoom.getUserChatRoomList().stream()
                    .map(u -> u.getUser().getUserProfile().getNickname())
                    .collect(Collectors.toList());
            int headCount = chatRoom.getUserChatRoomList().size();

            chatRoomDtoList.add(new ChatRoomDto(chatRoom.getName(), userNickNameList, headCount, getLastChat(chatRoom.getId())));
        }

        return chatRoomDtoList;
    }

    public String getLastChat(Long chatRoomId) {
        Chat lastChat = queryFactory.query()
                .select(chat)
                .from(chat)
                .where(chat.chatRoom.id.eq(chatRoomId))
                .orderBy(chat.id.desc())
                .limit(1)
                .fetchOne();

        return lastChat == null ? null : lastChat.getMessage();
    }
}
