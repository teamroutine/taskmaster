package fi.haagahelia.taskmaster.taskmaster.web;

import java.util.List;
import java.util.Optional;

import fi.haagahelia.taskmaster.taskmaster.domain.BlockRepository;
import fi.haagahelia.taskmaster.taskmaster.dto.RegisterUserDto;
import fi.haagahelia.taskmaster.taskmaster.service.AppUserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.server.ResponseStatusException;

import fi.haagahelia.taskmaster.taskmaster.domain.AppUser;
import fi.haagahelia.taskmaster.taskmaster.domain.AppUserRepository;

@CrossOrigin

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final AppUserService appUserService;

    private final BlockRepository blockRepository;
    private final AppUserRepository appUserRepository;

    public UserRestController(AppUserRepository appUserRepository, BlockRepository blockRepository,
            AppUserService appUserService) {
        this.appUserRepository = appUserRepository;
        this.blockRepository = blockRepository;
        this.appUserService = appUserService;
    }

    // Get All users
    @GetMapping
    public ResponseEntity<List<AppUser>> getAllAppUsers() {
        List<AppUser> users = appUserRepository.findAll();
        return ResponseEntity.ok(users);
    }

    // Get one user
    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getAppuserById(@PathVariable Long id) {
        return appUserRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete user
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAppUser(@PathVariable Long id) {
        AppUser appuser = appUserRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "block" + id + "cant be deleted, since it doesn't exist."));
        appUserRepository.delete(appuser);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public AppUser createAppUser(@Valid @RequestBody RegisterUserDto registration, BindingResult bindingResult) {
        Optional<AppUser> existingUser = appUserRepository.findByUsername(registration.getUsername());

        if (existingUser.isPresent()) {
            bindingResult.rejectValue("username", "Username taken",
                    "This username is already taken. Choose another one");
        }

        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        return appUserService.registerUser(registration);
    }

}
