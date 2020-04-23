package stuba.fei.gono.java.nonblocking.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.errors.ReportedOverlimitTransactionBadRequestException;
import stuba.fei.gono.java.errors.ReportedOverlimitTransactionNotFoundException;
import stuba.fei.gono.java.nonblocking.errors.ReportedOverlimitTransactionValidationException;
import stuba.fei.gono.java.nonblocking.services.ReportedOverlimitTransactionService;
import stuba.fei.gono.java.nonblocking.pojo.ReportedOverlimitTransaction;

import java.time.OffsetDateTime;

/***
 * REST controller for GET,POST,PUT and DELETE methods for ReportedOverlimitTransaction entities.
 * @see ReportedOverlimitTransaction
 */
@Slf4j
@RestController
@RequestMapping(value = "/reportedOverlimitTransaction")
public class ReportedOverlimitTransactionController {

    private ReportedOverlimitTransactionService transactionService;

    @Autowired
    public ReportedOverlimitTransactionController(ReportedOverlimitTransactionService transactionService) {

        this.transactionService = transactionService;
    }

    /***
     * GET method - returns ReportedOverlimitTransaction entity with the given id.
     * @param id id of requested entity.
     * @return Mono emitting the value of entity.
     * @throws ReportedOverlimitTransactionNotFoundException if there is no entity with the given id.
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
     * POST method - generates new id and saves news and saves the given entity.
     * @param newTransaction - entity to be saved.
     * @return Mono emitting the saved entity.
     * @throws ReportedOverlimitTransactionValidationException containing error codes if the validation of entity fails.
     */
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ReportedOverlimitTransaction> postTransaction( @RequestBody ReportedOverlimitTransaction newTransaction)
    throws ReportedOverlimitTransactionValidationException
    {
        newTransaction.setModificationDate(OffsetDateTime.now());
      return  transactionService.postTransaction(newTransaction);
              /*.onErrorResume(throwable -> throwable instanceof ReportedOverlimitTransactionValidationException,
              throwable -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                      ((ReportedOverlimitTransactionValidationException)throwable).getErrors()))
      );*/
    }

    /***
     * PUT method - saves the given entity with the given id.
     * @param id id which will be identifying the saved entity.
     * @param transaction entity to be saved.
     * @return Mono emitting the saved entity.
     * @throws ReportedOverlimitTransactionValidationException containing error codes if the validation of entity fails.
     */
    @PutMapping(value = "/{id}", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ReportedOverlimitTransaction> putTransaction(@PathVariable String id,
                                                             @RequestBody ReportedOverlimitTransaction transaction)
    throws ReportedOverlimitTransactionValidationException
    {
        transaction.setModificationDate(OffsetDateTime.now());
        return transactionService.putTransaction(id, transaction);
    }

    /***
     * DELETE method - that deletes the entity with given id.
     * @param id id of entity that should be deleted.
     * @return Mono emitting when the operation was completed.
     * @throws ReportedOverlimitTransactionNotFoundException no entity with the given id was found.
     * @throws ReportedOverlimitTransactionBadRequestException entity with the given id couldn't be deleted.
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteTransaction(@PathVariable String id)
    throws ReportedOverlimitTransactionNotFoundException, ReportedOverlimitTransactionBadRequestException
    {
       return  transactionService.deleteTransaction(id);
    }

}
