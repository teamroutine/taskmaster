package fi.haagahelia.taskmaster.taskmaster.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository <Team, Long >{
    List<Team> findByTeamName(String teamName);
}
