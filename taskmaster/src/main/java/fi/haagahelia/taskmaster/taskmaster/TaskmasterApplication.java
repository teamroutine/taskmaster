package fi.haagahelia.taskmaster.taskmaster;

import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.slf4j.Logger;
import fi.haagahelia.taskmaster.taskmaster.domain.Panel;
import fi.haagahelia.taskmaster.taskmaster.domain.PanelRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.Team;
import fi.haagahelia.taskmaster.taskmaster.domain.TeamRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.Block;
import fi.haagahelia.taskmaster.taskmaster.domain.BlockRepository;


@SpringBootApplication
public class TaskmasterApplication {

	private static final Logger log = LoggerFactory.getLogger(TaskmasterApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(TaskmasterApplication.class, args);
	}

	@Bean
    public CommandLineRunner demo(PanelRepository prepository, TeamRepository trepository, BlockRepository brepository) {
        return (args) -> {
            log.info("Saving panels and team");
			Team team1 = new Team(null, "TestTeam", "Team data for testing", null );
			trepository.save(team1);
			

            Panel panel1 = new Panel(null, "Testi projekt", "testi projekti testaukseen", team1, null);
			
            Panel panel2 = new Panel(null, "new project", "test for project testing", team1, null);

            prepository.save(panel1);
			prepository.save(panel2);

			panel1.setTeam(team1);
			panel2.setTeam(team1);
	
			prepository.save(panel1);
			prepository.save(panel2);

			Block block1 = new Block(null, "test block", "for testing", null);
			brepository.save(block1);
			block1.setPanel(panel1);
			brepository.save(block1);
	
           
        };
    }

}
