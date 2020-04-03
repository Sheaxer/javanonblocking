package stuba.fei.gono.java.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Account {


    private String iban;

    private String bic;
    private String localAccountNumber;

}
