package cleanbook.com.entity.chat;

import cleanbook.com.entity.Timestamped;
import cleanbook.com.entity.user.User;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cleanbook.com.entity.chat.UserChatroom.createUserChatroom;

@Entity
@Getter
@NoArgsConstructor
@Slf4j
public class Chatroom extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    private Long id;

    @CreatedDate
    private LocalDateTime modifedDate;

    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL)
    private List<Chat> chatList = new ArrayList<>();

    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL)
    private List<UserChatroom> userChatroomList = new ArrayList<>();

    public static Chatroom createChatroom(String name, List<User> userList) {
        Chatroom chatroom = new Chatroom();
        for (User user : userList) {
            chatroom.getUserChatroomList().add(createUserChatroom(user, chatroom, name));
        }
        return chatroom;
    }

    public void modify(LocalDateTime modifedDate) {
        log.info("data modified from {} to {}" ,this.modifedDate,modifedDate);
        this.modifedDate = modifedDate;
    }

}
