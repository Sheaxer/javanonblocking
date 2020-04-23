package stuba.fei.gono.java.nonblocking.mongo.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import stuba.fei.gono.java.nonblocking.pojo.ReportedOverlimitTransaction;

/***
 * Interface extending ReactiveMongoRepository for ReportedOverlimitTransaction entities.
 * @see ReportedOverlimitTransaction
 * @see org.springframework.data.repository.reactive.ReactiveCrudRepository
 */
@Repository
public interface ReportedOverlimitTransactionRepository extends
        ReactiveCrudRepository<ReportedOverlimitTransaction, String> {
}
