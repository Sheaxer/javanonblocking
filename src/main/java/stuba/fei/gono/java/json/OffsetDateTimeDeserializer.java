package stuba.fei.gono.java.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.OffsetDateTime;

/***
 * <div class="en">Class that allows deserialization from JSON to an instance of OffsetDateTime.</div>
 * <div class="sk">Trieda ktorá zabezpečuje de-serializáciu z JSON reťazca na inštanciu triedy OffsetDateTime.</div>
 * @see OffsetDateTime
 * @see StdDeserializer
 */
public class OffsetDateTimeDeserializer extends StdDeserializer<OffsetDateTime> {
    protected OffsetDateTimeDeserializer(Class<?> vc) {
        super(vc);
    }

    public OffsetDateTimeDeserializer()
    {
        this(null);
    }

    @Override
    public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String tmp = jsonParser.getText();

        return OffsetDateTime.parse(tmp);
    }
}
