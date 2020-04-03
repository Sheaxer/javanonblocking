package stuba.fei.gono.java.mongo.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/***
 * Custom serialization of OffsetDateTime to JSON. JSON format is yy-MM-ddThh:mm:ssOffset.
 */
@Slf4j
public class OffsetDateTimeSerializer extends StdSerializer<OffsetDateTime> {
    protected OffsetDateTimeSerializer(Class<OffsetDateTime> t) {
        super(t);
    }

    public OffsetDateTimeSerializer()
    {
        this(null);
    }

    @Override
    public void serialize(OffsetDateTime offsetDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        //DateTimeFormatter.ISO_DATE_TIME
        String tmp = DateTimeFormatter.ISO_DATE_TIME.format(offsetDateTime);
        log.info("I AM HERE");
        log.info(tmp);

        if(tmp.lastIndexOf('Z') != -1)
        {
            tmp = tmp.substring(0,tmp.lastIndexOf('.')).concat("+00:00");
        }
        else {
            int index1 = tmp.lastIndexOf('.');
            int index2 = tmp.lastIndexOf('+');
            if (index2 == -1)
                index2 = tmp.lastIndexOf('-');
            tmp = tmp.substring(0, index1).concat(
                    tmp.substring(index2));
        }
        log.info(tmp);
        jsonGenerator.writeString(tmp);
    }
}
