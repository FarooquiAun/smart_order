package com.smartorder.security.jwt;

import com.smartorder.security.service.CustomerUserDetailsService;
import com.smartorder.security.token.TokenRepository;
import com.smartorder.security.userDetails.CustomerUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomerUserDetailsService service;
    private final TokenRepository tokenRepository;

    public JwtAuthenticationFilter(JwtService jwtService,
                                   CustomerUserDetailsService service,
                                   TokenRepository tokenRepository) {
        this.jwtService = jwtService;
        this.service = service;
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println(">>> JwtAuthenticationFilter executing for: " + request.getRequestURI());
        final String authHeader = request.getHeader("Authorization");
        System.out.println(">>> Authorization header = " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String username = jwtService.extractUsername(jwt);
        System.out.println(">>> Extracted username from JWT = " + username);

        var tokenOptional = tokenRepository.findByToken(jwt);
        if (tokenOptional.isPresent() && tokenOptional.get().isRevoked()) {
            System.out.println(">>> Token is revoked in DB, skipping authentication");
            filterChain.doFilter(request, response);
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = service.loadUserByUsername(username);
            boolean valid = jwtService.isTokenValid(jwt, userDetails);
            System.out.println(">>> jwtService.isTokenValid = " + valid);

            if (valid) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println(">>> SecurityContextHolder updated for user: " + username);
            } else {
                System.out.println(">>> JWT NOT valid for user: " + username);
            }
        }

        // IMPORTANT: always continue chain exactly once
        filterChain.doFilter(request, response);
    }
}
