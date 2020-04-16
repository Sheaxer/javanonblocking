package stuba.fei.gono.java.nonblocking.mongo.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.pojo.Employee;

@Repository
public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {

    Mono<Employee> findByUsername(String userName);
}
