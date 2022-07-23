package cleanbook.com.entity.chat;

import cleanbook.com.entity.Timestamped;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
public class ChatRoom extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    private String title;

    @OneToMany(mappedBy = "chatRoom")
    private List<Chat> chatList;

    @OneToMany(mappedBy = "chatRoom")
    private List<UserChatRoom> userChatRoomList;

    void setTitle(String title) {
        this.title = title;
    }
}
