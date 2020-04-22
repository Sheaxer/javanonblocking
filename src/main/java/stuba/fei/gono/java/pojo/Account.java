package stuba.fei.gono.java.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "accounts")
public class Account {


    private String iban;

    private String bic;
    private String localAccountNumber;
    @JsonIgnore
    private Boolean isActive;

}
