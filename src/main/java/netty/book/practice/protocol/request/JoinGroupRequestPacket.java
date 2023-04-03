package netty.book.practice.protocol.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import netty.book.practice.protocol.Packet;
import netty.book.practice.protocol.command.Command;

/**
 * 加入群组请求
 *
 * @author FangYuan
 * @since 2023-04-03 20:35:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JoinGroupRequestPacket extends Packet {

    private String groupId;

    @Override
    public Command getCommand() {
        return Command.JOIN_GROUP_REQUEST;
    }
}
