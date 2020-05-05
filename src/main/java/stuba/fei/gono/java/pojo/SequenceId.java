package stuba.fei.gono.java.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/***
 * <div class="en">Class holding data about custom sequences in MongoDB,
 * used to store maximal used id for auto id generation.</div>
 * <div class="sk">Trieda uchovávajúca dáta o vlastný sekvenciách
 * v MongoDB použitých na uloženie maximálnej hodnoty id použitého na automatickú
 * id generáciu.</div>
 */
@Document(collection = "customSequences")
public class SequenceId {
    /***
     * <div class="en">Name of the sequence</div>
     * <div class="sk">Názov sekvencie</div>
     */
    @Id
    private String id;
    /***
     * <div class="en"> Maximal value of id used to save an entity.</div>
     * <div class="sk">Maximálne hodnota id použitá na uloženie entity.</div>
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
