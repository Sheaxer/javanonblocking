package stuba.fei.gono.java.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@Document(collection = "employees")
public class Employee  {
    @Id
    private String id;
    @NotBlank
    private String userName;
    @NotBlank
    private String password;


}
