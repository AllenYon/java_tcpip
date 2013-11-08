import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Link
 * Date: 13-11-7
 * Time: PM2:26
 * To change this template use File | Settings | File Templates.
 */
public class VoteService {
    private Map<Integer, Long> results = new HashMap<Integer, Long>();

    public VoteMsg handleRequest(VoteMsg msg) {
        if (msg.isResponse) {
            return msg;
        }
        msg.isResponse = true;
        int candidate = msg.candidateID;
        Long count = results.get(candidate);
        if (count == null) {
            count = 0L;
        }
        if (!msg.isInquiry) {
            results.put(candidate, ++count);
        }
        msg.voteCount = count;

        return msg;
    }

}
