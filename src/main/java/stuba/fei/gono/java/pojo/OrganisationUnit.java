package stuba.fei.gono.java.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/***
 * <div class="en">Class maintaining data about organisation unit.</div>
 * <div class="sk">Trieda, ktorá uchováva dáta o mieste výberu.</div>
 */
@Data
@NoArgsConstructor
@Document(collection = "organisationUnits")
public class OrganisationUnit {
    @Id
    private String id;
}
