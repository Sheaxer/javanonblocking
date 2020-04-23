package stuba.fei.gono.java.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/***
 * Class representing data about a bank client.
 */
@Data
@NoArgsConstructor
@Document(collection = "clients")
@TypeAlias(value = "client")
public class Client {
    @NotBlank
    private String firstName;
    @NotBlank
    private String surName;
    @NotBlank
    @Id
    @JsonIgnore
    private String id;
    @NotNull
    private Date dateOfBirth;
}
