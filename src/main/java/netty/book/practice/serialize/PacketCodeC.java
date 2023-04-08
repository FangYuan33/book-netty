package netty.book.practice.serialize;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import netty.book.practice.protocol.request.*;
import netty.book.practice.protocol.Packet;
import netty.book.practice.protocol.command.Command;
import netty.book.practice.protocol.response.*;
import netty.book.practice.serialize.algorithm.SerializerAlgorithm;
import netty.book.practice.serialize.impl.JSONSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * 包解析工具类
 *
 * @author FangYuan
 * @since 2023-03-26 20:32:34
 */
public class PacketCodeC {

    /**
     * 魔数
     */
    public static final int MAGIC_NUMBER = 0x12345678;

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
        // 登录
        packetTypeMap.put(Command.LOGIN_REQUEST.getValue(), LoginRequestPacket.class);
        packetTypeMap.put(Command.LOGIN_RESPONSE.getValue(), LoginResponsePacket.class);
        // 消息
        packetTypeMap.put(Command.MESSAGE_REQUEST.getValue(), MessageRequestPacket.class);
        packetTypeMap.put(Command.MESSAGE_RESPONSE.getValue(), MessageResponsePacket.class);
        // 创建群组
        packetTypeMap.put(Command.CREATE_GROUP_REQUEST.getValue(), CreateGroupRequestPacket.class);
        packetTypeMap.put(Command.CREATE_GROUP_RESPONSE.getValue(), CreateGroupResponsePacket.class);
        // 加入群组
        packetTypeMap.put(Command.JOIN_GROUP_REQUEST.getValue(), JoinGroupRequestPacket.class);
        packetTypeMap.put(Command.JOIN_GROUP_RESPONSE.getValue(), JoinGroupResponsePacket.class);
        // 退出群组
        packetTypeMap.put(Command.QUIT_GROUP_REQUEST.getValue(), QuitGroupRequestPacket.class);
        packetTypeMap.put(Command.QUIT_GROUP_RESPONSE.getValue(), QuitGroupResponsePacket.class);
        // 列举群组成员
        packetTypeMap.put(Command.LIST_MEMBERS_REQUEST.getValue(), ListGroupMembersRequestPacket.class);
        packetTypeMap.put(Command.LIST_MEMBERS_RESPONSE.getValue(), ListGroupMembersResponsePacket.class);
        // 收发群组消息
        packetTypeMap.put(Command.SEND_GROUP_MESSAGE_REQUEST.getValue(), GroupMessageRequestPacket.class);
        packetTypeMap.put(Command.SEND_GROUP_MESSAGE_RESPONSE.getValue(), GroupMessageResponsePacket.class);
        // 心跳
        packetTypeMap.put(Command.HEART_BEAT_REQUEST.getValue(), HeartBeatRequestPacket.class);
        packetTypeMap.put(Command.HEART_BEAT_RESPONSE.getValue(), HeartBeatResponsePacket.class);
    }

    /**
     * 编码
     */
    public static ByteBuf encode(Packet packet) {
        // 创建 ByteBuf 对象
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        writeByteBufInfo(byteBuf, packet);

        return byteBuf;
    }

    /**
     * 编码
     */
    public static void encode(ByteBuf out, Packet msg) {
        writeByteBufInfo(out, msg);
    }

    /**
     * 编码ByteBuf信息
     */
    private static void writeByteBufInfo(ByteBuf out, Packet msg) {
        // 开始编码 魔数
        out.writeInt(MAGIC_NUMBER);
        // 版本号
        out.writeByte(msg.getVersion());
        // 指令
        out.writeByte(msg.getCommand().getValue());
        // 序列化器
        out.writeByte(msg.getSerializer());
        // 对象信息
        Serializer serializer = serializerMap.get(msg.getSerializer());
        byte[] bytes = serializer.serialize(msg);
        // 对象长度和信息
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
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
