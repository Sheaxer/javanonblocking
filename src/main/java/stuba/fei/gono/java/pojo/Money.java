package stuba.fei.gono.java.pojo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Money {

    @NotNull
    private Currency currency;
    private double amount;
}
