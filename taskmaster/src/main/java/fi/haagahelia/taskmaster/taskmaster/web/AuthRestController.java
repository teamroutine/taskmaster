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

import fi.haagahelia.taskmaster.taskmaster.domain.AppUser;
import fi.haagahelia.taskmaster.taskmaster.dto.AccessTokenPayloadDto;
import fi.haagahelia.taskmaster.taskmaster.dto.LoginUserDto;
import fi.haagahelia.taskmaster.taskmaster.service.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
@Tag(name = "Authentication", description = "Endpoint for user login and authentication")
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

    @Operation(summary = "Login user", description = "Authenticate user with username and password. Returns JWT token if successful.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password"),
            @ApiResponse(responseCode = "400", description = "Invalid input format")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginUserDto loginUserDto) {
        try {

            authenticationManager.authenticate(

                    new UsernamePasswordAuthenticationToken(loginUserDto.getUsername(),
                            loginUserDto.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginUserDto.getUsername());

            AppUser appUser = (AppUser) userDetails;
            String email = appUser.getEmail();
            String firstName = appUser.getFirstName();
            String lastName = appUser.getLastName();

            AccessTokenPayloadDto accessTokenPayloadDto = jwtService.getAccessToken(userDetails.getUsername(), email,
                    firstName, lastName);

            return ResponseEntity.ok(accessTokenPayloadDto);
        } catch (BadCredentialsException exception) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}