package fi.haagahelia.taskmaster.taskmaster.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import fi.haagahelia.taskmaster.taskmaster.dto.AccessTokenPayloadDto;
import fi.haagahelia.taskmaster.taskmaster.dto.LoginUserDto;
import fi.haagahelia.taskmaster.taskmaster.service.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public AuthRestController(AuthenticationManager authenticationManager, JwtService jwtService,
            UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager; // Authenticate of the user
        this.jwtService = jwtService; // Create and Validate JWT-tokens
        this.userDetailsService = userDetailsService; // Get the user data

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginUserDto loginUserDto) {
        try {
            // Authenticates the user
            authenticationManager.authenticate(
                    // Checks users input and tries to combine username and password
                    new UsernamePasswordAuthenticationToken(loginUserDto.getUsername(), loginUserDto.getPassword()));
            // Get the user data from database
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginUserDto.getUsername());
            // Create JWT-token and returns it as AccessTokenPayload object
            AccessTokenPayloadDto accessTokenPayloadDto = jwtService.getAccessToken(userDetails.getUsername());

            return ResponseEntity.ok(accessTokenPayloadDto);
        } catch (BadCredentialsException exception) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}