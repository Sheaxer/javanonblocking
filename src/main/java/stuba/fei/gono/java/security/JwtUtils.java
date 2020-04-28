package stuba.fei.gono.java.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static stuba.fei.gono.java.security.SecurityConstants.*;

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
            return JWT.require(Algorithm.HMAC512(SECRET_KEY.getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""))
                    .getSubject();
        } catch(com.auth0.jwt.exceptions.JWTDecodeException ex)
        {
            return null;
        }

    }
}
