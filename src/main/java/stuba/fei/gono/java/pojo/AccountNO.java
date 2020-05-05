package stuba.fei.gono.java.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/***
 * <div class="en">Class representing AccountNO - Account number of the client
 * (type: IBAN with optional BIC or local account number) where withdraw will be realised.</div>
 * <div class="sk">Trieda reprezentujúca AccountNO - číslo bankového účtu (typ: IBAN s voliteľným BIC alebo
 * lokálne číslo účtu).</div>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountNO {
    private String iban;

    private String bic;

    private String localAccountNumber;
}
