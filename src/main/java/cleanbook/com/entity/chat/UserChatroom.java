package cleanbook.com.entity.chat;

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

    private String name;
    private Integer uncheckedChatCount = 0;
    private Boolean online = false;

    public static UserChatroom createUserChatroom(User user, Chatroom chatroom, String chatroomName) {
        UserChatroom userChatroom = new UserChatroom();
        userChatroom.user = user;
        userChatroom.chatroom = chatroom;
        userChatroom.name = chatroomName;
        userChatroom.uncheckedChatCount = 0;
        userChatroom.online = false;
        user.getUserChatroomList().add(userChatroom);
        return userChatroom;
    }

    public void changeChatroomName(String chatroomName) {
        this.name = chatroomName;
    }

    public void addUncheckedChatCount() {
        this.uncheckedChatCount++;
    }

    public void resetUncheckedChatCount() {
        this.uncheckedChatCount = 0;
    }

    public void subscribeChatroom() {
        this.online = true;
    }

    public void unsubscribeChatroom() {
        this.online = false;
    }
}
