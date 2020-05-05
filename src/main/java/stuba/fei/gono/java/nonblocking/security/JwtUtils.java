package stuba.fei.gono.java.nonblocking.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static stuba.fei.gono.java.security.SecurityConstants.*;

/***
 * <div class="en">Class implementing JWT helper functions.</div>
 * <div class="sk">Trieda implementujúca JWT pomocné funkcie.</div>
 */
@Component
public class JwtUtils implements Serializable {
    /***
     * <div class="en">Creates JWT from given user name, with expiration date obtained from SecurityConstants class,
     * signed with the key from SecurityConstants class.</div>
     * <div class="sk">Vytvorí JWT pomocou zadaného používateľského mena, s expiračným dátumom získaným
     * z triedy SecurityConstants a podpísaný kľúčom získaným z triedy SecurityConstants.</div>
     * @see stuba.fei.gono.java.security.SecurityConstants
     * @param username <div class="en">user name to be added as subject to the JWT.</div>
     *                 <div class="sk">používateľské meno, ktoré sa má pridať ako subjekt JWT.</div>
     * @return JWT
     */
    public static String createJWT(String username)
    {
       return TOKEN_PREFIX + JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_LENGTH))
                .sign(HMAC512(SECRET_KEY.getBytes()));
    }

    /***
     * <div class="en">Retrieves the subject from JWT, if the JWT is valid and not expired.</div>
     * <div class="sk">Získa subjekt z JWT, ak je JWT validný a nebol expirovaný.</div>
     * @param token JWT
     * @return <div class="en">user name that is the subject of the JWT, if JWT was valid, null otherwise.</div>
     * <div class="sk">používateľské meno ktoré je subjekt JWT, ak je JWT validný, inak null.</div>
     */
    public static String getUserFromToken(String token)
    {
        try{
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET_KEY.getBytes()))
                    .build()
                    /* Remove "Bearer " from the beginning of the string */
                    .verify(token.replace(TOKEN_PREFIX, ""));
            /* JWT is not expired */
            if(new Date().before(decodedJWT.getExpiresAt()))
            {
                return decodedJWT.getSubject();
            }
            /* JWT is expired */
            else
            {
                //log.info(decodedJWT.getExpiresAt().toString());
                return null;
            }
            /* JWT is not valid */
        } catch(com.auth0.jwt.exceptions.JWTDecodeException ex)
        {
            return null;
        }

    }

}
