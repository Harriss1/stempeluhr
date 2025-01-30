package de.karlk.timetracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import lombok.extern.slf4j.Slf4j;

@TestPropertySource(
		  locations = "classpath:application-integrationtest.properties")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
/**
 * Test of Account creation, lookup and relationship between Employee.
 * 
 * <p>For the tasks goals it's right now not required to make a full CRUD-Test.
 */
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
		
		UserAccount actualUser = userRepository.findByName(name).get(0);
		log.info("gefundener Mitarbeiter: " + actualUser.getName());
        assertEquals(expectedUser.getName(), actualUser.getName(), "Die Benutzernamen sollten übereinstimmen.");
    }
	
	@ParameterizedTest
	@ValueSource(strings = {"Marius", "Nadine"})
    void missingEmployeeIsNull(String name) {
		int actualSize = userRepository.findByName(name).size();
		int expectedSize = 0;
        assertEquals(expectedSize, actualSize, "Der Benutzer dürfte nicht existieren.");
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
		
		UserAccount training = userRepository.findByName("account_expects_employee").get(0);
		training.setEmployee(employee);
		entityManager.persist(training);
		entityManager.flush();
		entityManager.clear();
		
		List<Employee> resultEmployees = employeeRepository.findByFirstName("Leonie");
        assertTrue(resultEmployees.size() > 0, "Der soeben persistierte Mitarbeiter sollte in der Datenbank existieren.");
        
        Employee actual = resultEmployees.get(0);
        assertNotNull(actual.getUserAccount(), "Dem Mitarbeiter sollte ein Account assoziert worden sein.");
        
        // call relationship from employee to account 
        assertEquals(training.getName(), actual.getUserAccount().getName(), "Die Benutzernamen sollten übereinstimmen.");
        
        // call relationship from account to employee
        assertEquals(training.getEmployee(), actual, "Objektinstanzen sollten übereinstimmen");
    }
    


}
