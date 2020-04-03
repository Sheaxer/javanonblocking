package stuba.fei.gono.java.mongo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import stuba.fei.gono.java.mongo.converters.OffsetDateTimeReadConverter;
import stuba.fei.gono.java.mongo.converters.OffsetDateTimeWriteConverter;
import stuba.fei.gono.java.mongo.converters.ZonedDateTimeReadConverter;
import stuba.fei.gono.java.mongo.converters.ZonedDateTimeWriteConverter;

import java.util.ArrayList;
import java.util.List;

/***
 * Configuration class that modifies default configuration of MongoDB
 */
@Configuration
public class MongoConfig  {

    /***
     * Adds custom converters for converting Java Classes that are unable to be serialized / deserialized by MongoDB
     * to classes that are able to be serialized / deserialized
     * @return Instance of class MongoCustomConversions instantiated by list of custom MongoDB converters
     * @see MongoCustomConversions
     * @see org.springframework.data.convert.WritingConverter
     * @see org.springframework.data.convert.ReadingConverter
     */
    @Bean
    public MongoCustomConversions customConversions()
    {
        List<Converter<?,?>> converters = new ArrayList<>();
        converters.add(new ZonedDateTimeReadConverter());
        converters.add(new ZonedDateTimeWriteConverter());
        converters.add(new OffsetDateTimeReadConverter());
        converters.add(new OffsetDateTimeWriteConverter());
        return new MongoCustomConversions(converters);
    }

}
