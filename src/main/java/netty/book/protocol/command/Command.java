package netty.book.protocol.command;

/**
 * 指令枚举
 *
 * @author FangYuan
 * @since 2023-03-26 20:14:57
 */
public enum Command {

    LOGIN_REQUEST(Byte.valueOf("1"));

    private final Byte value;

    Command(Byte value) {
        this.value = value;
    }

    public Byte getValue() {
        return value;
    }
}
