package stuba.fei.gono.java.nonblocking.mongo.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import stuba.fei.gono.java.nonblocking.pojo.ReportedOverlimitTransaction;

/***
 * <div class="en">Interface extending ReactiveCrudRepository for ReportedOverlimitTransaction entities.
 * Automatically instantiated by Spring.</div>
 * <div class="sk">Rozhranie rozširujúce ReactiveCrudRepository pre entity triedy ReportedOverlimitTransaction.
 *  Automaticky inštanciované pomocou Spring.</div>
 * @see ReportedOverlimitTransaction
 * @see org.springframework.data.repository.reactive.ReactiveCrudRepository
 */
@Repository
public interface ReportedOverlimitTransactionRepository extends
        ReactiveCrudRepository<ReportedOverlimitTransaction, String> {
}
