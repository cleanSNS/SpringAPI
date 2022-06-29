package cleanbook.com.domain.chat;

import cleanbook.com.domain.Timestamped;
import cleanbook.com.domain.user.User;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Chat extends Timestamped {

    @Id @GeneratedValue
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    private String imgUrl;
}
