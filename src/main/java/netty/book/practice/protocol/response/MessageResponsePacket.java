package netty.book.practice.protocol.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import netty.book.practice.protocol.Packet;
import netty.book.practice.protocol.command.Command;

/**
 * 服务端回复客户端的消息对象
 *
 * @author FangYuan
 * @since 2023-03-28 21:14:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MessageResponsePacket extends Packet {

    private String message;

    @Override
    public Command getCommand() {
        return Command.MESSAGE_RESPONSE;
    }
}
