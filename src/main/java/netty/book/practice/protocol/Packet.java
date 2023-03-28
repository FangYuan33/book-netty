package netty.book.practice.protocol;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import netty.book.practice.protocol.command.Command;

/**
 * 通信数据包基类
 *
 * @author FangYuan
 * @since 2023-03-26 20:16:22
 */
@Data
public abstract class Packet {

    /**
     * 协议版本
     */
    @JSONField(deserialize = false, serialize = false)
    private Byte version = 1;

    /**
     * 序列化器 默认JSON解析
     */
    @JSONField(deserialize = false, serialize = false)
    private Byte serializer = 1;

    /**
     * 获取这个请求的命令
     */
    @JSONField(serialize = false, deserialize = false)
    public abstract Command getCommand();
}
