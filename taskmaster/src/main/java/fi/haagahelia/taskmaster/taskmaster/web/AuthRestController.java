package fi.haagahelia.taskmaster.taskmaster.web;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
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

public class AuthRestController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login") // Handle the POST request
    public ResponseEntity<?> login(@Valid @RequestBody LoginUserDto login, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, // If validation fails, then show error message
                                                                      // with code 400
                    bindingResult.getAllErrors().get(0).getDefaultMessage());

        }

        // Create authentication request with username and password
        UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                login.getUsername(),
                login.getPassword());

        try { // If credentials are correct token is created and access is granted
            Authentication auth = authenticationManager.authenticate(credentials);
            AccessTokenPayloadDto accessTokenPayloadDto = jwtService.getAccessToken(auth.getName());

            return ResponseEntity.ok().body(accessTokenPayloadDto); // Return with code 200 and JWT token

        } catch (Exception exception) { // If username and/or password are incorrect return error message with code 403

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid username or password");
        }

    }

}