package netty.book.practice.protocol.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import netty.book.practice.protocol.Packet;
import netty.book.practice.protocol.command.Command;

/**
 * 退出群组的响应
 *
 * @author FangYuan
 * @since 2023-04-06 20:02:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuitGroupResponsePacket extends Packet {

    private String groupId;

    private boolean success;

    private String reason;

    @Override
    public Command getCommand() {
        return Command.QUIT_GROUP_RESPONSE;
    }
}
