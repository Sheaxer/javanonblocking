package stuba.fei.gono.java.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/***
 * Class holding data about custom sequences in MongoDB, used to store maximal used id for auto id generation.
 */
@Document(collection = "customSequences")
public class SequenceId {
    /***
     * Name of the sequence
     */
    @Id
    private String id;
    /***
     * Maximal value of id used to save entities.
     */
    private long seq;

    public long getSeq() {
        return this.seq;
    }

    public void setSeq(long seq)
    {
        this.seq = seq;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
