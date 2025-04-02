package fi.haagahelia.taskmaster.taskmaster.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import fi.haagahelia.taskmaster.taskmaster.domain.AppUserRepository;

public class AppUserDetailsServiceImpl implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public AppUserDetailsServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
    }

}
