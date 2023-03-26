package netty.book.serialize;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import netty.book.protocol.LoginRequestPacket;
import netty.book.protocol.Packet;
import netty.book.protocol.command.Command;
import netty.book.serialize.algorithm.SerializerAlgorithm;
import netty.book.serialize.impl.JSONSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * 包解析工具类
 *
 * @author FangYuan
 * @since 2023-03-26 20:32:34
 */
public class PacketSerializerUtil {

    /**
     * 魔数
     */
    private static final int MAGIC_NUMBER = 0x12345678;

    /**
     * 序列化器们
     */
    private static final Map<Byte, Serializer> serializerMap;

    /**
     * 指令们
     */
    private static final Map<Byte, Class<? extends Packet>> packetTypeMap;

    static {
        serializerMap = new HashMap<>();
        serializerMap.put(SerializerAlgorithm.JSON.getValue(), new JSONSerializer());

        packetTypeMap = new HashMap<>();
        packetTypeMap.put(Command.LOGIN_REQUEST.getValue(), LoginRequestPacket.class);
    }

    /**
     * 编码
     */
    public static ByteBuf encode(Packet packet) {
        // 创建 ByteBuf 对象
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();

        // 开始编码 魔数
        byteBuf.writeInt(MAGIC_NUMBER);
        // 版本号
        byteBuf.writeByte(packet.getVersion());
        // 指令
        byteBuf.writeByte(packet.getCommand().getValue());
        // 序列化器
        byteBuf.writeByte(packet.getSerializer());
        // 对象信息
        Serializer serializer = serializerMap.get(packet.getSerializer());
        byte[] bytes = serializer.serialize(packet);
        // 对象长度和信息
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    /**
     * 解码
     */
    public static Packet decode(ByteBuf byteBuf) {
        // 跳过魔数
        byteBuf.skipBytes(4);
        // 跳过版本号
        byteBuf.skipBytes(1);
        // 指令
        byte command = byteBuf.readByte();
        // 序列化器
        Serializer serializer = serializerMap.get(byteBuf.readByte());
        // 对象信息
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        // 获取解析后的请求类型
        Class<? extends Packet> clazz = packetTypeMap.get(command);
        if (serializer != null && clazz != null) {
            return serializer.deserialize(clazz, bytes);
        }

        return null;
    }
}
