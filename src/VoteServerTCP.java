import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: Link
 * Date: 13-11-7
 * Time: PM2:38
 * To change this template use File | Settings | File Templates.
 */
public class VoteServerTCP {

    public static void main(String args[]) throws IOException {
//        if (args.length != 1) {
//            throw new IllegalArgumentException();
//        }
//
//        int port = Integer.parseInt(args[0]);
        int port=5135;

        ServerSocket servSocket = new ServerSocket(port);
        VoteMsgCoder coder = new VoteMsgBinCoder();
        VoteService service = new VoteService();
        while (true) {
            Socket clntSock = servSocket.accept();
            System.out.println("Handling client at " + clntSock.getRemoteSocketAddress());
            Framer framer = new LengthFramer(clntSock.getInputStream());
            try {

                byte[] req;
                while ((req = framer.nextMsg()) != null) {
                    System.out.println("Received message (" + req.length + " bytes");
                    VoteMsg responseMsg = service.handleRequest(coder.fromWire(req));
                    framer.frameMsg(coder.toWire(responseMsg), clntSock.getOutputStream());

                }
            } catch (IOException e) {

            } finally {
                System.out.println("Closing connection");
                clntSock.close();
            }

        }
    }
}
