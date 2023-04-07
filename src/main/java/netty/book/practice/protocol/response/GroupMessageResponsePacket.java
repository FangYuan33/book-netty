package netty.book.practice.protocol.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import netty.book.practice.protocol.Packet;
import netty.book.practice.protocol.command.Command;

/**
 * 群组消息响应对象
 *
 * @author FangYuan
 * @since 2023-04-07 20:24:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupMessageResponsePacket extends Packet {

    private String fromGroupId;

    private String fromUser;

    private String message;

    @Override
    public Command getCommand() {
        return Command.SEND_GROUP_MESSAGE_RESPONSE;
    }
}
