package ar.edu.unq.desapp.grupog.criptop2p.config.filter;

import ar.edu.unq.desapp.grupog.criptop2p.config.utils.JwtUtil;
import ar.edu.unq.desapp.grupog.criptop2p.dto.ErrorMessageResponseBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().equals("/api/user/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeaderIsValid(authorizationHeader)) {
            try {
                String token = authorizationHeader.substring("Bearer ".length());

                String userEmail = JwtUtil.decodeUserEmail(token);
                List<String> roles = JwtUtil.decodeUserRoles(token);

                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userEmail, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                filterChain.doFilter(request, response);
            } catch (Exception exception) {
                log.error("Authorization attempt failed: {}", exception.getMessage());

                ObjectWriter objectWriter =
                        new ObjectMapper()
                                .registerModule(new JavaTimeModule())
                                .writer()
                                .withDefaultPrettyPrinter();

                ErrorMessageResponseBody errorMessageResponseBody =
                        new ErrorMessageResponseBody(
                                FORBIDDEN.value(),
                                LocalDateTime.now(),
                                FORBIDDEN.getReasonPhrase(),
                                exception.getMessage());

                response.setStatus(FORBIDDEN.value());
                response.setContentType("application/json");
                response.getWriter().write(objectWriter.writeValueAsString(errorMessageResponseBody));
            }

        } else {
            filterChain.doFilter(request, response);
        }

    }

    private boolean authorizationHeaderIsValid(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }

}
