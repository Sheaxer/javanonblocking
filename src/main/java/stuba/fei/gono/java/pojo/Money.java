package stuba.fei.gono.java.pojo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/***
 * <div class="en">Class representing withdraw amount in defined currency
 * (only EUR for DOMESTIC) and with precision.</div>
 * <div class="sk">Trieda reprezentujúca výber čiastky v definovanej peňažnej mene (pre domáce výbery len
 * mena EURO.)</div>
 */
@Data
public class Money {

    @NotNull
    private Currency currency;
    private double amount;
}
