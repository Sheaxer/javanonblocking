package stuba.fei.gono.java.security;

import org.springframework.beans.factory.annotation.Value;

/***
 * <div class="en">Class holding constants used in security implementation</div>
 * <div class="sk">Trieda, ktorá udržiava konštanty použité v implementácii
 * bezpečnosti.</div>
 */
public class SecurityConstants {
    @Value("${security.jwt.token.expire-length:864_000_000}")
    public static final long EXPIRE_LENGTH = 864_000_000;

    @Value("${security.jwt.token.secret-key:SecretKeyToGenJWTs}")
    public static final String SECRET_KEY="SecretKeyToGenJWTs";

    public static final String HEADER_STRING = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String SIGN_UP_URL = "/signup";
}
