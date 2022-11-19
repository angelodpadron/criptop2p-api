package ar.edu.unq.desapp.grupog.criptop2p.config.filter;

import ar.edu.unq.desapp.grupog.criptop2p.config.utils.JwtUtil;
import ar.edu.unq.desapp.grupog.criptop2p.dto.LoginRequestBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            LoginRequestBody loginRequestBody = new ObjectMapper().readValue(request.getInputStream(), LoginRequestBody.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestBody.getEmail(), loginRequestBody.getPassword());
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        User userDetails = (User) authentication.getPrincipal();
        String accessToken = JwtUtil.generateAccessToken(userDetails, request.getRequestURL().toString());
        log.info("Token generated for user '{}'", userDetails.getUsername());
        response.setHeader(AUTHORIZATION, accessToken);

    }

}
