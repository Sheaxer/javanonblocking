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
 * Service that generates next id value for storing data in MongoDB.
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
     * Increments the value of sequence with the given sequence name and return it.
     * @see SequenceId
     * @param seqName - name of the sequence.
     * @return - updated value of the sequence.
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
     * Sets value of sequence in MongoDB.
     * @see SequenceId
     * @param seqName name of the sequence,
     * @param value value that the sequence will be set to.
     * @return Mono containing SequenceId with modified value.
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
     * Retrieves new value of an id for saving new object in a repository. Updates maximal value
     * of sequence with the given name, checks if an entity with this id already exists in the repository.
     * If it does exist, function finds the actual maximal value of id used to store entities in the repository and
     * updates the sequence.
     * @param rep repository where the object will be saved.
     * @param sequenceName name of the sequence holding the id of last saved object.
     * @return value of id that should be used to save object in the given repository.
     */
    public Mono<String> getNewId(@NotNull ReactiveCrudRepository<?,String> rep, @NotNull String sequenceName)
    {


       return this.getNextSequence(sequenceName).flatMap(
                e ->
                        rep.existsById(e).map(
                                  t ->
                                  {
                                      log.info("wasModified");
                                      log.info(t.toString());
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
                                          log.info("wasModified");
                                          return tmpId;
                                      } else
                                          return e;
                                  }
                          ).cast(String.class)
        );
    }

    /***
     * Calculates the maximal id that was used to save an object in the given repository.
     * Transforms ids of all entities of the given class into long and finds the max.
     * @param rep repository, must not be null.
     * @return String value of the maximal id in the given repository.
     */
    private String lastId(@NotNull Class<?> rep)
    {
        return mongoOperations.execute(rep, mongoCollection -> {
            FindIterable<Document> doc= mongoCollection.find().projection(Projections.include("_id"));
            Long max=0L;

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
            Long lastVal=0L;
            for (Long tmp : s) {
                lastVal = tmp > lastVal ? tmp : lastVal;
            }
            lastVal++;
            return String.valueOf(lastVal);
        });
    }

    /***
     * Checks if the sequence with given name needs to update its maximal id value by the given value.
     * @param seqName - name of the sequence, must not be null.
     * @param val - value to be checked against maximal id value, must not be null.
     * @return Mono emitting when the operation was completed.
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
