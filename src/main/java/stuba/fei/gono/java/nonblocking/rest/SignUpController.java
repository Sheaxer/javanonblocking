package stuba.fei.gono.java.nonblocking.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.nonblocking.services.EmployeeService;
import stuba.fei.gono.java.pojo.Employee;

/***
 * <div class="en">REST controller allowing to register new employees - users of the system.</div>
 * <div class="sk">REST kontrolér umožňujúci registrovať nových zamestnancov - používateľov systému.</div>
 */
@RestController
public class SignUpController {
    /***
     * <div class="en">Service allowing to retrieve and save entities.</div>
     * <div class="sk">Služba umožňujúca získať a uložiť entity.</div>
     */
    private final EmployeeService employeeService;

    public SignUpController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /***
     * <div class="en">POST method attached to the endpoint /signup. Validates the entity and then saves it
     * if valid.</div>
     * <div class="sk">POST metóda pripojená na endpoint /signup. Validuje entitu a ak je korektná, uloží ju.</div>
     * @param user <div class="en">entity representing new employee to be registered as a user of the system.</div>
     *             <div class="sk">entita reprezentujúca nového zamestnanca ktorý má byť zaregistrovaný ako
     *             nový používateľ systému.</div>
     * @return <div class="en">Mono emitting the confirmation message.</div>
     * <div class="sk">Mono emitujúce potvrdzovaciu správu.</div>
     */
    @PostMapping("/signup")
    public Mono<String> signUp(@RequestBody Employee user)
    {
        return employeeService.saveEmployee(user).then(Mono.just("SUCCESSFULLY_REGISTERED"));
    }
}
