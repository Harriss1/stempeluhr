package de.karlk.timetracker;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    private UserRepository userRepository;
    
	@ParameterizedTest
	@ValueSource(strings = {"timetracking", "timetracker-prototype"})
    void canConnectToH2Database(String databaseName) {
        assertTrue(true, "Datenbank sollte ansteuerbar sein");
    }
	
	@ParameterizedTest
	@ValueSource(strings = {"Max", "Sarah"})
    void canCreateAndReadUser(String name) {
		User expectedUser = new User(name);

		userRepository.save(expectedUser);
		userRepository.flush();
		
		User actualUser = userRepository.findByName(name);
		log.info("gefundener Mitarbeiter: " + actualUser.getName());
        assertEquals(expectedUser.getName(), actualUser.getName(), "Die Benutzernamen sollten übereinstimmen.");
    }
	
	@ParameterizedTest
	@ValueSource(strings = {"Marius", "Nadine"})
    void missingEmployeeIsNull(String name) {
		User actualEmployee = userRepository.findByName(name);
        assertNull(actualEmployee, "Das Objekt müsste null sein, da der Benutzer nicht existiert.");
    }
}
