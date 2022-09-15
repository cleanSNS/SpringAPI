package cleanbook.com.entity.chat;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class ChatImgUrl {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_img_url")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @Column(columnDefinition = "TEXT")
    private String url;

    void setChat(Chat chat) {
        this.chat = chat;
        chat.getChatImgUrlList().add(this);
    }

    void setUrl(String url) {
        this.url = url;
    }
}
