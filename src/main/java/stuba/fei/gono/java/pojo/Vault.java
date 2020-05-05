package stuba.fei.gono.java.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/***
 * <div class="en">Class representing structure for vault.
 * Detail information about withdraw amount.</div>
 * <div class="sk">Trieda reprezentujúca tzv. štruktúru "vault".
 * Podáva podrobnejšie informácie o výbere sumy.</div>
 */
@Data
@NoArgsConstructor
public class Vault {
    /***
     * <div class="en">Type of tender.</div>
     * <div class="sk">Typ tendra.</div>
     */
    @NotNull
    private VaultType type;
    /***
     * <div class="en">Number tenders per type.</div>
     * <div class="sk">Počet tendrov typu</div>
     */
    @Positive
    private int number;
    /***
     * <div class="en">Nominal value (power) of tender.</div>
     * <div class="sk">Nominálna hodnota (sila) tendra.</div>
     */
    @Positive
    private int nominalValue;
}
