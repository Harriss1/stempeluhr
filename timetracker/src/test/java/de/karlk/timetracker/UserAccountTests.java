package de.karlk.timetracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.extern.slf4j.Slf4j;

@TestPropertySource(
		  locations = "classpath:application-integrationtest.properties")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
public class UserAccountTests {
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;

	@ParameterizedTest
	@ValueSource(strings = {"Max", "Sarah"})
    void canCreateAndReadUser(String name) {
		UserAccount expectedUser = new UserAccount(name);

		userRepository.save(expectedUser);
		userRepository.flush();
		
		UserAccount actualUser = userRepository.findByName(name);
		log.info("gefundener Mitarbeiter: " + actualUser.getName());
        assertEquals(expectedUser.getName(), actualUser.getName(), "Die Benutzernamen sollten übereinstimmen.");
    }
	
	@ParameterizedTest
	@ValueSource(strings = {"Marius", "Nadine"})
    void missingEmployeeIsNull(String name) {
		UserAccount actualUser = userRepository.findByName(name);
        assertNull(actualUser, "Das Objekt müsste null sein, da der Benutzer nicht existiert.");
    }
	
	@Test
	@Rollback(false) // in case of failure the data should be inspected in database
    void canCreateEmployeeAndAssociateToUser() {
		UserAccount accountExpectingEmployee = new UserAccount("account_expects_employee");
		userRepository.save(accountExpectingEmployee);
		userRepository.flush();
		entityManager.clear();
		
		Employee employee = new Employee("Leonie", "Lannieh");
		employeeRepository.save(employee);
		
		UserAccount training = userRepository.findByName("account_expects_employee");
		training.setEmployee(employee);
		entityManager.persist(training);
		entityManager.flush();
		entityManager.clear();
		
		List<Employee> result = employeeRepository.findByFirstName("Leonie");
        assertTrue(result.size() > 0, "Der soeben persistierte Mitarbeiter sollte in der Datenbank existieren.");
        
        Employee actual = result.get(0);
        assertNotNull(actual.getUserAccount(), "Dem Mitarbeiter sollte ein Account assoziert worden sein.");
        
        assertEquals(training.getName(), actual.getUserAccount().getName(), "Die Benutzernamen sollten übereinstimmen.");
    }
    
	public void setupTrainingAccount() {
		Employee ina = new Employee("Ina", "Zinn");
		employeeRepository.save(ina);
		UserAccount training = new UserAccount("training");
		training.setEmployee(ina);
		userRepository.save(training);
		
	}


}
