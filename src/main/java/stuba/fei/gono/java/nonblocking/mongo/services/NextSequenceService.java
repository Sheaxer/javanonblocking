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
import stuba.fei.gono.java.nonblocking.mongo.repositories.ClientRepository;
import stuba.fei.gono.java.nonblocking.mongo.repositories.EmployeeRepository;
import stuba.fei.gono.java.nonblocking.mongo.repositories.OrganisationUnitRepository;
import stuba.fei.gono.java.nonblocking.mongo.repositories.ReportedOverlimitTransactionRepository;
import stuba.fei.gono.java.pojo.*;

import javax.validation.constraints.NotNull;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Slf4j
@Service
public class NextSequenceService {
    private final ReactiveMongoOperations reactiveMongoOperations;
    private final MongoOperations mongoOperations;

    public NextSequenceService(ReactiveMongoOperations reactiveMongoOperations, MongoOperations mongoOperations) {
        this.reactiveMongoOperations = reactiveMongoOperations;
        this.mongoOperations = mongoOperations;
    }

    public Mono<String> getNextSequence(@NotNull String seqName)
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
     * @param seqName name of the sequence
     * @param value value that the sequence will be set to
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

    public String lastId(@NotNull Class<?> rep)
    {
        return mongoOperations.execute(rep, mongoCollection -> {
            FindIterable<Document> doc= mongoCollection.find().projection(Projections.include("_id"));
            Long max=0L;

            MongoIterable<Long> s = doc.map(document -> Long.valueOf(document.getString("_id")));
            Long lastVal=0L;
            for (Long tmp : s) {
                lastVal = tmp > lastVal ? tmp : lastVal;
            }
            lastVal++;
            return String.valueOf(lastVal);
        });
    }

}
