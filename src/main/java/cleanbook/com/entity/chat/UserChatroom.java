package cleanbook.com.entity.chat;

import cleanbook.com.entity.user.User;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    private Integer uncheckedChatCount = 0;
    private LocalDateTime lastReadDate;
    private Boolean online = false;

    public static UserChatroom createUserChatroom(User user, Chatroom chatroom) {
        UserChatroom userChatroom = new UserChatroom();
        userChatroom.user = user;
        userChatroom.chatroom = chatroom;
        userChatroom.uncheckedChatCount = 0;
        userChatroom.online = false;
        user.getUserChatroomList().add(userChatroom);
        return userChatroom;
    }

    public void addUncheckedChatCount() {
        this.uncheckedChatCount++;
    }

    public void resetUncheckedChatCount() {
        this.uncheckedChatCount = 0;
        lastReadDate = LocalDateTime.now();
    }

    public void updateLastReadDate() {
        this.lastReadDate = LocalDateTime.now();
    }

    public void subscribeChatroom() {
        this.online = true;
    }

    public void unsubscribeChatroom() {
        this.online = false;
    }
}
