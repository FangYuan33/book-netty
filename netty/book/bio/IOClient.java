package bio;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

/**
 * BIO 客户端
 *
 * @author FangYuan
 * @since 2023-03-26 08:58:25
 */
public class IOClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 8888);
            while (true) {
                socket.getOutputStream().write((new Date() + ": hello world").getBytes());

                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
