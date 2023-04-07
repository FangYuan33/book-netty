package netty.book.practice.protocol.command;

/**
 * 指令枚举
 *
 * @author FangYuan
 * @since 2023-03-26 20:14:57
 */
public enum Command {

    /**
     * 登录
     */
    LOGIN_REQUEST(Byte.valueOf("1")),
    LOGIN_RESPONSE(Byte.valueOf("2")),
    /**
     * 收发消息
     */
    MESSAGE_REQUEST(Byte.valueOf("3")),
    MESSAGE_RESPONSE(Byte.valueOf("4")),
    /**
     * 创建群组的请求和响应
     */
    CREATE_GROUP_REQUEST(Byte.valueOf("5")),
    CREATE_GROUP_RESPONSE(Byte.valueOf("6")),
    /**
     * 加入、退出和群组列表
     */
    JOIN_GROUP_REQUEST(Byte.valueOf("7")),
    JOIN_GROUP_RESPONSE(Byte.valueOf("8")),
    QUIT_GROUP_REQUEST(Byte.valueOf("9")),
    QUIT_GROUP_RESPONSE(Byte.valueOf("10")),
    LIST_MEMBERS_REQUEST(Byte.valueOf("11")),
    LIST_MEMBERS_RESPONSE(Byte.valueOf("12")),
    /**
     * 发送群聊消息
     */
    SEND_GROUP_MESSAGE_REQUEST(Byte.valueOf("13")),
    SEND_GROUP_MESSAGE_RESPONSE(Byte.valueOf("14"));

    private final Byte value;

    Command(Byte value) {
        this.value = value;
    }

    public Byte getValue() {
        return value;
    }
}
