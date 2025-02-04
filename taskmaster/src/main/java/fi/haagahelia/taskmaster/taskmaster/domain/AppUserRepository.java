package fi.haagahelia.taskmaster.taskmaster.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository <AppUser,Long>{
      List<AppUser> findByFirstName(String fisrtName);

}
