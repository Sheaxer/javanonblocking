package stuba.fei.gono.java.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/***
 * Class representing structure for vault. Detail information about withdraw amount.
 */
@Data
@NoArgsConstructor
public class Vault {
    /***
     * Type of tender.
     */
    @NotNull
    private VaultType type;
    /***
     * Number tenders per type.
     */
    @Positive
    private int number;
    /***
     * Nominal value (power) of tender.
     */
    @Positive
    private int nominalValue;
}
