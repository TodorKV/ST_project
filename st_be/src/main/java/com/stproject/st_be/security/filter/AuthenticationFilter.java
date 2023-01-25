package com.stproject.st_be.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stproject.st_be.dto.UserDto;
import com.stproject.st_be.security.util.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        super();
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
            UserDto authenticationRequest = new ObjectMapper().readValue(request.getInputStream(), UserDto.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword());
            return authenticationManager.authenticate(authentication);

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) {
        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim(SecurityConstants.AUTHORITIES, authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_DATE))
                .signWith(Keys.hmacShaKeyFor(SecurityConstants.SECRET.getBytes()))
                .compact();

        response.addHeader(SecurityConstants.HEADER, SecurityConstants.TOKEN_PREFIX + token);
    }
}
