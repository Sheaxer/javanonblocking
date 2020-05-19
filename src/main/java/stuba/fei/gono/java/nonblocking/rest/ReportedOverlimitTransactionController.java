package stuba.fei.gono.java.nonblocking.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.errors.ReportedOverlimitTransactionBadRequestException;
import stuba.fei.gono.java.errors.ReportedOverlimitTransactionNotFoundException;
import stuba.fei.gono.java.nonblocking.errors.ReportedOverlimitTransactionValidationException;
import stuba.fei.gono.java.nonblocking.pojo.ReportedOverlimitTransaction;
import stuba.fei.gono.java.nonblocking.services.ReportedOverlimitTransactionService;

/***
 * <div class="en">REST controller for GET, POST, PUT and DELETE methods for
 * ReportedOverlimitTransaction resource. Attached to the endpoints that start with
 * /reportedOverlimitTransaction .</div>
 * <div class="sk">REST kontrolér pre GET, POST, PUT a DELETE metódy nad zdrojom triedy
 * ReportedOverlimitTransaction. Pripojený k endpointom začínajúcim s /reportedOverlimitTransaction .</div>
 * @see ReportedOverlimitTransaction
 */
@Slf4j
@RestController
@RequestMapping(value = "/reportedOverlimitTransaction")
public class ReportedOverlimitTransactionController {
    /***
     * <div class="en">Service that provides GET, PUT, POST and DELETE operations on ReportedOverlimitTransaction
     * entities.</div>
     * <div class="sk">Služba ktorá poskytuje GET, PUT, POST a DELETE operácie nad entitami triedy
     * ReportedOverlimitTransaction.</div>
     */
    private ReportedOverlimitTransactionService transactionService;

    @Autowired
    public ReportedOverlimitTransactionController(ReportedOverlimitTransactionService transactionService) {

        this.transactionService = transactionService;
    }

    /***
     * <div class="en">GET method - retrieves the entity with the given id. </div>
     * <div class="sk">GET metóda - načíta entitu so zadaným id.</div>
     * @param id <div class="en">id of requested entity, must not be null.</div>
     *           <div class="sk">id požadovanej entity, nesmie byť null.</div>
     * @return <div class="en">Mono emitting the value of entity or Mono.error containing
     * ReportedOverlimitTransactionNotFoundException if entity does not exist.</div>
     * <div class="sk">Mono emitujúce hodnotu žiadanej entity alebo Mono.error obsahujúce
     * výnimku ReportedOverlimitTransactionNotFoundException ak žiadaná entita neexistuje.</div>
     * @throws ReportedOverlimitTransactionNotFoundException <div class="en">exception thrown
     * if there is no entity with the given id.</div> <div class="sk">výnimka
     * vyvolaná, ak entita so zadaným id neexistuje.</div>
     */
    @GetMapping(value = "/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Mono<ReportedOverlimitTransaction> getTransaction (@PathVariable String id)
            throws ReportedOverlimitTransactionNotFoundException
    {
        return transactionService.getTransactionById(id).switchIfEmpty(Mono.error(
                new ReportedOverlimitTransactionNotFoundException("ID_NOT_FOUND")));
    }

    /***
     * <div class="en">POST method - validates the entity and if valid -
     * generates new id and saves the entity.</div>
     * <div class="sk">POST metóda - validuje entitu a ak je korektná - generuje nové id a uloží entitu.</div>
     * @param newTransaction <div class="en">entity to be saved, must not be null.</div>
     *                       <div class="sk">entita, ktorá sa má uložiť, nesmie byť null.</div>
     * @return <div class="en">Mono emitting the saved entity.</div>
     * <div class="sk">Mono emitujúce uloženú entitu.</div>
     * @throws ReportedOverlimitTransactionValidationException <div class="en"> exception containing validation
     * error codes, thrown if the validation of entity fails.</div>
     * <div class="sk">výnimka obsahujúca chybné validačné hlášky, vyvolaná ak je validácia neúspešná.</div>
     */
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ReportedOverlimitTransaction> postTransaction( @RequestBody ReportedOverlimitTransaction newTransaction)
    throws ReportedOverlimitTransactionValidationException
    {

      return  transactionService.postTransaction(newTransaction);
    }

    /***
     * <div class="en">PUT method - validates the entity and if valid - saves it using the given id.</div>
     * <div class="sk">PUT metóda - validuje entitu a ak je korektná - uloží ju so zadaným id.</div>
     * @param id <div class="en">id which will be identifying the saved entity.</div>
     *           <div class="sk">id ktoré bude identifikovať uloženú entitu.</div>
     * @param transaction <div class="en">entity to be saved.</div>
     *                    <div class="sk">entita, ktorá sa má uložiť.</div>
     * @return <div class="en">Mono emitting the saved entity.</div>
     * <div class="sk">Mono emitujúce uloženú entitu.</div>
     * @throws ReportedOverlimitTransactionValidationException <div class="en">exception containing the validation
     * error codes, thrown if the validation of entity fails.</div>
     * <div class="sk">výnimka obsahujúca validačné chybové hlášky, ktorá je vyvolaná ak je validácia entity
     * neúspešná.</div>
     */
    @PutMapping(value = "/{id}", consumes = "application/json")
    @PostMapping(value = "/{id}",consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ReportedOverlimitTransaction> putTransaction(@PathVariable String id,
                                                             @RequestBody ReportedOverlimitTransaction transaction)
    throws ReportedOverlimitTransactionValidationException
    {

        return transactionService.putTransaction(id, transaction);
    }

    /***
     * <div class="en">DELETE method - that deletes the entity with given id.</div>
     * <div class="sk">DELETE metóda - zmaže entitu so zadaným id.</div>
     * @param id <div class="en">id of entity that should be deleted.</div>
     *           <div class="sk">id entity ktorá by mala byť zmazaná</div>
     * @return <div class="en">Mono emitting when the operation was completed.</div>
     * <div class="sk">Mono emitujúce keď je operácia úspešná.</div>
     * @throws ReportedOverlimitTransactionNotFoundException <div class="sk">exception
     * thrown if no entity with the given id was found.</div>
     * <div class="sk">výnimka vyhodená ak nebola nájdená entita so zadaným id.</div>
     * @throws ReportedOverlimitTransactionBadRequestException <div class="en">exception thrown
     * if entity with the given id couldn't be deleted.</div>
     * <div class="sk">výnimka vyvolaná ak entita so zadaným id nemohla byť vymazaná.</div>
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteTransaction(@PathVariable String id)
    throws ReportedOverlimitTransactionNotFoundException, ReportedOverlimitTransactionBadRequestException
    {
       return  transactionService.deleteTransaction(id);
    }

}
