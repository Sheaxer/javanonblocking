package stuba.fei.gono.java.mongo.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.ZonedDateTime;

@Component
public class ZonedDateTimeDeserializer extends StdDeserializer<ZonedDateTime> {

   // private static  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a z");

    public ZonedDateTimeDeserializer()
    {
        this(null);
    }

    protected ZonedDateTimeDeserializer (Class<?> vc) {
        super(vc);
    }

    @Override
    public ZonedDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String tmp = jsonParser.getText();

        return ZonedDateTime.parse(tmp);
    }
}
