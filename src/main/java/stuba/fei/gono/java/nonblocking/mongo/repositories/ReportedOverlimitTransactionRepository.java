package stuba.fei.gono.java.nonblocking.mongo.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import stuba.fei.gono.java.nonblocking.pojo.ReportedOverlimitTransaction;

@Repository
public interface ReportedOverlimitTransactionRepository extends ReactiveMongoRepository<ReportedOverlimitTransaction, String> {
}
