package stuba.fei.gono.java.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/***
 * <div class="en">Custom serialization of OffsetDateTime to JSON. JSON format is yy-MM-ddThh:mm:ssOffset.</div>
 *
 * <div class="sk">Vlastná serializácia objektu triedy OffsetDateTime do JSON reťazca
 * vo formáte yyyy-MM-ddThh:mm:ss:Z .</div>
 * @see OffsetDateTime
 * @see StdSerializer
 */
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
        // replace
        if(tmp.lastIndexOf('Z') != -1)
        {
            tmp = tmp.substring(0,tmp.lastIndexOf('Z')).concat("+00:00");
        }

        int index1=tmp.lastIndexOf('.');
        if(index1!= -1)
        {
            int index2= tmp.lastIndexOf('+');
            if(index2 == -1)
                index2=tmp.lastIndexOf('-');
            tmp = tmp.substring(0,index1).concat(tmp.substring(index2));
        }
        jsonGenerator.writeString(tmp);
    }
}
