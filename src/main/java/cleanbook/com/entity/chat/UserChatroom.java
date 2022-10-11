package cleanbook.com.entity.chat;

import cleanbook.com.entity.Timestamped;
import cleanbook.com.entity.user.User;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class UserChatroom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_chat_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private Chatroom chatroom;

    public static UserChatroom createUserChatroom(User user, Chatroom chatroom) {
        UserChatroom userChatroom = new UserChatroom();
        userChatroom.user = user;
        userChatroom.chatroom = chatroom;
        user.getUserChatroomList().add(userChatroom);
        return userChatroom;
    }
}
