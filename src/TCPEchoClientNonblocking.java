import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created with IntelliJ IDEA.
 * User: Link
 * Date: 13-11-7
 * Time: PM3:47
 * To change this template use File | Settings | File Templates.
 */
public class TCPEchoClientNonblocking {

    public static void main(String[] args) throws IOException {
        String server = "127.0.0.1";
        byte[] argument = "TTTTT".getBytes();
        int servPort = 5225;
        SocketChannel clntChannel = SocketChannel.open();
        clntChannel.configureBlocking(false);
        if (!clntChannel.connect(new InetSocketAddress(server, servPort))) {
            while (!clntChannel.finishConnect()) {
                System.out.println(".");
            }
        }

        ByteBuffer writeBuf = ByteBuffer.wrap(argument);
        ByteBuffer readBuf = ByteBuffer.allocateDirect(argument.length);
        int totalByteRcvd = 0;
        int bytesRcvd;
        while (totalByteRcvd < argument.length) {
            if (writeBuf.hasRemaining()) {
                clntChannel.write(writeBuf);
            }
            if ((bytesRcvd = clntChannel.read(readBuf)) == -1) {
                throw new SocketException();
            }
            totalByteRcvd += bytesRcvd;
            System.out.println(".");
        }
        System.out.println("Recevied : " + new String(readBuf.array(), 0, totalByteRcvd));

        clntChannel.close();

    }
}
