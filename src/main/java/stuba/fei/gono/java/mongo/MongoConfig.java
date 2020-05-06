package stuba.fei.gono.java.mongo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import stuba.fei.gono.java.mongo.converters.OffsetDateTimeReadConverter;
import stuba.fei.gono.java.mongo.converters.OffsetDateTimeWriteConverter;

import java.util.ArrayList;
import java.util.List;

/***
 * <div class="en">Configuration class that modifies default configuration of MongoDB.</div>
 * <div class="sk">Konfiguračná trieda, ktorá modifikuje predvolenú konfiguráciu MongoDB.</div>
 */
@Configuration
public class MongoConfig {


    /***
     * <div class="en">Adds custom converters for converting Java Classes that are unable to be serialized
     * / deserialized by MongoDB to classes that are able to be serialized / deserialized.</div>
     * <div class="sk">Trieda ktorá pridáva vlastné prevodníky pre MongoDB na konverziu Java tried ktoré nie je možné
     * priamo serializovať v MongoDB.</div>
     * @return <div class="en">Instance of class MongoCustomConversions instantiated by list of
     * custom MongoDB converters.</div>
     * <div class="sk">Inštancia triedy MongoCustomConvertions vytvorená zoznamom vlastných MongoDB prevodníkov.</div>
     * @see MongoCustomConversions
     * @see org.springframework.data.convert.WritingConverter
     * @see org.springframework.data.convert.ReadingConverter
     */
    @Bean
    public MongoCustomConversions customConversions()
    {
        List<Converter<?,?>> converters = new ArrayList<>();
        converters.add(new OffsetDateTimeReadConverter());
        converters.add(new OffsetDateTimeWriteConverter());
        return new MongoCustomConversions(converters);
    }
}
