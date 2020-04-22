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

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "reportedOverlimitTransactions")
public class ReportedOverlimitTransaction {

    @Id
    private String id;


    private OrderCategory orderCategory;

    private State state;


    private Account sourceAccount;

   // @DBRef
    //@Valid
    //@JsonDeserialize(using = ClientDeserializer.class)
    //@JsonSerialize(using = ClientSerializer.class)
    private String clientId;

    private String identificationId;


    private Money amount;


    private List<Vault> vault;

    @JsonDeserialize(using = OffsetDateTimeDeserializer.class)
    @JsonSerialize(using = OffsetDateTimeSerializer.class)
    private OffsetDateTime modificationDate;

    public OffsetDateTime getModificationDate()
    {
        if(this.zoneOffset !=null)
            this.setModificationDate(this.modificationDate.toInstant().atOffset(ZoneOffset.of(this.zoneOffset)));
        return this.modificationDate;
    }

    public void setModificationDate(OffsetDateTime modificationDate) {
        this.modificationDate = modificationDate;
        this.zoneOffset = modificationDate.getOffset().getId();
    }

    //@Past(message = "INVALID_DATE_IN_PAST")
    //@BankingDay(message = "INVALID_DATE")

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date transferDate;

    private String note;

    /*@DBRef

    @JsonDeserialize(using = OrganisationUnitDeserializer.class)
    @JsonSerialize(using = OrganisationUnitSerializer.class)
    private OrganisationUnit organisationUnitID;*/

    private String organisationUnitID;

    //@DBRef

    //@JsonDeserialize(using = EmployeeDeserializer.class)
    //@JsonSerialize(using = EmployeeSerializer.class)
    private String createdBy;

    @JsonIgnore
    private String zoneOffset;
    /*@PersistenceConstructor
    public ReportedOverlimitTransaction(String id, OrderCategory orderCategory, State state, Account sourceAccount,
                                        Client clientId, String identificationId, Money amount, )

*/
}
