package stuba.fei.gono.java.nonblocking.mongo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.nonblocking.mongo.repositories.EmployeeRepository;
import stuba.fei.gono.java.nonblocking.services.EmployeeService;
import stuba.fei.gono.java.pojo.Employee;

@Service
public class EmployeeServiceImple implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImple(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Mono<Employee> findEmloyeeByUsername(String userName) {
        return employeeRepository.findByUsername(userName);
    }

    @Override
    public Mono<Employee> saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Mono<Boolean> employeeExistsById(String id) {
        return employeeRepository.existsById(id);
    }
}
