package cleanbook.com.entity.chat;

import cleanbook.com.entity.Timestamped;
import cleanbook.com.entity.user.User;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class UserChatRoom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_chat_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    public static UserChatRoom createUserChatRoom(User user, ChatRoom chatRoom) {
        UserChatRoom userChatRoom = new UserChatRoom();
        userChatRoom.user = user;
        userChatRoom.chatRoom = chatRoom;
        user.getUserChatRoomList().add(userChatRoom);
        return userChatRoom;
    }
}
