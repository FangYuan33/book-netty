import io.netty.buffer.ByteBuf;
import netty.book.protocol.LoginRequestPacket;
import netty.book.protocol.Packet;
import netty.book.serialize.PacketCodeC;
import netty.book.serialize.impl.JSONSerializer;
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
