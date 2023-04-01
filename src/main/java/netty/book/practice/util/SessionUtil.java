package netty.book.practice.util;


import io.netty.channel.Channel;
import netty.book.practice.constant.Attributes;
import netty.book.practice.session.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Session工具类
 *
 * @author FangYuan
 * @since 2023-03-28 21:22:52
 */
public class SessionUtil {

    /**
     * 用来保存 userName 和 channel 的映射
     */
    private static final Map<String, Channel> userNameChannelMap = new ConcurrentHashMap<>();

    /**
     * 绑定Session代表登陆成功
     */
    public static void bindSession(Session session, Channel channel) {
        // 保存映射对对应channel的参数
        userNameChannelMap.put(session.getUserName(), channel);
        channel.attr(Attributes.SESSION).set(session);
    }

    /**
     * 登出
     */
    public static void unBindSession(Channel channel) {
        if (hasLogin(channel)) {
            // 移除对应的映射关系和保存的channel参数
            userNameChannelMap.remove(getSession(channel).getUserName());
            channel.attr(Attributes.SESSION).set(null);
        }
    }

    /**
     * 根据该变量是否有来判断是否登录成功
     */
    public static boolean hasLogin(Channel channel) {
        return channel.attr(Attributes.SESSION).get() != null;
    }

    /**
     * 获取登录Session
     */
    public static Session getSession(Channel channel) {
        return channel.attr(Attributes.SESSION).get();
    }

    /**
     * 获取Channel信息
     */
    public static Channel getChannel(String userName) {
        return userNameChannelMap.get(userName);
    }
}
