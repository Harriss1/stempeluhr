package de.karlk.timetracker;

import java.time.Duration;
import java.time.ZonedDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import de.karlk.timetracker.employee.Employee;
import de.karlk.timetracker.employee.EmployeeRepository;
import de.karlk.timetracker.employee.UserAccount;
import de.karlk.timetracker.employee.UserAccountRepository;
import de.karlk.timetracker.worksession.WorkSession;
import de.karlk.timetracker.worksession.WorkSessionService;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@ComponentScan({"de.karlk*"})
@EntityScan("de.karlk*")
@EnableJpaRepositories("de.karlk*")
@Slf4j
public class TimetrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimetrackerApplication.class, args);
	}
	
	public static final String DEMO_USER_NAME="DemoUser";

	@Bean
	CommandLineRunner createDemoData(UserAccountRepository userRepo, EmployeeRepository employeeRepo, WorkSessionService workSessionService) {
		return (args) -> {
			UserAccount demo = new UserAccount(DEMO_USER_NAME);
			Employee max = new Employee("Max", "Muster");
			employeeRepo.save(max);
			demo.setEmployee(max);
			userRepo.saveAndFlush(demo);

			log.info("Employees found with findAll():");
			log.info("-------------------------------");
			userRepo.findAll().forEach(u -> {
				log.info(u.toString());
			});
			log.info("");
			
			WorkSession session = new WorkSession(max);
			var start = ZonedDateTime.parse("2024-06-25T10:30:00+02:00");
			session.setStartTimeStamp(start);
			session.setEndTimeStamp(start.plus(Duration.ofHours(8)));
			workSessionService.saveWorkSession(session);
		};
	}
}
