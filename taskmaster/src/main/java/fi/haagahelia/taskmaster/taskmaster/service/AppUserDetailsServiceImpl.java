package fi.haagahelia.taskmaster.taskmaster.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fi.haagahelia.taskmaster.taskmaster.domain.AppUserRepository;

@Service
public class AppUserDetailsServiceImpl implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public AppUserDetailsServiceImpl(AppUserRepository appUserRepository) { // Use AppUserRepository in this class
        this.appUserRepository = appUserRepository;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByUsername(username) // Get the user from database based on user details
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
    }

}
