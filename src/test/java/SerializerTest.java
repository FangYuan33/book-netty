import io.netty.buffer.ByteBuf;
import netty.book.practice.protocol.login.LoginRequestPacket;
import netty.book.practice.protocol.Packet;
import netty.book.practice.serialize.PacketCodeC;
import netty.book.practice.serialize.impl.JSONSerializer;
import org.junit.Assert;
import org.junit.Test;

public class SerializerTest {

    @Test
    public void testSerializer() {
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setVersion(((byte) 1));
        loginRequestPacket.setSerializer((byte) 1);
        loginRequestPacket.setUserId(123);
        loginRequestPacket.setUserName("zhangsan");
        loginRequestPacket.setPassword("password");

        // 编码
        ByteBuf byteBuf = PacketCodeC.encode(loginRequestPacket);
        // 解码
        Packet packet = PacketCodeC.decode(byteBuf);

        JSONSerializer jsonSerializer = new JSONSerializer();
        Assert.assertArrayEquals(jsonSerializer.serialize(loginRequestPacket), jsonSerializer.serialize(packet));
    }
}
