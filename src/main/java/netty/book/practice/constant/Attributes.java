package netty.book.practice.constant;

import io.netty.util.AttributeKey;
import netty.book.practice.session.Session;

/**
 * 一些必要的Channel Attribute 常量
 *
 * @author FangYuan
 * @since 2023-04-01 21:42:37
 */
public interface Attributes {

    AttributeKey<Session> SESSION = AttributeKey.newInstance("SESSION");
}
