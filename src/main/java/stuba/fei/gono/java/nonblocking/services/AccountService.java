package stuba.fei.gono.java.nonblocking.services;

import reactor.core.publisher.Mono;
import stuba.fei.gono.java.pojo.Account;

/***
 * Interface for marshalling and de-marshalling Account entities.
 */
public interface AccountService {
    /***
     * Retrieve entity identified by the given IBAN.
     * @param iban IBAN identifying the entity.
     * @return Mono emitting the entity or Mono.empty() if no entity was found.
     */
    Mono<Account> getAccountByIban(String iban);
    /***
     * Retrieve entity identified by the given Local Account Number.
     * @param number Local Account Number identifying the entity.
     * @return Mono emitting the entity or Mono.empty() if no entity was found.
     */
    Mono<Account> getAccountByLocalAccountNumber(String number);
}
