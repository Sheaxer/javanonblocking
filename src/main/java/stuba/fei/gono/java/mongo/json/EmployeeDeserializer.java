package stuba.fei.gono.java.mongo.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stuba.fei.gono.java.blocking.mongo.repositories.EmployeeRepository;
import stuba.fei.gono.java.pojo.Employee;

import java.io.IOException;
import java.util.Optional;

@Component
public class EmployeeDeserializer extends StdDeserializer<Employee> {
    public EmployeeDeserializer(Class<?> vc) {
        super(vc);
    }

    public EmployeeDeserializer() {
        this(null);
    }

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        Optional<Employee> e =  employeeRepository.findById(jsonParser.getText());
        return e.orElse(null);
    }
}
