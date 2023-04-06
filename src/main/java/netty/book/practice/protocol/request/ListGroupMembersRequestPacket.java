package netty.book.practice.protocol.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import netty.book.practice.protocol.Packet;
import netty.book.practice.protocol.command.Command;

/**
 * 获取群组成员列表请求
 *
 * @author FangYuan
 * @since 2023-04-06 20:24:23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ListGroupMembersRequestPacket extends Packet {

    private String groupId;

    @Override
    public Command getCommand() {
        return Command.LIST_MEMBERS_REQUEST;
    }
}
