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
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();

        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationFilter authenticationFilter)
                        throws Exception {
                http.csrf(csrf -> csrf.disable()).cors(withDefaults())
                                .sessionManagement(
                                                sessionManagement -> sessionManagement
                                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                                                .requestMatchers(
                                                                antMatcher(HttpMethod.POST, "/api/auth/login"),
                                                                antMatcher(HttpMethod.POST, "/api/users/register"),
                                                                antMatcher(HttpMethod.GET, "/api/panels"),
                                                                antMatcher("/error"))
                                                .permitAll()
                                                .anyRequest()
                                                .authenticated())
                                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
                return authConfig.getAuthenticationManager();
        }
}
