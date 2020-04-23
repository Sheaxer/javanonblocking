package stuba.fei.gono.java.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/***
 * Class representing a banking account.
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "accounts")
public class Account {

    @Indexed(unique = true)
    private String iban;

    private String bic;
    @Indexed(unique = true)
    private String localAccountNumber;
    @JsonIgnore
    private Boolean isActive;

}
