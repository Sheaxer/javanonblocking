package stuba.fei.gono.java.nonblocking.mongo.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.pojo.Account;

/***
 * <div class="en">Interface extending ReactiveCrudRepository for Account entities. Automatically
 * instantiated by Spring</div>
 * <div class="sk>Rozhranie rozširujúce ReactiveCrudRepository pre entity triedy Account. Automaticky inštanciované
 * pomocou Spring.</div>
 * @see Account
 * @see org.springframework.data.repository.reactive.ReactiveCrudRepository
 */
@Repository
public interface AccountRepository extends ReactiveCrudRepository<Account, String> {
    /***
     * <div class="en">Finds the entity identified by the given IBAN.</div>
     * <div class="sk">Nájde entitu identifikovanú zadaným IBAN-om.</div>
     * @param iban - <div class="en">IBAN identifying the Account entity, must not be null.</div>
     *             <div class="sk">IBAN identikujúci entitu triedy Account, nemôže byť null.</div>
     * @return <div class="en">Mono emitting the entity or Mono.empty() if no entity is found.</div>
     * <div class="sk">Mono emitujúce entitu alebo Mono.empty() ak nebolo možné entitu nájsť.</div>
     */
    Mono<Account> findAccountByIban(String iban);

    /***
     * <div class="en">Finds the entity identified by the given Local Account Number.</div>
     * <div class="sk">Nájde entitu identifikovanú zadaným lokálnym číslom účtu.</div>
     * @param number - <div class="en">Local Account Number identifying the Account entity, must not be null.</div>
     *               <div class="sk">lokálne číslo účtu identifikujúce entitu triedy Account, nesmie byť null.</div>
     * @return <div class="en">Mono emitting the entity or Mono.empty() if no entity is found.</div>
     * <div class="sk">Mono emitujúce entitu alebo Mono.empty() ak nebolo možné entitu nájsť.</div>
     */
    Mono<Account> findAccountByLocalAccountNumber(String number);
}
