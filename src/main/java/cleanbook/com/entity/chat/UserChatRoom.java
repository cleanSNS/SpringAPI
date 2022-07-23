package cleanbook.com.entity.chat;

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

    void setUser(User user) {
        this.user = user;
        user.getUserChatRoomList().add(this);
    }

    void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        chatRoom.getUserChatRoomList().add(this);
    }
}
