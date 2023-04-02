package netty.book.practice.protocol.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import netty.book.practice.protocol.Packet;
import netty.book.practice.protocol.command.Command;

import java.util.List;

/**
 * 创建群组响应对象
 *
 * @author FangYuan
 * @since 2023-04-02 22:55:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CreateGroupResponsePacket extends Packet {

    /**
     * 成功标志
     */
    private boolean success;

    /**
     * 群组ID
     */
    private String groupId;

    /**
     * 用户名们
     */
    private List<String> userNameList;

    @Override
    public Command getCommand() {
        return Command.CREATE_GROUP_RESPONSE;
    }
}
