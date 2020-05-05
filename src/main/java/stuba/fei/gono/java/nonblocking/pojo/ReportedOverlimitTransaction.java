package stuba.fei.gono.java.nonblocking.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import stuba.fei.gono.java.json.OffsetDateTimeDeserializer;
import stuba.fei.gono.java.json.*;
import stuba.fei.gono.java.pojo.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

/***
 * <div class="en">Class representing ReportedOverlimitTransaction from FENiX - New FrontEnd solution API definition.
 * </div>
 * <div class="sk">Trieda reprezentujúca ReportedOverlimitTransaction z FENix - New FrontEnd solution API definition
 * </div>
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "reportedOverlimitTransactions")
public class ReportedOverlimitTransaction {
    /***
     * Internal identifier of order (provided as response after creation from BE)
     */
    @Id
    private String id;

    /***
     * Order category determines whether reported overlimit transaction is withdraw in EUR or foreign currency.
     */
    private OrderCategory orderCategory;
    /***
     * State of order presented to user on FE, value is mapped based on provided BE technical states.
     */
    private State state;

    /***
     * Account number of the client (type: IBAN with optional BIC or local account number) where
     * withdraw will be realised.
     */
    private AccountNO sourceAccount;
    /***
     * Id of client who will realize withdraw. On frontend we have to show client name and dato of birth.
     */
    private String clientId;

    /***
     * Id of client identification with which will realize withdraw. On frontend we have to show number of
     * identification.
     */
    private String identificationId;
    /***
     * Structure for vault. Detail information about withdrow amount.
     */
    private List<Vault> vault;

    /***
     * Withdraw amount in defined currency (only EUR for DOMESTIC) and with precision (embedded AMOUNT type).
     */
    private Money amount;

    /***
     * Requested due date entered by client (have to be in near future, minimal D+3),
     * date when withdraw order should be realized from user account.
     * Default value could be current business day +3 ISO date format: YYYY-MM-DD.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date transferDate;
    /***
     * Client/teller note to related withdraw
     */

    private String note;

    /***
     * Modification date indicates the last update of order done by user or BE system (read-only field provided by BE).
     * ISO dateTime format: YYYY-MM-DDThh:mm:ssZ
     */
    @JsonDeserialize(using = OffsetDateTimeDeserializer.class)
    @JsonSerialize(using = OffsetDateTimeSerializer.class)
    private OffsetDateTime modificationDate;

    /***
     * Getter for modificationDate.
     * @return modification date at an offset saved.
     */
    public OffsetDateTime getModificationDate()
    {
        if(this.zoneOffset !=null)
            this.setModificationDate(this.modificationDate.toInstant().atOffset(ZoneOffset.of(this.zoneOffset)));
        return this.modificationDate;
    }

    /***
     * Setter for modification date, stores the offset id as well.
     * @param modificationDate new modificationDate.
     */
    public void setModificationDate(OffsetDateTime modificationDate) {
        this.modificationDate = modificationDate;
        this.zoneOffset = modificationDate.getOffset().getId();
    }

    /***
     * Id of organisation unit where client want to realize withdraw.
     */
    private String organisationUnitID;
    /***
     * Id of employer who entered an transaction. In this case report over limit withdraw.
     */
    private String createdBy;
    /***
     * Offset id of time zone.
     */
    @JsonIgnore
    private String zoneOffset;

}
