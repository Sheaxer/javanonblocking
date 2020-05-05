package stuba.fei.gono.java.nonblocking.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static stuba.fei.gono.java.security.SecurityConstants.TOKEN_PREFIX;

/***
 * <div class="en">Class implementing ServerSecurityContextRepository - loads
 * Authorization header of the HTTP request and checks if it contains valid non expired JWT.</div>
 * <div class="sk">Trieda implementujúca ServerSecurityContextRepository - načíta Authorization hlavičku
 * z HTTP požiadavky a skontroluje či obsahuje korektný a neexpirovaný JWT.</div>
 * @see ServerSecurityContextRepository
 */
@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {
    /***
     * <div class="en">Performs the authentication on the JWT.</div>
     * <div class="sk">Vykoná overenie JWT.</div>
     */
    private final ReactiveAuthenticationManager authenticationManager;

    @Autowired
    public SecurityContextRepository(ReactiveAuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Mono<Void> save(ServerWebExchange serverWebExchange, SecurityContext securityContext) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /***
     * <div class="en">Retrieves the Authorization header of HTTP request, checks if it contains
     * JWT and validates it.</div>
     * <div class="sk">Načíta Authorization sekciu hlavičky HTTP požiadavky, skontroluje či táto sekcia
     * obsahuje JWT a validuje JWT.</div>
     * @param serverWebExchange <div class="en">provides access to HTTP request and response.</div>
     *                          <div class="sk">poskytuje prístup k HTTP požiadavke a odpovedi.</div>
     * @return <div class="en">Mono emitting SecurityContextImpl if authorization was successful or Mono.empty().</div>
     * otherwise.
     * <div class="sk">Mono emitujúce SecurityContextImpl obsahujúce ak autorizácia prebehla úspešne,
     * Mono.empty() inak.</div>
     */
    @Override
    public Mono<SecurityContext> load(ServerWebExchange serverWebExchange) {
        ServerHttpRequest request = serverWebExchange.getRequest();
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
            // seems like a hack?
            Authentication auth = new UsernamePasswordAuthenticationToken(authHeader, authHeader);
            return this.authenticationManager.authenticate(auth).map(SecurityContextImpl::new);

        }
        return Mono.empty();
    }
}
