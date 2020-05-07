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
import stuba.fei.gono.java.security.SecurityConstants;

/***
 * <div class="en">Rest controller that generates JWT that allows access to REST
 * resources upon successfully providing correct username and corresponding password.</div>
 * <div class="sk">Rest kontrolér ktorý generuje JWT ktorý umožňuje prístup k REST
 * prostriedkom po úspešnom zadaní používateľského mena a zodpovedajúceho hesla.</div>
 */
@RestController
public class LoginController {
    /***
     * <div class="en">Password encoder used to encode the given password
     * to check if is the same as stored password.</div>
     * <div class="sk">Enkóder hesiel použitý na zakódovanie doručeného hesla ktoré sa porovná s uloženým
     * heslom.</div>
     */
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /***
     * <div class="en">Service used to retrieve and save Employee entities.</div>
     * <div class="sk">Služba použitá na získanie a uloženie entít triedy Employee.</div>
     */
    private final EmployeeService employeeService;
    @Autowired
    public LoginController(BCryptPasswordEncoder bCryptPasswordEncoder,
                           EmployeeService employeeService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.employeeService = employeeService;
    }

    /***
     * <div class="en">Rest POST method on /login endpoint. If HTTP request contains valid entity with correct
     * username and corresponding passwords returns a JWT that allows
     * access to resources in the Authorization header of Http response.</div>
     * <div class="sk">Rest POST metóda na /login endpointe. Ak obsah HTTP požiadavky obsahuje validnú
     * entitu triedy Employee s koretkným používateľským menom a správnym korešpondujúcim heslom
     * HTTP odvoveď bude v hlavičke Authorization obsahovať JWT ktorý umožňuje prístup k REST prostriedkom.
     * </div>
     * @param employee <div class="en">entity representing employee who wants to log into the system and gain
     *                 access to REST resources.</div>
     *                 <div class="sk">entita reprezentujúca zamestnanca, ktorý požiada o prihlásenie do systému
     *                 a získať prístup k REST prostriedkom.</div>
     * @return <div class="en">ResponseEntity with HTTP code OK - 200 with the JWT in the Authorization header if the
     * log in was successful,
     * or ResponseEntity with HttpStatus code UNAUTHORIZED - 401 if the log in was unsuccessful</div>
     * <div class="sk">ResponseEntity s HTTP kódom OK - 200 a s JWT v Authorization sekcii http hlavičky ak
     * bolo prihlásenie úspešné alebo ResponseEntity s HTTP kódom UNAUTHORIZED - 401 ak prihlásenie zlyhalo.</div>
     */
    @PostMapping(value = "/login", consumes = "application/json")
    public Mono<ResponseEntity<Object>> login(@RequestBody Employee employee)
    {

            return   employeeService.validate(employee).then(
                    /* retrieves an entity from username */
                    employeeService.findEmployeeByUsername(employee.getUsername()).map(
                    user -> {
                    /* encodes the password from payload and checks if the retrieved entity has the same password */
                            if(bCryptPasswordEncoder.matches(employee.getPassword(),user.getPassword()))
                            {
                                /* create HTTP header of the response - add generated JWT to Authorization header */
                                HttpHeaders responseHeaders = new HttpHeaders();
                                responseHeaders.set(HttpHeaders.AUTHORIZATION, JwtUtils.createJWT(employee.getUsername()));
                                return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).build();
                                //return ResponseEntity.ok(JwtUtils.createJWT(employee.getUsername()));
                            }
                            else
                                /* passwords don't match */
                                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                    }
                /* employee with the given username does not exist */
        ).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }


}
