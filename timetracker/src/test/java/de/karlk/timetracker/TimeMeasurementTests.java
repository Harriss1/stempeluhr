package de.karlk.timetracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
public class TimeMeasurementTests {
	
	@Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    private static final String TRAINING_ACCOUNT_NAME = "training_time_measurements";
    
    @BeforeEach
	public void setupTrainingAccount() {
    	if(userRepository.findByName(TRAINING_ACCOUNT_NAME).size() > 0) {
    		log.warn("Trainingsaccount für TimeMeasurementTests sollte noch nicht existieren!");
    		return;
    	}
		Employee ina = new Employee("Ina", "Zinn");
		employeeRepository.save(ina);
		UserAccount training = new UserAccount(TRAINING_ACCOUNT_NAME);
		training.setEmployee(ina);
		userRepository.save(training);
		entityManager.flush();
		entityManager.clear();
	}
    
    UserAccount getTrainingAccount() {
    	return userRepository.findByName(TRAINING_ACCOUNT_NAME).get(0);
    }
    
    @Test
    void canStartAMeasurement() {
    	Employee employee = getTrainingAccount().getEmployee();
    	log.info(employee.getUserAccount().toString());
    }
}
