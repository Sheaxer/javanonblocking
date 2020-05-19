package stuba.fei.gono.java.nonblocking.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.nonblocking.services.EmployeeService;

import java.util.ArrayList;

/***
 * <div class="en">Class implementing ReactiveAuthenticationManager interface.
 * Provides the authentication via validating a JWT.</div>
 * <div class="sk">Trieda implementujúca ReactiveAuthenticationManager rozhranie.
 * Poskytuje validáciu pomocou správneho JWT.</div>
 * @see ReactiveAuthenticationManager
 * @see com.auth0.jwt.JWT
 */
@Component
public class JWTAuthenticationManager implements ReactiveAuthenticationManager {

    private final EmployeeService employeeService;

    @Autowired
    public JWTAuthenticationManager(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    /*private final ReactiveUserDetailsService reactiveUserDetailsService;

    public JWTAuthenticationManager(ReactiveUserDetailsService reactiveUserDetailsService) {
        this.reactiveUserDetailsService = reactiveUserDetailsService;
    }*/

    /***
     * <div class="en">Validates if the credentials of Authentication object contains contain valid JWT with
     * a subject.</div>
     * <div class="sk">Validuje či credentials premenná objektu triedy Authentication obsahuje korektný JWT so
     * subjektom.</div>
     * @param authentication <div class="en">authentication information</div>
     *                       <div class="sk">autentikačné informácie</div>
     * @return <div class="en">authentication token with user name</div>
     * <div class="sk">autentifikačný token s používateľským menom.</div>
     */
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        String user = JwtUtils.getUserFromToken(token);
        if(user!= null)
        {
            // checks if employee with that user name exists
           return employeeService.employeeExistsByUsername(user).flatMap(
                    b ->
                    {
                        if(b)
                        {
                            UsernamePasswordAuthenticationToken auth =
                                    new UsernamePasswordAuthenticationToken(user,null,new ArrayList<>());
                            // SecurityContextHolder.getContext().setAuthentication(auth);
                            return  Mono.just(auth);
                        }
                        else
                            return Mono.empty();
                    }
            );

        }
        else return Mono.empty();
    }


}
