package stuba.fei.gono.java.nonblocking.mongo.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import stuba.fei.gono.java.pojo.Account;
import stuba.fei.gono.java.pojo.Client;

/***
 * Interface extending ReactiveMongoRepository for Client entities.
 * @see Client
 * @see org.springframework.data.repository.reactive.ReactiveCrudRepository
 */
@Repository
public interface ClientRepository extends ReactiveCrudRepository<Client,String> {
}
