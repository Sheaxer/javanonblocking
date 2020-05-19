package stuba.fei.gono.java.nonblocking.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;
import stuba.fei.gono.java.security.SecurityConstants;

//@Configuration

/***
 * <div class="en">Class setting up web security - exposes POST operation access to endpoint /login and to /signup
 * without authorization, and requires valid JWT to access every other endpoint.</div>
 * <div class="sk">Trieda, ktorá nastavuje webovú bezpečnosť - povolí prístup cez POST operáciu na endpoint-y
 * /login a /signup, a nastaví vyžadovanie správneho JWT pre prístup k ostatným endpoint-om.</div>
 */
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfigurationImpl {

    private final SecurityContextRepository securityContextRepository;
    private final ReactiveAuthenticationManager reactiveAuthenticationManager;

    @Autowired
    public SecurityConfigurationImpl(SecurityContextRepository  securityContextRepository,
                                     ReactiveAuthenticationManager reactiveAuthenticationManager) {
        this.securityContextRepository = securityContextRepository;
        this.reactiveAuthenticationManager = reactiveAuthenticationManager;
    }

    /***
     * <div class="en">Sets up security chain - if not authorized returns Http code 401 - Unauthorized, when fails
     * the authorization process returns HTTP code 403 - Forbidden, disables the default authorization methods
     * and adds instances of SecurityContextRepository and JWTAuthenticationManager to handle authorization to system.
     * Permits access to /login and to /signup endpoints and prevents unauthorized access to all other endpoints.</div>
     * <div class="sk">Nastaví reťaz bezpečnostných filtrov: pri prístupu do systému bez autorizácie vráti HTTP kód 401
     * - Unauthorized, pri nesprávnej autorizácii vráti HTTP kód 403 - Forbidden, zakáže použitie prednastavených
     * autorizačných metód a pridá inštancie tried SecurityContextRepository a JWTAuthenticationManager ako správcov
     * autorizácie do systému. Umožní neautorizovaný prístup k /login a /signup endpointom, a zakáže neautorizaovaný
     * prístup ku všetkým ostatným endpointom.</div>
     * @param http
     * @return <div class="en">configured security filter chain.</div>
     * <div class="sk">konfigurovaná reťaz bezpečnostných filtrov.</div>
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        /* handle exceptions during authorization */
      return http.exceptionHandling()
              /* no authorization attempted */
               .authenticationEntryPoint(((serverWebExchange, e) ->
                       Mono.fromRunnable(() ->
                               /* return HTTP CODE UNAUTHORIZED */
                               serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))).
                      /* authorization failed */
                      accessDeniedHandler((serverWebExchange, e) -> Mono.fromRunnable(()->
                              /* return HTTP CODE FORBIDDEN  */
                              serverWebExchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN)))
              .and()
              /* disables CSRF */
               .csrf().disable()
              /* disables formLogin */
               .formLogin().disable()
              /* disables http basic authentication */
               .httpBasic().disable()
               .authenticationManager(reactiveAuthenticationManager)
               .securityContextRepository(securityContextRepository)
               .authorizeExchange()
               .pathMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL).permitAll()
               .pathMatchers(HttpMethod.POST,"/login").permitAll()
               .anyExchange().authenticated()
               .and().build();
    }

    /*@Bean public ReactiveAuthenticationManager authenticationManager()
    {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(reactiveUserDetailsService);
        authenticationManager.setPasswordEncoder(bCryptPasswordEncoder);
        return authenticationManager;
    }*/

   /* @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }*/

}
