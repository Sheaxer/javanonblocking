package stuba.fei.gono.java.nonblocking.mongo.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.pojo.Account;

@Repository
public interface AccountRepository extends ReactiveMongoRepository<Account, String> {
    Mono<Account> findAccountByIban(String iban);
    Mono<Account> findAccountByLocalAccountNumber(String number);
}
