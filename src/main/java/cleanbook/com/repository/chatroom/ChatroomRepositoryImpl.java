package cleanbook.com.repository.chatroom;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.chat.ChatroomDto;
import cleanbook.com.entity.chat.Chat;
import cleanbook.com.entity.chat.Chatroom;
import cleanbook.com.entity.chat.UserChatroom;
import cleanbook.com.exception.exceptions.NotFoundException;
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
    private final UserChatroomRepository userChatroomRepository;

    @Transactional
    public ResultDto<List<ChatroomDto>> readChatroomList(Long userId) {
        // 채팅방을 최신순으로 얻기
        List<Chatroom> chatroomList = queryFactory.query()
                .select(chatroom)
                .from(userChatroom)
                .where(userChatroom.user.id.eq(userId))
                .join(userChatroom.chatroom, chatroom)
                .orderBy(chatroom.modifedDate.desc())
                .fetch();

        List<ChatroomDto> chatroomDtoList = new ArrayList<>();
        for (Chatroom chatroom : chatroomList) {
            List<String> userImgUrlList = chatroom.getUserChatroomList().stream()
                    .map(u -> u.getUser().getUserProfile().getImgUrl())
                    .collect(Collectors.toList());
            int headCount = chatroom.getUserChatroomList().size();

            UserChatroom userChatroom = userChatroomRepository.findByUser_IdAndChatroom_Id(userId, chatroom.getId())
                                                                .orElseThrow(() -> new NotFoundException("채팅방"));
            chatroomDtoList.add(new ChatroomDto(chatroom.getId(), userChatroom.getName(), userImgUrlList, headCount,
                                                getLastChat(chatroom.getId()), userChatroom.getUncheckedChatCount()));
        }

        return new ResultDto<>(chatroomDtoList);
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
