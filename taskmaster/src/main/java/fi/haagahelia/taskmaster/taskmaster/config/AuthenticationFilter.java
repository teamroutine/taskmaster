package fi.haagahelia.taskmaster.taskmaster.config;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, java.io.IOException {

        String uri = request.getRequestURI();
        logger.info("AuthenticationFilter {}", uri);

        if (uri.contains("/api/auth/login") || uri.equals("/api/users")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jws = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (jws != null) {
            String user = jwtService.getAuthUser(request);
            logger.info("{} {}", jws, user);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user, null, List.of());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}