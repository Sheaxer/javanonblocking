package stuba.fei.gono.java.nonblocking.services;

import reactor.core.publisher.Mono;
import stuba.fei.gono.java.pojo.Employee;

import java.util.Optional;

/***
 * Interface for marshalling and de-marshalling Employee entities.
 */
public interface EmployeeService {
    /***
     * Retrieves the entity identified by the given User Name.
     * @param userName - User Name identifying the entity.
     * @return Mono emitting the entity or Mono.empty() if no entity was found.
     */
    Mono<Employee> findEmloyeeByUsername(String userName);
    /***
     * Saves the entity.
     * @param employee entity to be saved.
     * @return Mono emitting the saved entity.
     */
    Mono<Employee> saveEmployee(Employee employee);
    /***
     * Checks if the entity identified by the given id was found.
     * @param id - must not be null.
     * @return Mono emitting true if the entity was found, false otherwise.
     */
    Mono<Boolean> employeeExistsById(String id);

}
