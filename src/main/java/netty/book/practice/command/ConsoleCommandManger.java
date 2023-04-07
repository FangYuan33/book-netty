package netty.book.practice.command;

import io.netty.channel.Channel;
import netty.book.practice.command.impl.*;
import netty.book.practice.util.SessionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 使用策略模式对控制台命令进行管理
 *
 * @author FangYuan
 * @since 2023-04-02 22:31:36
 */
public class ConsoleCommandManger {

    private final Map<String, ConsoleCommand> consoleCommandMap = new HashMap<>(4);

    public ConsoleCommandManger() {
        // 策略模式进行管理
        consoleCommandMap.put("sendToUser", new SendToUserConsoleCommand());
        consoleCommandMap.put("login", new LoginConsoleCommand());
        consoleCommandMap.put("createGroup", new CreateGroupConsoleCommand());
        consoleCommandMap.put("joinGroup", new JoinGroupConsoleCommand());
        consoleCommandMap.put("quitGroup", new QuitGroupConsoleCommand());
        consoleCommandMap.put("listGroupMembers", new ListGroupMembersConsoleCommand());
        consoleCommandMap.put("sendToGroup", new SendToGroupConsoleCommand());
    }

    /**
     * 根据输入的命令去执行
     */
    public void execCommand(Scanner scanner, Channel channel) {
        if (!SessionUtil.hasLogin(channel)) {
            // 未登录时默认执行登录
            ConsoleCommand login = consoleCommandMap.get("login");
            login.exec(scanner, channel);
        } else {
            //  获取指令
            System.out.println("请输入要执行的指令");
            String command = scanner.next();

            ConsoleCommand consoleCommand = consoleCommandMap.get(command);
            if (consoleCommand == null) {
                System.err.println("无法识别[" + command + "]指令，请重新输入!");
            } else {
                // 执行对应命令
                consoleCommand.exec(scanner, channel);
            }
        }
    }
}
