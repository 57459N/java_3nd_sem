package bsu.rfct.course2.group9.indyukov.java_3rd_sem;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChatMessage {
    // Текст сообщения
    private String message;
    // Автор сообщения
    private ChatUser author;
    // Временная метка сообщения (в микросекундах)
    private long timestamp;

    private ChatUser onlyFor = null;

    public ChatMessage(String message, ChatUser author, long timestamp, ChatUser onlyFor) {
        super();
        this.message = message;
        this.author = author;
        this.timestamp = timestamp;
        this.onlyFor = onlyFor;
    }


}
