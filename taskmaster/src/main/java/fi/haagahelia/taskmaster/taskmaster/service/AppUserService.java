package fi.haagahelia.taskmaster.taskmaster.service;

import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fi.haagahelia.taskmaster.taskmaster.domain.AppUser;
import fi.haagahelia.taskmaster.taskmaster.domain.AppUserRepository;
import fi.haagahelia.taskmaster.taskmaster.dto.RegisterUserDto;

@Service
public class AppUserService {

    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser registerUser(RegisterUserDto registerUserDto) {
        String encodedPassword = passwordEncoder.encode(registerUserDto.getPassword());

        AppUser newUser = new AppUser();
        newUser.setFirstName(registerUserDto.getFirstName());
        newUser.setLastName(registerUserDto.getLastName());
        newUser.setEmail(registerUserDto.getEmail());
        newUser.setPhone(registerUserDto.getPhone());
        newUser.setUsername(registerUserDto.getUsername());
        newUser.setPassword(encodedPassword);

        return appUserRepository.save(newUser);
    }

    public Optional<AppUser> getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check and validate user
        if (authentication instanceof UsernamePasswordAuthenticationToken token && token.getPrincipal() != null) {
            return Optional.empty();

        }
        // Get the authentication data if its valid
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        if (token.getPrincipal() == null) {
            return Optional.empty();
        }
        // Get user from database based on username
        return appUserRepository.findByUsername(token.getPrincipal().toString());
    }

}
