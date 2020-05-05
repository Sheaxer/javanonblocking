package stuba.fei.gono.java.mongo.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/***
 * <div class="en">Custom converter for transforming object of Date class
 * to OffsetDateTime object with offset UTC.
 * Needed because MongoDB cannot serialize objects of OffsetDateTime class.</div>
 * <div class="sk">Vlastný prevodník na transformáciu objektu triedy Date na objekt OffsetDateTime s ofsetom UTC.
 * Potrebné, pretože MongoDB nemôže priamo serializovať objekty triedy OffsetDateTime.</div>
 * @see OffsetDateTime
 * @see Date
 * @see ReadingConverter
 */
@ReadingConverter
public class OffsetDateTimeReadConverter implements Converter<Date, OffsetDateTime> {
    @Override
    public OffsetDateTime convert(Date date) {
        return date.toInstant().atOffset(ZoneOffset.UTC);
    }
}
