package stuba.fei.gono.java.nonblocking.services;


import reactor.core.publisher.Mono;
import stuba.fei.gono.java.errors.ReportedOverlimitTransactionBadRequestException;
import stuba.fei.gono.java.errors.ReportedOverlimitTransactionNotFoundException;
import stuba.fei.gono.java.nonblocking.errors.ReportedOverlimitTransactionValidationException;
import stuba.fei.gono.java.nonblocking.pojo.ReportedOverlimitTransaction;

/***
 * <div class="en">Interface for marshalling and de-marshalling ReportedOverlimitTransaction entities.</div>
 * <div class="sk">Rozhranie na marhslling a de-marshalling entít triedy ReportedOverlimitTransaction.</div>
 */
public interface ReportedOverlimitTransactionService {
     /***
      * <div class="en">Validates the antity and if it is valid - generates a new id
      * and saves the entity.</div>
      * <div class="sk">Validuje entitu a ak je korektná - generuje nové id a uloží entitu.</div>
      * @param transaction <div class="en">entity to be saved.</div>
      *                    <div class="sk">entita, ktorá má byť uložená.</div>
      * @return <div class="en">Mono emitting the saved entity if the entity was valid or
      * Mono.error() containing ReportedOverlimitTransactionValidationException with failed validation error codes if
      * the entity is not valid.</div>
      * <div class="sk">Mono emitujúce uloženú entitu ak entita bola korektná alebo Mono.error()
      * obsahujúce ReportedOverlimitTransactionValidationException výnimku s chybovými kódmi neúspešnej validácie.
      * </div>
      * @see ReportedOverlimitTransactionValidationException
      */
     Mono<ReportedOverlimitTransaction> postTransaction(ReportedOverlimitTransaction transaction);

     /***
      * <div class="en">Finds the entity with the given id.</div>
      * <div class="sk">Nájde entitu so zadaným id.</div>
      * @param id <div class="en">id of the entity, must not be null.</div>
      *           <div class="sk">id entity, nesmie byť null.</div>
      * @return <div class="en">Mono emitting the entity or Mono.empty() if there is none.</div>
      * <div class="sk">Mono emitujúce entitu alebo Mono.empty() ak entita neexistuje.</div>
      */
     Mono<ReportedOverlimitTransaction> getTransactionById(String id);

     /***
      * <div class="en">Validates the entity and if valid - saves the entity with the given id.</div>
      * <div class="sk">Validuje entitu a ak je korektná, uloží entitu so zadaným id.</div>
      * @param id <div class="en">id of the entity.</div>
      *           <div class="sk">id entity</div>
      * @param transaction <div class="en">entity to be saved.</div>
      *                    <div class="sk">entita, ktorá sa má uložiť.</div>
      * @return <div class="en">Mono emitting the saved entity or
      * Mono.error() containing ReportedOverlimitTransactionValidationException with failed
      * validation error codes if the entity is not valid.</div>
      * <div class="sk">Mono emitujúce uloženú entitu ak bola entita korektná alebo Mono.error() obsahujúca
      * ReportedOverlimitTransactionValidationException výnimku s chybovými validačnými kódmi ak entita
      * nebola korektná.</div>
      * @see ReportedOverlimitTransactionValidationException
      */
     Mono<ReportedOverlimitTransaction> putTransaction(String id, ReportedOverlimitTransaction transaction);

     /***
      * <div class="en">Deletes the entity wtih by the given id.</div>
      * <div class="sk">Zmaže entitu so zadaným id.</div>
      * @param id <div class="en">id of entity to be deleted.</div>
      *           <div class="sk">id entity, ktorú treba vymazať.</div>
      * @return <div class="en">Mono emitting when the operation was completed,
      * Mono.error() constaining ReportedOverlimitTransactionNotFoundException
      * if the entity with given id was not found or Mono.error() containing
      * ReportedOverlimitTransactionBadRequestException if the
      * entity couldn't be deleted because its state is State.CLOSED.</div>
      * <div class="sk">Mono emitujúce ak operácia prebehla, Mono.error() obsahujúce
      * ReportedOverlimitTransactionNotFoundException výnimku ak entita so zadaným id neexistuje
      * alebo Mono.error() obsahujúce ReportedOverlimitTransactionBadRequestException ak entita nemôže byť vymazaná,
      * pretože hodnota State je State.CLOSED.</div>
      * @see  ReportedOverlimitTransactionNotFoundException
      * @see ReportedOverlimitTransactionBadRequestException
      */
     Mono<Void> deleteTransaction (String id);

     /***
      * <div class="en">Checks if the entity is valid.</div>
      * <div class="sk">Skontroluje či entita je korektná.</div>
      * @param transaction <div class="en">entity to be validated.</div>
      *                    <div class="sk">entita, ktorá má byť validovaná.</div>
      * @return <div class="en">Mono emitting that the operation completed or Mono.error() containing
      * ReportedOverlimitTransactionValidationException with the failed validation error codes.</div>
      * <div class="sk">Mono emitujúce informáciu, že operácia prebehla ak entita bola korektná alebo
      * Mono.error() obsahujúca ReportedOverlimitTransactionValidationException výnimku s validačnými
      * chybnými kódmi ak entita nebola korektná.</div>
      * @see ReportedOverlimitTransactionValidationException
      */
      Mono<Void> validate(ReportedOverlimitTransaction transaction)
             throws ReportedOverlimitTransactionValidationException;
}
