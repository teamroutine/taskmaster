package fi.haagahelia.taskmaster.taskmaster.web;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fi.haagahelia.taskmaster.taskmaster.dto.AccessTokenPayloadDto;
import fi.haagahelia.taskmaster.taskmaster.dto.LoginUserDto;
import fi.haagahelia.taskmaster.taskmaster.service.JwtService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public AuthRestController(AuthenticationManager authenticationManager, JwtService jwtService,
            UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginUserDto loginUserDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUserDto.getUsername(), loginUserDto.getPassword()));

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginUserDto.getUsername());
            AccessTokenPayloadDto accessTokenPayloadDto = jwtService.getAccessToken(userDetails.getUsername());

            return ResponseEntity.ok(accessTokenPayloadDto);
        } catch (BadCredentialsException exception) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}