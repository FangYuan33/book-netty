package netty.book.practice.protocol.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import netty.book.practice.protocol.Packet;
import netty.book.practice.protocol.command.Command;

/**
 * 服务端回复客户端的消息对象
 *
 * @author FangYuan
 * @since 2023-03-28 21:14:10
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class MessageResponsePacket extends Packet {

    public MessageResponsePacket(String message) {
        this.message = message;
    }

    /**
     * 是否发送成功
     */
    private boolean success;

    private String message;

    private String fromUserId;

    private String fromUserName;

    @Override
    public Command getCommand() {
        return Command.MESSAGE_RESPONSE;
    }
}
