package de.karlk.timetracker;

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

	@Bean
	CommandLineRunner createDemoUser(UserAccountRepository userRepo, EmployeeRepository employeeRepo) {
		return (args) -> {
			UserAccount demo = new UserAccount("DemoUser");
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
		};
	}
}
