package stuba.fei.gono.java.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

//@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfigurationImpl {

    private final SecurityContextRepository  securityContextRepository;
    private ReactiveAuthenticationManager reactiveAuthenticationManager;

    public SecurityConfigurationImpl(SecurityContextRepository  securityContextRepository,
                                     ReactiveAuthenticationManager reactiveAuthenticationManager) {
        this.securityContextRepository = securityContextRepository;
        this.reactiveAuthenticationManager = reactiveAuthenticationManager;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

      return http.exceptionHandling()
               .authenticationEntryPoint(((serverWebExchange, e) ->
                       Mono.fromRunnable(() ->
                               serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))).
                      accessDeniedHandler((serverWebExchange, e) -> Mono.fromRunnable(()->
                              serverWebExchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN)))
              .and()
               .csrf().disable()
               .formLogin().disable()
               .httpBasic().disable()
               .authenticationManager(reactiveAuthenticationManager)
               .securityContextRepository(securityContextRepository)
               .authorizeExchange()
               .pathMatchers(HttpMethod.POST,SecurityConstants.SIGN_UP_URL).permitAll()
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
