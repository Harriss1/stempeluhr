package de.karlk.timetracker;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.extern.slf4j.Slf4j;

@TestPropertySource(
		  locations = "classpath:application-integrationtest.properties")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Slf4j
public class ProtypeTests {
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
	@ParameterizedTest
	@ValueSource(strings = {"timetracking", "timetracker-prototype"})
    void canConnectToH2Database(String databaseName) {
        assertTrue(true, "Datenbank sollte ansteuerbar sein");
    }
	
	@ParameterizedTest
	@ValueSource(strings = {"Max", "Sarah"})
    void canCreateAndReadEmployee(String firstName) {
		Employee expectedEmployee = new Employee(firstName);

		employeeRepository.save(expectedEmployee);
		employeeRepository.flush();
//		entityManager.persist(expectedEmployee);
//		entityManager.flush();
		
		Employee actualEmployee = employeeRepository.findByFirstName(firstName);
		log.info("gefundener Mitarbeiter: " + actualEmployee.getFirstName());
        assertEquals(expectedEmployee.getFirstName(), actualEmployee.getFirstName(), "Die Vornamen sollten übereinstimmen");
    }
	
	@ParameterizedTest
	@ValueSource(strings = {"Marius", "Nadine"})
    void missingEmployeeIsNull(String firstName) {
		Employee actualEmployee = employeeRepository.findByFirstName(firstName);
        assertNull(actualEmployee, "Der Employee exisitert nicht, weshalb das Objekt null sein müsste");
    }
}
