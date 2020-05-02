package stuba.fei.gono.java.nonblocking.mongo.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.errors.ReportedOverlimitTransactionBadRequestException;
import stuba.fei.gono.java.nonblocking.errors.ReportedOverlimitTransactionValidationException;
import stuba.fei.gono.java.nonblocking.mongo.repositories.EmployeeRepository;
import stuba.fei.gono.java.nonblocking.services.EmployeeService;
import stuba.fei.gono.java.nonblocking.validation.EmployeeValidator;
import stuba.fei.gono.java.pojo.Employee;

import java.util.Objects;
import java.util.stream.Collectors;

/***
 * MongoDB implementation of service managing marshalling and de-marshalling Employee objects.
 */
@Service
public class EmployeeServiceImple implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeValidator employeeValidator;
    private final NextSequenceService nextSequenceService;

    @Value("${reportedOverlimitTransaction.employee.sequenceName:employeeSequence}")
    private String sequenceName;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public EmployeeServiceImple(EmployeeRepository employeeRepository, EmployeeValidator employeeValidator,
                                NextSequenceService nextSequenceService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.employeeRepository = employeeRepository;
        this.employeeValidator = employeeValidator;
        this.nextSequenceService = nextSequenceService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }



    /***
     * Retrieves Employee by its user name.
     * @param userName user name of Employee, must be not null
     * @return Mono emitting the Employee of Mono.empty() if none found.
     */
    @Override
    public Mono<Employee> findEmployeeByUsername(String userName) {
        return employeeRepository.findByUsername(userName);
    }

    /***
     * Saves the Employee
     * @param employee must not be null.
     * @return Mono emitting the saved Employee
     */
    @Override
    public Mono<Employee> saveEmployee(Employee employee) {
        return validate(employee).then(
                employeeRepository.existsByUsername(employee.getUsername()).flatMap(
                        exists -> {
                            if(!exists)
                                return nextSequenceService.getNewId(employeeRepository,sequenceName).flatMap(
                                        id ->
                                        {
                                            employee.setId(id);
                                            employee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));
                                            return  employeeRepository.save(employee);
                                        }
                                );
                            else
                                return Mono.error(
                                        new ReportedOverlimitTransactionBadRequestException("USERNAME_EXISTS"));
                        }
                )
        );
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

    @Override
    public Mono<Void> validate(Employee employee)
    {
       return Mono.just(employee).flatMap(
                e -> {
                    Errors errors = new BeanPropertyBindingResult(e,Employee.class.getName());
                    employeeValidator.validate(e,errors);
                    if(errors.hasErrors()) {
                        return Mono.error(new ReportedOverlimitTransactionValidationException(
                                errors.getAllErrors().stream().map(
                                        objectError ->
                                                Objects.requireNonNull(objectError.getCodes())[objectError.
                                                        getCodes().length - 1]
                                ).collect(Collectors.toList())
                        ));
                    }
                    else return Mono.just(true).then();
                }

        );
    }
}
