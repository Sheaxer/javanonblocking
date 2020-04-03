package stuba.fei.gono.java.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "organisationUnits")
public class OrganisationUnit {
    @Id
    private String id;
}
