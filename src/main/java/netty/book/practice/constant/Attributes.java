package netty.book.practice.constant;

import io.netty.util.AttributeKey;

public interface Attributes {
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("LOGIN");
}
