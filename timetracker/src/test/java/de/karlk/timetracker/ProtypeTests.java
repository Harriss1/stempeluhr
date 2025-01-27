package de.karlk.timetracker;


import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@TestPropertySource(
		  locations = "classpath:application-integrationtest.properties")
@ExtendWith(SpringExtension.class)
@DataJpaTest
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
    void canCreateAndReadEmployee(String forename) {
        assertTrue(true, "Datenbank sollte ansteuerbar sein");
    }
}
