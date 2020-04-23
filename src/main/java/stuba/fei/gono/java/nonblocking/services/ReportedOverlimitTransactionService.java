package stuba.fei.gono.java.nonblocking.services;


import reactor.core.publisher.Mono;
import stuba.fei.gono.java.errors.ReportedOverlimitTransactionBadRequestException;
import stuba.fei.gono.java.errors.ReportedOverlimitTransactionNotFoundException;
import stuba.fei.gono.java.nonblocking.errors.ReportedOverlimitTransactionValidationException;
import stuba.fei.gono.java.nonblocking.pojo.ReportedOverlimitTransaction;

/***
 * Interface for marshalling and de-marshalling ReportedOverlimitTransaction  entities.
 */
public interface ReportedOverlimitTransactionService {
     /***
      * Generates a new id and saves the entity.
      * @param transaction entity to be saved.
      * @return Mono emitting the saved entity or Mono.error(ReportedOverlimitTransactionValidationException) if
      * the entity is not valid.
      * @see ReportedOverlimitTransactionValidationException
      */
     Mono<ReportedOverlimitTransaction> postTransaction(ReportedOverlimitTransaction transaction);
     /***
      * Return entity with the given id.
      * @param id must not be null.
      * @return Mono emitting the entity or Mono.empty() if there is none.
      * @see ReportedOverlimitTransactionValidationException
      */
     Mono<ReportedOverlimitTransaction> getTransactionById(String id);

     /***
      * Saves the entity with the given id.
      * @param id id identifying the saved entity.
      * @param transaction entity to be saved
      * @return Mono emitting the saved entity or Mono.error(ReportedOverlimitTransactionValidationException) if
      * the entity is not valid.
      * @see ReportedOverlimitTransactionValidationException
      */
     Mono<ReportedOverlimitTransaction> putTransaction(String id, ReportedOverlimitTransaction transaction);

     /***
      * Deletes the entity identified by the given id.
      * @param id id of entity to be deleted.
      * @return Mono emitting when the operation was completed,  Mono.error(ReportedOverlimitTransactionNotFoundException)
      * if the entity with given id was not found or Mono.error(ReportedOverlimitTransactionBadRequestException) if the
      * entity couldn't be deleted because its state is CLOSED.
      * @see  ReportedOverlimitTransactionNotFoundException
      * @see ReportedOverlimitTransactionBadRequestException
      */
     Mono<Void> deleteTransaction (String id);

}
