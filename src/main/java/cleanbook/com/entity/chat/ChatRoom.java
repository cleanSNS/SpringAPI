package cleanbook.com.entity.chat;

import cleanbook.com.entity.Timestamped;
import cleanbook.com.entity.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cleanbook.com.entity.chat.UserChatRoom.createUserChatRoom;

@Entity
@Getter
@NoArgsConstructor
@Slf4j
public class ChatRoom extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    private String name;

    @CreatedDate
    private LocalDateTime modifedDate;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<Chat> chatList = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<UserChatRoom> userChatRoomList = new ArrayList<>();

    public static ChatRoom createChatRoom(String name, List<User> userList) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.name = name;
        for (User user : userList) {
            chatRoom.getUserChatRoomList().add(createUserChatRoom(user, chatRoom));
        }
        return chatRoom;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void modify(LocalDateTime modifedDate) {
        log.info("data modified from {} to {}" ,this.modifedDate,modifedDate);
        this.modifedDate = modifedDate;
    }


    public ChatRoom(String name) {
        this.name = name;
    }
}
