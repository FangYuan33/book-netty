package netty.book.practice.protocol.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import netty.book.practice.protocol.Packet;
import netty.book.practice.protocol.command.Command;

/**
 * 加入群组的响应
 *
 * @author FangYuan
 * @since 2023-04-03 20:37:20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JoinGroupResponsePacket extends Packet {

    private String groupId;

    private boolean success;

    private String message;

    private String reason;

    @Override
    public Command getCommand() {
        return Command.JOIN_GROUP_RESPONSE;
    }
}
