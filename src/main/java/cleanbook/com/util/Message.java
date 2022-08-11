package cleanbook.com.util;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String type;
    private String sender;
    private String channelId;
    private String data;

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void newConnect() {
        this.type = "new";
    }

    public void closeConnect() {
        this.type = "close";
    }
}
