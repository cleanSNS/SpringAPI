package cleanbook.com.domain.chat;

import cleanbook.com.domain.user.User;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class UserChatRoom {

    @Id @GeneratedValue
    @Column(name = "user_chat_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;
}
