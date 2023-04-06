package netty.book.practice.protocol.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import netty.book.practice.protocol.Packet;
import netty.book.practice.protocol.command.Command;

/**
 * 退出群组请求
 *
 * @author FangYuan
 * @since 2023-04-06 19:54:38
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuitGroupRequestPacket extends Packet {

    /**
     * 群组ID
     */
    private String groupId;

    @Override
    public Command getCommand() {
        return Command.QUIT_GROUP_REQUEST;
    }
}
