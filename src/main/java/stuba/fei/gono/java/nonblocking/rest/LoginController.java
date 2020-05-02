package stuba.fei.gono.java.nonblocking.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.nonblocking.services.EmployeeService;
import stuba.fei.gono.java.pojo.Employee;
import stuba.fei.gono.java.nonblocking.security.JwtUtils;

@RestController
public class LoginController {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmployeeService employeeService;
    @Autowired
    public LoginController(BCryptPasswordEncoder bCryptPasswordEncoder,
                           EmployeeService employeeService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.employeeService = employeeService;
    }

    @PostMapping(value = "/login", consumes = "application/json")
    public Mono<ResponseEntity<Object>> login(@RequestBody Employee employee)
    {

            return   employeeService.validate(employee).then(
                    employeeService.findEmployeeByUsername(employee.getUsername()).map(
                user -> {
                    if(bCryptPasswordEncoder.matches(employee.getPassword(),user.getPassword()))
                    {
                        HttpHeaders responseHeaders = new HttpHeaders();
                        responseHeaders.set("Authorization","Bearer " + JwtUtils.createJWT(employee.getUsername()));
                        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).build();
                        //return ResponseEntity.ok(JwtUtils.createJWT(employee.getUsername()));
                    }
                    else
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
        ).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }


}
