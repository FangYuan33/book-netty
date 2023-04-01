package netty.book.practice.util;


import io.netty.channel.Channel;
import netty.book.practice.constant.Attributes;

/**
 * 登录工具类
 *
 * @author FangYuan
 * @since 2023-03-28 21:22:52
 */
public class LoginUtil {

    /**
     * 登录成功后标记
     */
    public static void markAsLogin(Channel channel) {
        channel.attr(Attributes.LOGIN).set(true);
    }

    /**
     * 根据该变量是否有来判断是否登录成功
     */
    public static boolean hasLogin(Channel channel) {
        return channel.attr(Attributes.LOGIN).get() != null;
    }
}
