package stuba.fei.gono.java.mongo.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.time.OffsetDateTime;
import java.util.Date;

/***
 * Custom converter for MongoDB that converts instance of OffsetDateTime class to instance of Date class.
 * Needed because MongoDB cannot deserialize object of OffsetDateTime class.
 * @see OffsetDateTime
 * @see Date
 * @see WritingConverter
 */
@WritingConverter
public class OffsetDateTimeWriteConverter implements Converter<OffsetDateTime, Date> {
    @Override
    public Date convert(OffsetDateTime offsetDateTime) {
        return Date.from(offsetDateTime.toInstant());
    }
}
