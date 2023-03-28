package netty.book.practice.serialize.impl;

import com.alibaba.fastjson.JSON;
import netty.book.practice.serialize.Serializer;
import netty.book.practice.serialize.algorithm.SerializerAlgorithm;

public class JSONSerializer implements Serializer {
    @Override
    public SerializerAlgorithm getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
