package stuba.fei.gono.java.nonblocking.services;

import reactor.core.publisher.Mono;
import stuba.fei.gono.java.pojo.Account;

/***
 * <div class="en">Interface for marshalling and de-marshalling Account entities.</div>
 * <div class="sk">Rozhranie na marhalling a de-marshalling entít triedy Account.</div>
 */
public interface AccountService {

    /***
     * <div class="en">Finds the entity identified by the given IBAN.</div>
     * <div class="sk">Nájde entitu identifikovanú zadaným IBAN-om.</div>
     * @param iban <div class="en">IBAN identifying the entity.</div>
     *             <div class="sk">IBAN identifikujúci entitu.</div>
     * @return <div class="en">Mono emitting the entity or Mono.empty() if no entity was found.</div>
     * <div class="sk">Mono emitujúce entitu alebo Mono.empty() ak entita neexistuje.</div>
     */
    Mono<Account> getAccountByIban(String iban);

    /***
     * <div class="en">Finds entity identified by the given Local Account Number.</div>
     * <div class="sk">Nájde entitu identifikovanú lokálnym číslom účtu.</div>
     * @param number <div class="en">Local Account Number identifying the entity.</div>
     *               <div class="sk">Lokálne číslo účtu identifikujúce entitu.</div>
     * @return <div class="en">Mono emitting the entity or Mono.empty() if no entity was found.</div>
     * <div class="sk">Mono emitujúce entitu alebo Mono.empty() ak žiadna entita nebola nájdená.</div>
     */
    Mono<Account> getAccountByLocalAccountNumber(String number);
}
