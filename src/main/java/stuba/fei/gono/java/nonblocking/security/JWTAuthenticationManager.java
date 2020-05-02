package stuba.fei.gono.java.nonblocking.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Component
public class JWTAuthenticationManager implements ReactiveAuthenticationManager {

    /*private final ReactiveUserDetailsService reactiveUserDetailsService;

    public JWTAuthenticationManager(ReactiveUserDetailsService reactiveUserDetailsService) {
        this.reactiveUserDetailsService = reactiveUserDetailsService;
    }*/

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        String user = JwtUtils.getUserFromToken(token);
        if(user!= null)
        {

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(user,null,new ArrayList<>());
           // SecurityContextHolder.getContext().setAuthentication(auth);
            return  Mono.just(auth);
        }
        else return Mono.empty();
    }


}
