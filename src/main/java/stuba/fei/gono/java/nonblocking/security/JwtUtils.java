package stuba.fei.gono.java.nonblocking.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static stuba.fei.gono.java.security.SecurityConstants.*;
@Slf4j
@Component
public class JwtUtils implements Serializable {

    public static String createJWT(String username)
    {
       return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_LENGTH))
                .sign(HMAC512(SECRET_KEY.getBytes()));
    }

    public static String getUserFromToken(String token)
    {
        try{
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET_KEY.getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""));

            if(new Date().before(decodedJWT.getExpiresAt()))
            {
                return decodedJWT.getSubject();
            }
            else
            {
                log.info(decodedJWT.getExpiresAt().toString());
                return null;
            }

        } catch(com.auth0.jwt.exceptions.JWTDecodeException ex)
        {
            return null;
        }

    }

}
