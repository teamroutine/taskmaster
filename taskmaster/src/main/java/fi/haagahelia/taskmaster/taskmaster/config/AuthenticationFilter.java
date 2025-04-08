package fi.haagahelia.taskmaster.taskmaster.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;

import fi.haagahelia.taskmaster.taskmaster.service.JwtService;
import org.springframework.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, java.io.IOException {

        String jws = request.getHeader(HttpHeaders.AUTHORIZATION); // Retrive the authorization header from HTTP request

        if (jws != null) { // Check if the authorization header exists and contains JWT token
            String user = jwtService.getAuthUser(request); // Use the user information from the token

            Authentication authentication = new UsernamePasswordAuthenticationToken( // If user is found create
                                                                                     // Authentication object
                    user, null, List.of());

            SecurityContextHolder.getContext().setAuthentication(authentication); // Authenticates user for current
                                                                                  // request

        }

        filterChain.doFilter(request, response); // Continue the process and filter all the layers

    }
}