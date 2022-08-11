package cleanbook.com.entity.chat;

import cleanbook.com.entity.Timestamped;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "chatRoom")
    private List<Chat> chatList = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom")
    private List<UserChatRoom> userChatRoomList = new ArrayList<>();

    public static ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.name = name;
        return chatRoom;
    }
}
