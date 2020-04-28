package stuba.fei.gono.java.nonblocking.mongo.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.pojo.Employee;
/***
 * Interface extending ReactiveMongoRepository for Employee entities.
 * @see Employee
 * @see org.springframework.data.repository.reactive.ReactiveCrudRepository
 */
@Repository
public interface EmployeeRepository extends ReactiveCrudRepository<Employee, String> {
    /***
     * Retrieves the entity with the given user name.
     * @param userName user name of the required entity.
     * @return Mono emitting the entity or Mono.empty() if no entity was found.
     */
    Mono<Employee> findByUsername(String userName);

    Mono<Boolean> existsByUsername(String username);
}
