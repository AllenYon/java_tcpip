import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Link
 * Date: 13-11-7
 * Time: PM1:56
 * To change this template use File | Settings | File Templates.
 */
public interface VoteMsgCoder {
    byte[] toWire(VoteMsg msg) throws IOException;

    VoteMsg fromWire(byte[] input) throws IOException;

}
