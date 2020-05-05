package stuba.fei.gono.java.nonblocking.mongo.services;

import org.springframework.beans.factory.annotation.Value;
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
 * <div class="en">Implementation of service managing marshalling and de-marshalling Employee objects using CRUD
 * operations and EmployeeRepository instance.</div>
 * <div class="sk">Implementácia služby ktorá spravuje marshalling a de-marshalling objektov triedy Employee pomocou
 * CRUD operácií a využíva inštanciu rozhrania EmployeeRepository.</div>
 * @see org.springframework.data.repository.reactive.ReactiveCrudRepository
 * @see EmployeeRepository
 */
@Service
public class EmployeeServiceImple implements EmployeeService {
    /***
     * <div class="en">Repository that provides CRUD operations on Employee entities.</div>
     * <div class="sk">Repozitár ktorý poskytuje CRUD operácie nad entitami triedy Employee.</div>
     * @see org.springframework.data.repository.reactive.ReactiveCrudRepository
     */
    private final EmployeeRepository employeeRepository;

    /***
     * <div class="en">Validator for checking if entity is valid before saving it.</div>
     * <div class="sk">Validátor na kontrolu korektnosti entity pred jej uložením.</div>
     */
    private final EmployeeValidator employeeValidator;

    /***
     * <div class="en">Service that generates next id for saving a new entity.</div>
     * <div class="sk">Služba poskytujúca generáciu nasledujúceho id pre uloženie nove entity.</div>
     */
    private final NextSequenceService nextSequenceService;

    /***
     * <div class="en">Name of the sequence that stores information necessary to generate new id.</div>
     * <div class="sk">Názov sekvencie ktorá obsahuje potrebné informácie na generáciu nového id.</div>
     * @see stuba.fei.gono.java.pojo.SequenceId
     * @see NextSequenceService
     */
    @Value("${reportedOverlimitTransaction.employee.sequenceName:employeeSequence}")
    private String sequenceName;

    /***
     * <div class="en">Password encoder that encodes open text passwords into salted hash.</div>
     * <div class="sk">Kódovač hesiel ktorá kóduje otvorený text hesla na salted hash.</div>
     */
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public EmployeeServiceImple(EmployeeRepository employeeRepository, EmployeeValidator employeeValidator,
                                NextSequenceService nextSequenceService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.employeeRepository = employeeRepository;
        this.employeeValidator = employeeValidator;
        this.nextSequenceService = nextSequenceService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public Mono<Employee> findEmployeeByUsername(String userName) {
        return employeeRepository.findByUsername(userName);
    }

    /***
     * <div class="en">Validates the given entity and if valid saves it. Encodes the password before
     * saving the entity.</div>
     * <div class="sk">Validuje entitu a ak je korektná, uloží ju. Pred uložením zakóduje heslo.</div>
     * @param employee <div class="en">entity to be saved, must not be null.</div>
     *                 <div class="sk">entita na uloženie, nesmie byť null.</div>
     * @return <div class="en">Mono emitting the saved entity or Mono.error containing
     * ReportedOverlimitTransactionBadRequestException if entity with the same user name already exists.</div>
     * <div class="sk">Mono emitujúce uloženú entitu alebo Mono.error() obsahujúce výnimku
     * ReportedOverlimitTransactionBadRequestException ak entita so zadaným používateľským menom už existuje.</div>
     */
    @Override
    public Mono<Employee> saveEmployee(Employee employee) {
        //validation
        return validate(employee).then(
                // checks if employee with the same username already exists
                employeeRepository.existsEmployeeByUsername(employee.getUsername()).flatMap(
                        exists -> {
                            if(!exists)
                                // new id generation
                                return nextSequenceService.getNewId(employeeRepository,sequenceName).flatMap(
                                        id ->
                                        {
                                            employee.setId(id);
                                            // encoding the password - salt+hash using bcrypt
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

    @Override
    public Mono<Boolean> employeeExistsById(String id) {
        return employeeRepository.existsById(id);
    }

    /***
     * <div class="en">Validates the entity. Uses the EmployeeValidator to check if entity contains both username
     * and password.</div>
     * <div class="sk">Validuje entitu. Využíva objekt triedy EmployeeValidator na kontrolu, či entita obsahuje
     * užívateľské meno a heslo zároveň.</div>
     * @see EmployeeValidator
     * @param employee <div class="en">entity to be validated, must not be null.</div>
     *                 <div class="sk">entita na validáciu, nesmie byť null</div>
     * @return <div class="en">Mono emitting true if entity is valid, Mono.error() containing
     * ReportedOverlimitTransactionValidationException with error codes of failed properties that failed
     * validation.</div>
     * <div class="sk">Mono emitujúce true ak je entita korektná, Mono.error() emitujúca
     * ReportedOverlimitTransactionValidationException výnimku ktorá obsahuje kódy nekorektných
     * premenných entity.</div>
     * @see ReportedOverlimitTransactionValidationException
     */
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

    @Override
    public Mono<Boolean> employeeExistsByUsername(String username) {
        return employeeRepository.existsEmployeeByUsername(username);
    }
}
