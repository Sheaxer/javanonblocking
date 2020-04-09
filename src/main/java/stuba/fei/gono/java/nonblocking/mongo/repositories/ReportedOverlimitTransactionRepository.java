package stuba.fei.gono.java.nonblocking.mongo.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import stuba.fei.gono.java.pojo.ReportedOverlimitTransaction;

@Repository
public interface ReportedOverlimitTransactionRepository extends ReactiveMongoRepository<ReportedOverlimitTransaction, String> {
}
