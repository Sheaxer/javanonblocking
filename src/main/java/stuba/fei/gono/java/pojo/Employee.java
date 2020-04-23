package stuba.fei.gono.java.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

/***
 * Class representing data about bank employee and the system user.
 */
@Data
@NoArgsConstructor
@Document(collection = "employees")
public class Employee  {
    @Id
    private String id;
    @NotBlank
    @Indexed(unique = true)
    private String username;
    @NotBlank
    private String password;


}
