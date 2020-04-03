package stuba.fei.gono.java.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class Vault {
    @NotNull
    private VaultType type;
    @Positive
    private int number;
    @Positive
    private int nominalValue;
}
