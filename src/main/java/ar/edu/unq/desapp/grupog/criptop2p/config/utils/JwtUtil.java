package ar.edu.unq.desapp.grupog.criptop2p.config.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public final class JwtUtil {

    private JwtUtil() {
    }

    private static final String JWT_SECRET = "no_so_secret";
    private static final Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET.getBytes());

    public static String generateAccessToken(User userDetails, String issuer) {
        Date shortLifeSpam = new Date(System.currentTimeMillis() + 10 * 60 * 1000);
        return JWT
                .create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(shortLifeSpam)
                .withIssuer(issuer)
                .withClaim("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    private static DecodedJWT decodeToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();

        return verifier.verify(token);
    }

    public static String decodeUserEmail(String token) {
        return decodeToken(token).getSubject();
    }

    public static List<String> decodeUserRoles(String token) {
        return List.of(decodeToken(token).getClaim("roles").asArray(String.class));
    }

}
