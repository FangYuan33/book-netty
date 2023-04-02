package netty.book.practice.command.impl;

import io.netty.channel.Channel;
import netty.book.practice.command.ConsoleCommand;
import netty.book.practice.protocol.request.LoginRequestPacket;
import netty.book.practice.util.SessionUtil;

import java.util.Scanner;
import java.util.UUID;

/**
 * 登录命令
 *
 * @author FangYuan
 * @since 2023-04-02 22:18:49
 */
public class LoginConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        if (!SessionUtil.hasLogin(channel)) {
            System.out.println("输入用户名登录: ");
            String userName = scanner.nextLine();

            // 初始化登录请求对象
            LoginRequestPacket loginRequestPacket = initialLoginRequest(userName);
            // 发送登录请求
            channel.writeAndFlush(loginRequestPacket);

            // 等待连接请求回复
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("已登录成功，无需重复登录");
        }
    }

    /**
     * 初始化登录请求
     */
    private static LoginRequestPacket initialLoginRequest(String userName) {
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserName(userName);
        loginRequestPacket.setPassword(UUID.randomUUID().toString());

        return loginRequestPacket;
    }
}
