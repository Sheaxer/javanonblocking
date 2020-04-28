package stuba.fei.gono.java.nonblocking.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.nonblocking.services.EmployeeService;
import stuba.fei.gono.java.pojo.Employee;

@RestController
public class SignUpController {
    private final EmployeeService employeeService;

    public SignUpController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/signup")
    public Mono<String> signUp(@RequestBody Employee user)
    {
        return employeeService.saveEmployee(user).then(Mono.just("SUCCESSFULLY_REGISTERED"));
    }
}
