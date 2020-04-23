package stuba.fei.gono.java.nonblocking.mongo.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.pojo.Account;
import stuba.fei.gono.java.pojo.Employee;
/***
 * Interface extending ReactiveMongoRepository for Employee entities.
 * @see Employee
 * @see org.springframework.data.repository.reactive.ReactiveCrudRepository
 */
@Repository
public interface EmployeeRepository extends ReactiveCrudRepository<Employee, String> {

    Mono<Employee> findByUsername(String userName);
}
