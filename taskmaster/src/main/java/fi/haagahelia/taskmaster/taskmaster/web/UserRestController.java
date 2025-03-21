package fi.haagahelia.taskmaster.taskmaster.web;

import java.util.List;
import fi.haagahelia.taskmaster.taskmaster.domain.BlockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fi.haagahelia.taskmaster.taskmaster.domain.AppUser;
import fi.haagahelia.taskmaster.taskmaster.domain.AppUserRepository;

@CrossOrigin

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final BlockRepository blockRepository;
    private final AppUserRepository appUserRepository;

    @Autowired
    public UserRestController(AppUserRepository appUserRepository, BlockRepository blockRepository){
        this.appUserRepository = appUserRepository;
        this.blockRepository = blockRepository;
    };
    //Get All users
    @GetMapping
    public ResponseEntity<List<AppUser>>getAllAppUsers(){
        List<AppUser> users = appUserRepository.findAll();
        return ResponseEntity.ok(users);
    }
    //Get one user
    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getAppuserById(@PathVariable Long id){
        return appUserRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    //Delete user
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAppUser(@PathVariable Long id){
        AppUser appuser = appUserRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "block" + id + "cant be deleted, since it doesn't exist."
                ));
                appUserRepository.delete(appuser);
                return ResponseEntity.noContent().build();
    }

}
