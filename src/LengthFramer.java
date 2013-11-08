import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Link
 * Date: 13-11-7
 * Time: PM1:33
 * To change this template use File | Settings | File Templates.
 */
public class LengthFramer implements Framer {

    public static final int MAXMESSAGELENGTH = 65535;
    public static final int BYTEMASK = 0xff;
    public static final int SHORTMASK = 0xffff;
    public static final int BYTESHIFT = 8;
    private DataInputStream in;

    public LengthFramer(InputStream in) {
        this.in = new DataInputStream(in);
    }

    @Override
    public void frameMsg(byte[] message, OutputStream out) throws IOException {
        //ToDo
        if (message.length > MAXMESSAGELENGTH) {
            throw new IOException("Message too long");
        }

        //write length prefix
        out.write((message.length >> BYTESHIFT) & BYTEMASK);
        out.write(message.length & BYTEMASK);
        //write message
        out.write(message);
        out.flush();

    }

    @Override
    public byte[] nextMsg() throws IOException {
        int length;
        try {
            length = in.readUnsignedShort(); //read 2 bytes
        } catch (EOFException e) {
            return null;
        }
        // 0<=Length <=65535
        byte[] msg = new byte[length];
        in.readFully(msg);
        return msg;
    }


}
