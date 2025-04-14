package fi.haagahelia.taskmaster.taskmaster;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import fi.haagahelia.taskmaster.taskmaster.domain.Panel;
import fi.haagahelia.taskmaster.taskmaster.domain.PanelRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.Team;
import fi.haagahelia.taskmaster.taskmaster.domain.TeamRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.Ticket;
import fi.haagahelia.taskmaster.taskmaster.domain.TicketRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.Block;
import fi.haagahelia.taskmaster.taskmaster.domain.BlockRepository;
import fi.haagahelia.taskmaster.taskmaster.domain.AppUser;
import fi.haagahelia.taskmaster.taskmaster.domain.AppUserRepository;

@SpringBootApplication
public class TaskmasterApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	private static final Logger log = LoggerFactory.getLogger(TaskmasterApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TaskmasterApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(PanelRepository prepository, TeamRepository terepository, BlockRepository brepository,
			TicketRepository tirepository, AppUserRepository arepository) {
		return args -> {
			log.info("Saving panels and team");
			Team team1 = new Team(null, "Team1", "description for team 1", null, null, null);
			terepository.save(team1);

			String encodedPassword = passwordEncoder.encode("salasana");
			String encodedPassword2 = passwordEncoder.encode("admin");

			AppUser appuser1 = new AppUser(null, "Hilja", "Katajamäki", "hilja.example@gmail", "0123456789", "hilja123",
					encodedPassword, null);
			arepository.save(appuser1);
			AppUser appuser2 = new AppUser(null, "Admin", "Katajamäki", "hilja.example@gmail", "0123456789", "admin",
					encodedPassword2, null);
			arepository.save(appuser2);

			Panel panel1 = new Panel(null, "Project 1", "description for panel 1 ", team1, null);

			Panel panel2 = new Panel(null, "Projekct 2", "description for panel 2", team1, null);

			prepository.save(panel1);
			prepository.save(panel2);

			panel1.setTeam(team1);
			panel2.setTeam(team1);

			prepository.save(panel1);
			prepository.save(panel2);

			Block block1 = new Block(null, "Block 1", "description for block 1", null, panel2, null);

			brepository.save(block1);
			block1.setPanel(panel1);
			brepository.save(block1);

			Ticket ticket1 = new Ticket(null, "Ticket 1 ", "description for ticket 1", null, null, block1);
			tirepository.save(ticket1);
			ticket1.setBlock(block1);
			tirepository.save(ticket1);

		};
	}

}