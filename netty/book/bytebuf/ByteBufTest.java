package bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * ByteBuf API 测试、学习
 *
 * @author FangYuan
 * @since 2023-03-26 15:15:48
 */
public class ByteBufTest {
    public static void main(String[] args) {
        // initial:9 max: 100
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(9, 100);

        print("allocate ByteBuf(9, 100)", buffer);

        // 写入字节数组 写指针移动 未到capacity 仍然可写
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        print("writeBytes(1, 2, 3, 4)", buffer);

        // 写入整型 占用四个字节
        buffer.writeInt(12);
        print("writeInt(12)", buffer);

        // 写入到达初始化的字节大小 9 isWritable未false
        buffer.writeBytes(new byte[]{5});
        print("writeBytes(5)", buffer);

        // 扩容，扩容之后可以继续写
        buffer.writeBytes(new byte[]{6});
        print("writeBytes(6)", buffer);

        // get 方法不改变读写指针
        System.out.println("getByte(3) return: " + buffer.getByte(3));
        System.out.println("getShort(3) return: " + buffer.getShort(3));
        System.out.println("getInt(3) return: " + buffer.getInt(3));
        print("getByte()", buffer);

        // set 方法不改变读写指针
        buffer.setByte(buffer.readableBytes() + 1, 0);
        print("setByte()", buffer);

        // read 方法改变读指针
        byte[] bytes = new byte[buffer.readableBytes()];
        buffer.readBytes(bytes);
//        buffer.readByte();
//        buffer.readInt();
        // ... 其他各种不同的读方法
        print("readBytes(" + bytes.length + ")", buffer);

        // 写入到最大容量
        for (byte i = 10; i < 100; i++) {
            buffer.writeByte(i);
        }
        print("write to max", buffer);

        // 超出max
        buffer.writeByte(1);
    }

    private static void print(String action, ByteBuf byteBuf) {
        System.out.println("after ===========" + action + "============");
        System.out.println("capacity(): " + byteBuf.capacity());
        System.out.println("maxCapacity(): " + byteBuf.maxCapacity());
        System.out.println("readerIndex(): " + byteBuf.readerIndex());
        System.out.println("readableBytes(): " + byteBuf.readableBytes());
        // 读写指针重合不可读
        System.out.println("isReadable(): " + byteBuf.isReadable());
        System.out.println("writerIndex(): " + byteBuf.writerIndex());
        System.out.println("writableBytes(): " + byteBuf.writableBytes());
        // capacity 和 写指针重合不可写 但是扩容之后还可写
        System.out.println("isWritable(): " + byteBuf.isWritable());
        System.out.println("maxWritableBytes(): " + byteBuf.maxWritableBytes());
        System.out.println();
    }
}
