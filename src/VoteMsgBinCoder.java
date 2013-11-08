import java.io.*;

/**
 * Wire Format
 * 111111
 * 0123456789012345
 * |Magic|Flags|ZERO|
 * |Candidate ID|
 * |Vote Count(only in respons_|
 */

public class VoteMsgBinCoder implements VoteMsgCoder {
    public static final int MIN_WIRE_LENGTH = 4;
    public static final int MAX_WIRE_LEGNTH = 16;
    public static final int MAGIC = 0x5400;
    public static final int MAGIC_MASK = 0xfc00;
    public static final int MAGIC_SHIFT = 8;
    public static final int RESPONSE_FLAG = 0x0200;
    public static final int INQUIRE_FLAG = 0x0100;

    @Override
    public byte[] toWire(VoteMsg msg) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteStream);
        short magicAndFlag = MAGIC;
        if (msg.isInquiry) {
            magicAndFlag |= INQUIRE_FLAG;
        }
        if (msg.isResponse) {
            magicAndFlag |= RESPONSE_FLAG;
        }
        out.writeShort(magicAndFlag);
        out.writeShort((short) msg.candidateID);
        if (msg.isResponse) {
            out.writeLong(msg.voteCount);
        }
        out.flush();
        byte[] date = byteStream.toByteArray();
        return date;
    }

    @Override
    public VoteMsg fromWire(byte[] input) throws IOException {
        if (input.length < MIN_WIRE_LENGTH) {
            throw new IOException("Runt message");
        }
        ByteArrayInputStream bs = new ByteArrayInputStream(input);
        DataInputStream in = new DataInputStream(bs);
        int magic = in.readShort();
        if ((magic & MAGIC_MASK) != MAGIC) {
            throw new IOException("Bad Magic #:" + ((magic & MAGIC_MASK) >> MAGIC_SHIFT));
        }
        boolean resp = ((magic & RESPONSE_FLAG) != 0);
        boolean inq = ((magic & INQUIRE_FLAG) != 0);
        int candidateID = in.readShort();
        if (candidateID < 0 || candidateID > 1000) {
            throw new IOException();
        }
        long count = 0;
        if (resp) {
            count = in.readLong();
            if (count < 0) {
                throw new IOException();
            }
        }
        return new VoteMsg(resp, inq, candidateID, count);
    }
}
