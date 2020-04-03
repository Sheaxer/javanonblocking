package stuba.fei.gono.java.nonblocking.mongo.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.pojo.Employee;

@Repository
public interface EmployeeRepository extends ReactiveCrudRepository<Employee, String> {

    Mono<Employee> findByUserName(String userName);
}
