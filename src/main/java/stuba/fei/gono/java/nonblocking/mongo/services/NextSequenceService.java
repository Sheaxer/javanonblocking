package stuba.fei.gono.java.nonblocking.mongo.services;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Projections;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.nonblocking.mongo.repositories.*;
import stuba.fei.gono.java.nonblocking.pojo.ReportedOverlimitTransaction;
import stuba.fei.gono.java.pojo.*;

import javax.validation.constraints.NotNull;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
/***
 * <div class="en">Service that generates next id value for storing data in MongoDB.</div>
 * <div class="sk">Služba, ktorá generuje nasledujúcu hodnotu id použitú na uloženie entity do Mongo databázy.
 * </div>
 */
@Slf4j
@Service
public class NextSequenceService {
    private final ReactiveMongoOperations reactiveMongoOperations;
    private final MongoOperations mongoOperations;

    public NextSequenceService(ReactiveMongoOperations reactiveMongoOperations, MongoOperations mongoOperations) {
        this.reactiveMongoOperations = reactiveMongoOperations;
        this.mongoOperations = mongoOperations;
    }

    /***
     * <div class="en">Increments the value of sequence with the given
     * sequence name.</div>
     * <div class="sk">Inkrementuje hodnotu sekvencie so zadaným menom.</div>
     * @see SequenceId
     * @param seqName <div class="en">name of the sequence.</div>
     *                <div class="sk">názov sekvencie.</div>
     * @return <div class="en">Mono emitting updated value of the sequence.</div>
     * <div class="sk">Momo emitujúce aktualizovnú hodnota sekvencie.</div>
     */
    private Mono<String> getNextSequence(@NotNull String seqName)
    {
        Mono<SequenceId> counter = reactiveMongoOperations.findAndModify(
                query(where("_id").is(seqName)),
                new Update().inc("seq",1),
                options().returnNew(true).upsert(true),
                SequenceId.class).switchIfEmpty(setNextSequence(seqName,"1"));
        /*if(counter == null)
        {
            setNextSequence(seqName,String.valueOf(1));
            return "1";
        }*/
        /*if(counter == null)
        {
            log.info("doing new");
            mongoOperations.executeCommand("{db.sequence.insert({_id: \""+seqName+"\",seq: 2})}");
            return "1";
        }*/

        return counter.map(
                t-> String.valueOf(t.getSeq())
        ).cast(String.class);
    }

    /***
     * <div class="en">Sets value of sequence with the given name.</div>
     * <div class="sk">Nastaví hodnotu sekvencie so zadaným názvom.</div>
     * @see SequenceId
     * @param seqName <div class="en">name of the sequence.</div>
     *                <div class="sk">názov sekvencie.</div>
     * @param value <div class="en">value that the sequence will be set to.</div>
     *              <div class="sk">hodnota na ktorú sa sekvencia nastaví.</div>
     * @return <div class="en">Mono emitting the SequenceId with modified value.</div>
     * <div class="sk">Mono emitujúce objekt triedy SequenceId s modifikovanou hodnotou.</div>
     */
    public Mono<SequenceId> setNextSequence(@NotNull String seqName,@NotNull String value)
    {
        return reactiveMongoOperations.findAndModify(
                query(where("_id").is(seqName)),
                new Update().set("seq",Long.valueOf(value)),
                options().returnNew(true).upsert(true),
                SequenceId.class
        );
        /*if(s==null)
        {
            mongoOperations.executeCommand("{db.sequence.insert({_id: \""+seqName+"\",seq: "+value+"})}");
        }*/
    }

    /***
     * <div class="en">Generates a new value of an id for saving new object in a database.
     * Updates maximal value
     * of sequence with the given name, checks if an entity with this id already exists in the repository.
     * If it does exist, function finds the actual maximal value of id used to store entities in the repository and
     * updates the sequence.</div>
     * <div class="sk">Generuje novú hodnotu id na uloženie nového objektu do databázy. Aktualizuje
     * maximálnu hodnotu sekvencie so zadaným menom, skontroluje či už existuje entita so zadaným id. Ak existuje,
     * využije ďalšiu metódu na získanie skutočnej maximálnej hodnoty id v zadanom repozitáry a aktualizuje hodnotu
     * sekvencie.</div>
     * @param rep <div class="en">repository where the object will be saved.</div>
     *            <div class="sk">repozitár v ktorom bude objekt uložený.</div>
     * @param sequenceName <div class="en">name of the sequence holding the id of last saved object.</div>
     *                     <div class="sk">názov sekvencie ktorá udržiava id posledného uloženého objektu.</div>
     * @return <div class="en">Mono emitting the value of id that should be used to save object in
     * the given repository.</div>
     * <div class="sk">Mono emitujúce hodntotu id ktoré by malo byť použité na uloženie
     * objektu v zadanom repozitári.</div>
     */
    public Mono<String> getNewId(@NotNull ReactiveCrudRepository<?,String> rep, @NotNull String sequenceName)
    {


       return this.getNextSequence(sequenceName).flatMap(
                e ->
                        rep.existsById(e).map(
                                  t ->
                                  {
                                      //log.info("wasModified");
                                      //log.info(t.toString());
                                      if (t) {
                                          String tmpId = "";
                                          if (rep instanceof ReportedOverlimitTransactionRepository) {
                                              tmpId = lastId(ReportedOverlimitTransaction.class);
                                          } else if (rep instanceof ClientRepository)
                                              tmpId = lastId(Client.class);
                                          else if (rep instanceof EmployeeRepository)
                                              tmpId = lastId(Employee.class);
                                          else if (rep instanceof OrganisationUnitRepository)
                                              tmpId = lastId(OrganisationUnit.class);
                                          else if(rep instanceof AccountRepository)
                                              tmpId = lastId(Account.class);
                                          //newId = this.getNextSequence(sequenceName);
                                          this.setNextSequence(sequenceName, tmpId).subscribe();
                                          //log.info("wasModified");
                                          return tmpId;
                                      } else
                                          return e;
                                  }
                          ).cast(String.class)
        );
    }

    /***
     * <div class="en">Calculates the maximal id that was used to save an object of the given class.
     * Transforms ids of all entities of the given class into long and finds the maximal value.</div>
     * <div class="sk">Získa maximálnu hodnotu id ktoré bolo použité na uloženie objektu zadanej triedy.
     * Transformuje id všetkých entít triedy do typu long a získa maximálnu hodnotu.</div>
     * @param rep <div class="en">class of the entities, must not be null.</div>
     *            <div class="sk">trieda entít, nesmie byť null.</div>
     * @return <div class="en">string value of the maximal id of saved entity of the given class.</div>
     * <div class="sk">hodnota maximálneho id použitého na uloženie entity zadanej triedy.</div>
     */
    private String lastId(@NotNull Class<?> rep)
    {
        return mongoOperations.execute(rep, mongoCollection -> {
            // finds id field of entites in the repository
            FindIterable<Document> doc= mongoCollection.find().projection(Projections.include("_id"));
            //transforms id field to long
            MongoIterable<Long> s = doc.map(document ->
            {
                try{
                    return Long.parseLong(document.getString("_id"));
                }
                catch (NumberFormatException e)
                {
                    return 0L;
                }

            });
            //finds the max value
            Long lastVal=0L;
            for (Long tmp : s) {
                lastVal = tmp > lastVal ? tmp : lastVal;
            }
            lastVal++;
            //returns string
            return String.valueOf(lastVal);
        });
    }

    /***
     * <div class="en">Checks if the sequence with given name needs to update
     * its maximal value. If the given value is larger than the maximal value stored in
     * the sequence with the given name, it sets it to the new value.</div>
     * <div class="sk">Skontroluje, či sekvencia so zadaným názvom potrebuje aktualizovať
     * maximálnu hodnotu id. Ak je zadaná hodnota väčšia ako uložená maximálna hodnota, nastaví
     * sa táto uložená hodnota na novú.</div>
     * @param seqName <div class="en">name of the sequence, must not be null.</div>
     *                <div class="sk">názov sekvencie, nesmie byť null.</div>
     * @param val <div class="en">value to be checked against maximal id value, must not be null.</div>
     *            <div class="sk">hodnota oproti ktorej sa uložená maximálna hodnota porovná.</div>
     * @return <div class="en">Mono emitting when the operation was completed.</div>
     * <div class="sk">Mono emitujúce keď sa operácia uskutoční.</div>
     */
    public Mono<Void> needsUpdate(String seqName, String val)
    {
       return reactiveMongoOperations.find(query(where("_id").is(seqName)),SequenceId.class ).
               switchIfEmpty(Mono.just(new SequenceId())).single().flatMap(
               sequenceId ->
               {
                   try
                   {
                       long longVal = Long.parseLong(val);
                       if(longVal > sequenceId.getSeq())
                           return setNextSequence(seqName,val).then();
                       else
                           return Mono.just(true).then();
                   }
                   catch(NumberFormatException e)
                   {
                       return Mono.just(true).then();
                   }
               }
       );
    }

}
