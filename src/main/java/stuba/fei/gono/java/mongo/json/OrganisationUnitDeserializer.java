package stuba.fei.gono.java.mongo.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.nonblocking.mongo.repositories.OrganisationUnitRepository;
import stuba.fei.gono.java.pojo.OrganisationUnit;

import java.io.IOException;
import java.util.Optional;

@Component
public class OrganisationUnitDeserializer extends StdDeserializer<OrganisationUnit> {

    @Autowired
    private OrganisationUnitRepository organisationUnitRepository;

    protected OrganisationUnitDeserializer(Class<?> vc) {
        super(vc);
    }

    public OrganisationUnitDeserializer()
    {
        this(null);
    }

    @Override
    public OrganisationUnit deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        Mono<OrganisationUnit> o = organisationUnitRepository.findById(jsonParser.getText());
        return o.block();
    }
}
