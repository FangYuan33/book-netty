package netty.book.practice.protocol.login;

import lombok.Data;
import lombok.EqualsAndHashCode;
import netty.book.practice.protocol.Packet;
import netty.book.practice.protocol.command.Command;

import static netty.book.practice.protocol.command.Command.LOGIN_RESPONSE;

/**
 * 客户端登录Response对象
 *
 * @author FangYuan
 * @since 2023-03-28 20:44:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginResponsePacket extends Packet {

    private boolean success;

    private String reason;

    @Override
    public Command getCommand() {
        return LOGIN_RESPONSE;
    }
}
