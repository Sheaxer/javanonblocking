package stuba.fei.gono.java.nonblocking.mongo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.nonblocking.mongo.repositories.EmployeeRepository;
import stuba.fei.gono.java.nonblocking.services.EmployeeService;
import stuba.fei.gono.java.pojo.Employee;

/***
 * MongoDB implementation of service managing marshalling and de-marshalling Employee objects.
 */
@Service
public class EmployeeServiceImple implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImple(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /***
     * Retrieves Employee by its user name.
     * @param userName user name of Employee, must be not null
     * @return Mono emitting the Employee of Mono.empty() if none found.
     */
    @Override
    public Mono<Employee> findEmloyeeByUsername(String userName) {
        return employeeRepository.findByUsername(userName);
    }

    /***
     * Saves the Employee
     * @param employee must not be null.
     * @return Mono emitting the saved Employee
     */
    @Override
    public Mono<Employee> saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    /***
     * Checks if Employee with given id exists.
     * @param id - must be not null.
     * @return Mono emitting true if Employee exists, false otherwise.
     */
    @Override
    public Mono<Boolean> employeeExistsById(String id) {
        return employeeRepository.existsById(id);
    }
}
