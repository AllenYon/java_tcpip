/**
 * Created with IntelliJ IDEA.
 * User: Link
 * Date: 13-11-7
 * Time: PM1:55
 * To change this template use File | Settings | File Templates.
 */
public class VoteMsg {
    public boolean isInquiry;
    public boolean isResponse;
    public int candidateID;
    public long voteCount;
    public static final int MAX_CANDIDATE_ID = 1000;

    public VoteMsg(boolean inquiry, boolean response, int candidateID, long voteCount) {
        isInquiry = inquiry;
        isResponse = response;
        this.candidateID = candidateID;
        this.voteCount = voteCount;
    }


    @Override
    public String toString() {
        return "VoteMsg{" +
                "isInquiry=" + isInquiry +
                ", isResponse=" + isResponse +
                ", candidateID=" + candidateID +
                ", voteCount=" + voteCount +
                '}';
    }
}

