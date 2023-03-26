package netty.book.protocol;

import lombok.Data;
import lombok.EqualsAndHashCode;
import netty.book.protocol.command.Command;

/**
 * 登录请求类
 *
 * @author FangYuan
 * @since 2023-03-26 20:19:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginRequestPacket extends Packet {

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    @Override
    public Command getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
