package cleanbook.com.entity.chat;

import cleanbook.com.entity.Timestamped;
import cleanbook.com.entity.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Chat extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private Chatroom chatroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotEmpty
    @Column(columnDefinition = "MEDIUMTEXT")
    private String message;

    @OneToMany(mappedBy = "chat")
    private List<ChatImgUrl> chatImgUrlList = new ArrayList<>();

    public static Chat createChat(Chatroom chatroom, User user, String message, LocalDateTime createdDate) {
        Chat chat = new Chat();
        chat.chatroom = chatroom;
        chat.user = user;
        chat.message = message;
        chat.setCreatedDate(createdDate);
        chatroom.getChatList().add(chat);
        chatroom.modify(LocalDateTime.now());

        return chat;
    }
}
