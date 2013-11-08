import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Link
 * Date: 13-11-7
 * Time: PM12:18
 * To change this template use File | Settings | File Templates.
 */

public class DelimFramer implements Framer {

    private InputStream in;
    private static final byte DELIMITER = '\n';

    public DelimFramer(InputStream in) {
        this.in = in;
    }

    @Override
    public void frameMsg(byte[] message, OutputStream out) throws IOException {
        //ToDo
        for (byte b : message) {
            if (b == DELIMITER) {
                throw new IOException("Message contains delimiter");
            }
        }
        out.write(message);
        out.write(DELIMITER);
        out.flush();
    }

    @Override
    public byte[] nextMsg() throws IOException {
        ByteArrayOutputStream messageBuffer = new ByteArrayOutputStream();
        int nextByte;
        //fetch bytes until find delimiter

        while ((nextByte = in.read()) != DELIMITER) {
            if (nextByte == -1) {
                if (messageBuffer.size() == 0) {
                    return null;
                } else {
                    //if bytes followed by end of stream:framing error
                    throw new EOFException("Non-empty message without delimter");
                }
            }
            messageBuffer.write(nextByte);
        }
        return messageBuffer.toByteArray();
    }
}
