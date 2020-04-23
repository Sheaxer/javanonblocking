package stuba.fei.gono.java.nonblocking.mongo.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.pojo.Account;

/***
 * Interface extending ReactiveMongoRepository for Account entities.
 * @see Account
 * @see org.springframework.data.repository.reactive.ReactiveCrudRepository
 */
@Repository
public interface AccountRepository extends ReactiveCrudRepository<Account, String> {
    /***
     * Returns entity identified by the given IBAN
     * @param iban - IBAN identifying the Account entity, must not be null.
     * @return Mono emitting the Account or Mono.empty() if no entity is found.
     */
    Mono<Account> findAccountByIban(String iban);
    /***
     * Returns entity identified by the given Local Account Number
     * @param number - Local Account Number identifying the Account entity, must not be null.
     * @return Mono emitting the Account or Mono.empty() if no entity is found.
     */
    Mono<Account> findAccountByLocalAccountNumber(String number);
}
