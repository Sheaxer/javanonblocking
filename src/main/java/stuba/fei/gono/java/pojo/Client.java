package stuba.fei.gono.java.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

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
    private String id;
}
