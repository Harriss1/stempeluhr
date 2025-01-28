package de.karlk.timetracker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class TimetrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimetrackerApplication.class, args);
	}

	@Bean
	CommandLineRunner createDemoUser(UserRepository repository) {
		return (args) -> {
			// save a few customers
			repository.saveAndFlush(new User("Jack"));
			repository.saveAndFlush(new User("Jasmin"));

			// fetch all customers
			log.info("Customers found with findAll():");
			log.info("-------------------------------");
			repository.findAll().forEach(customer -> {
				log.info(customer.toString());
			});
			log.info("");
		};
	}
}
