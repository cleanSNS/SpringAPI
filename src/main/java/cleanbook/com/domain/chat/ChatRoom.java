package cleanbook.com.domain.chat;

import cleanbook.com.domain.Timestamped;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
public class ChatRoom extends Timestamped {

    @Id @GeneratedValue
    @Column(name = "chat_room_id")
    private Long id;

    private String title;

    @OneToMany(mappedBy = "chatRoom")
    private List<Chat> chatList;

    @OneToMany(mappedBy = "chatRoom")
    private List<UserChatRoom> userChatRoomList;
}
