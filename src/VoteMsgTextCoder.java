import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: Link
 * Date: 13-11-7
 * Time: PM1:57
 * To change this template use File | Settings | File Templates.
 */
public class VoteMsgTextCoder implements VoteMsgCoder {
    /**
     * Wire Format "VOTEPROTO" <"v"|"i"> [<RESPFLAG>] <CANDIDATE> [<VOTECNT></VOTECNT>]
     * <p/>
     * Charest is fixed by the wire format
     */

    public static final String MAGIC = "Voting";
    public static final String VOTESTR = "v";
    public static final String INQSTR = "i";
    public static final String RESPONSESTR = "R";
    public static final String CHARSETNAME = "US-ASCII";
    public static final String DELIMSTR = " ";
    public static final int MAX_WIRE_LENGTH = 2000;

    @Override
    public byte[] toWire(VoteMsg msg) throws IOException {
        String msgString = MAGIC + DELIMSTR
                + (msg.isInquiry ? INQSTR : VOTESTR) + DELIMSTR
                + (msg.isResponse ? RESPONSESTR + DELIMSTR : "")
                + Integer.toString(msg.candidateID) + DELIMSTR
                + Long.toString(msg.voteCount);
        byte data[] = msgString.getBytes(CHARSETNAME);
        return data;
    }

    @Override
    public VoteMsg fromWire(byte[] input) throws IOException {
        ByteArrayInputStream msgStream = new ByteArrayInputStream(input);
        Scanner s = new Scanner(new InputStreamReader(msgStream, CHARSETNAME));
        boolean isInquriy;
        boolean isResponse;
        int candidateID;
        long voteCount;
        String token;
        try {
            token = s.next();
            if (!token.equals(MAGIC)) {
                throw new IOException("Bad magic string: " + token);
            }
            token = s.next();
            if (token.equals(VOTESTR)) {
                isInquriy = false;
            } else if (!token.equals(INQSTR)) {
                throw new IOException("Bad vot/inq indicator: " + token);
            } else {
                isInquriy = true;
            }

            token = s.next();
            if (token.equals(RESPONSESTR)) {
                isResponse = true;
                token = s.next();
            } else {
                isResponse = false;
            }
            // Current token is candidateID
            // Note: isResponse now valid
            candidateID = Integer.parseInt(token);
            if (isResponse) {
                token = s.next();
                voteCount = Long.parseLong(token);
            } else {
                voteCount = 0;
            }
        } catch (IOException ioe) {
            throw new IOException("Parse error...");
        }

        return new VoteMsg(isResponse, isInquriy, candidateID, voteCount);  //ToDo
    }
}
