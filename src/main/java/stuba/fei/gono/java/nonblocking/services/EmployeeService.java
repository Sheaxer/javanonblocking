package stuba.fei.gono.java.nonblocking.services;

import reactor.core.publisher.Mono;
import stuba.fei.gono.java.pojo.Employee;

import java.util.Optional;


public interface EmployeeService {

    Mono<Employee> findEmloyeeByUsername(String userName);

    boolean saveEmployee(Employee employee);

}
