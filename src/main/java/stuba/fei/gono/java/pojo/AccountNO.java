package stuba.fei.gono.java.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountNO {
    private String iban;

    private String bic;

    private String localAccountNumber;
}
