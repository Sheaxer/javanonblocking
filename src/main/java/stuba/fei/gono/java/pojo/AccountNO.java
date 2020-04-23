package stuba.fei.gono.java.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/***
 * Class representing AccountNO - Account number of the client
 * (type: IBAN with optional BIC or local account number) where withdraw will be realised
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountNO {
    private String iban;

    private String bic;

    private String localAccountNumber;
}
