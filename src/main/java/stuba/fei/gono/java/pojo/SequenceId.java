package stuba.fei.gono.java.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customSequences")
public class SequenceId {

    @Id
    private String id;

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
