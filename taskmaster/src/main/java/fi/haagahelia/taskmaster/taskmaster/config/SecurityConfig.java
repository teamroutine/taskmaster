package fi.haagahelia.taskmaster.taskmaster.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public PasswordEncoder passwordEncoder() { // Bean for encrypting the passwords
                return new BCryptPasswordEncoder();

        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationFilter authenticationFilter) // Configuration
                                                                                                             // for HTTP
                                                                                                             // requests
                        throws Exception {
                http.csrf(csrf -> csrf.disable()).cors(withDefaults()) // Csrf disabled because of token based
                                                                       // authentication
                                .sessionManagement(
                                                sessionManagement -> sessionManagement
                                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Application
                                                                                                                         // uses
                                                                                                                         // JWT
                                                                                                                         // token
                                                                                                                         // so
                                                                                                                         // sessions
                                                                                                                         // have
                                                                                                                         // to
                                                                                                                         // stateless
                                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests // Endpoints which
                                                                                                      // are allowed for
                                                                                                      // unauthenticated
                                                                                                      // users
                                                .requestMatchers(
                                                                antMatcher(HttpMethod.POST, "/api/auth/login"),
                                                                antMatcher(HttpMethod.POST, "/api/users"),
                                                                antMatcher(HttpMethod.GET, "/api/users"),
                                                                antMatcher("/error"),
                                                                antMatcher(HttpMethod.GET, "/api/teams"),
                                                                antMatcher(HttpMethod.GET, "/h2-console/**"),
                                                                antMatcher(HttpMethod.POST, "/h2-console/**"),
                                                                antMatcher(HttpMethod.POST,
                                                                                "/api/invites/generateInvite"), // Allow
                                                                                                                // generateInvite
                                                                antMatcher(HttpMethod.GET,
                                                                                "/api/invites/validateInvite"), // Allow
                                                                                                                // validateInvite
                                                                antMatcher(HttpMethod.GET, "/api/invites/all"))
                                                // antMatcher(HttpMethod.POST, "/api/users"),
                                                // antMatcher(HttpMethod.GET, "/api/panels"),
                                                // antMatcher(HttpMethod.GET, "/api/teams"),
                                                // antMatcher(HttpMethod.GET, "/api/tickets"),
                                                // antMatcher(HttpMethod.GET, "/api/blocks"),
                                                // antMatcher(HttpMethod.POST, "/api/blocks"),
                                                // antMatcher(HttpMethod.POST, "/api/tickets"),
                                                // antMatcher(HttpMethod.POST, "/api/panels"),
                                                // antMatcher(HttpMethod.POST, "/api/teams"),

                                                .permitAll()
                                                .anyRequest() // Rest of the endpoints are restricted
                                                .authenticated())
                                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class); // Filter
                                                                                                                    // the
                                                                                                                    // requests
                                                                                                                    // with
                                                                                                                    // custom
                                                                                                                    // filter

                return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception { // Spring
                                                                                                                      // securitys
                                                                                                                      // own
                                                                                                                      // authentication
                                                                                                                      // which
                                                                                                                      // handles
                                                                                                                      // usernames
                                                                                                                      // and
                                                                                                                      // passwords
                return authConfig.getAuthenticationManager();
        }
}