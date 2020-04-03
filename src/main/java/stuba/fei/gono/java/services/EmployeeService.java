package stuba.fei.gono.java.services;

import reactor.core.publisher.Mono;
import stuba.fei.gono.java.pojo.Employee;

import java.util.Optional;


public interface EmployeeService {

    Employee findEmloyeeByUsername(String userName);

    boolean saveEmployee(Employee employee);

}
