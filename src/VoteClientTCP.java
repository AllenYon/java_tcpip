import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: Link
 * Date: 13-11-7
 * Time: PM2:30
 * To change this template use File | Settings | File Templates.
 */
public class VoteClientTCP {
    public static final int CANDIDATEID = 888;

    public static void main(String args[]) throws IOException {
//        if (args.length != 2) {
//            throw new IllegalArgumentException();
//        }
//        String destAddr = args[0];
//        int destPort = Integer.parseInt(args[1]);
        String destAddr = "127.0.0.1";
        int destPort = 5135;

        Socket sock = new Socket(destAddr, destPort);
        OutputStream out = sock.getOutputStream();
        VoteMsgCoder coder = new VoteMsgBinCoder();
        Framer framer = new LengthFramer(sock.getInputStream());
        VoteMsg msg = new VoteMsg(false, true, CANDIDATEID, 0);
        byte[] encodeMsg = coder.toWire(msg);
        System.out.println("Sending Inquiry (" + encodeMsg.length + " bytes);");
        framer.frameMsg(encodeMsg, out);

        //Now send a vote
        msg.isInquiry = false;
        encodeMsg = coder.toWire(msg);
        System.out.println("Sending Vote (" + encodeMsg.length + " bytes);");
        framer.frameMsg(encodeMsg, out);

        //Receive inquiry response
        encodeMsg = framer.nextMsg();
        msg = coder.fromWire(encodeMsg);
        System.out.println("Received Response (" + encodeMsg.length + " bytes);");
        System.out.println(msg);

        sock.close();
    }
}
