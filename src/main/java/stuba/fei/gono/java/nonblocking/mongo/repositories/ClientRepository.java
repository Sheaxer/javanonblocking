package stuba.fei.gono.java.nonblocking.mongo.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import stuba.fei.gono.java.pojo.Client;

/***
 * <div class="en">Interface extending ReactiveCrudRepository for Client entities. Automatically
 * instantiated by the Spring</div>
 * <div class="sk">Rozhranie rozširujúce ReactiveCrudRepository pre entity triedy Client. Automaticky inštanciované
 * pomocou Spring.</div>
 * @see Client
 * @see org.springframework.data.repository.reactive.ReactiveCrudRepository
 */
@Repository
public interface ClientRepository extends ReactiveCrudRepository<Client,String> {
}
