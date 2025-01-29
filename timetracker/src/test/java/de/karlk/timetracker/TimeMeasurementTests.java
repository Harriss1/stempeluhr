package de.karlk.timetracker;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.ZonedDateTime;

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

import de.karlk.timetracker.measurement.LegalShiftType;
import de.karlk.timetracker.measurement.WorkSession;
import de.karlk.timetracker.measurement.WorkSessionService;
import lombok.extern.slf4j.Slf4j;

@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
/**
 * Zeitmessungen werden im TTD-Stil implementiert
 */
public class TimeMeasurementTests {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	WorkSessionService sessionService;

	private static final String TRAINING_ACCOUNT_NAME = "training_time_measurements";

	@BeforeEach
	public void setupTrainingAccount() {
		if (userRepository.findByName(TRAINING_ACCOUNT_NAME).size() > 0) {
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
	void startAMeasurement_andCheckDuration() throws InterruptedException {
		var employee = getTrainingAccount().getEmployee();

		var session = sessionService.createAndStartWorkSessionFor(employee);
		assertNotNull(session, "WorkSession sollte instanziiert worden sein");
		assertNotNull(session.getEmployee(), "WorkSession sollte einem Mitarbeiter zugeordnet sein");
		Thread.sleep(4000);
		assertTrue(session.getElapsedDuration().toSeconds() > 2,
				"Es sollte mehr als 2 Sekunden Dauer gemessen werden.");
		assertTrue(session.getElapsedDuration().toSeconds() < 6,
				"Es sollten weniger als 6 Sekunden Dauer gemessen werden.");
	}

	@Test
	@Rollback(false)
	void finishAMeasurment_andCheckDuration() throws InterruptedException {
		var employee = getTrainingAccount().getEmployee();

		var session = sessionService.createAndStartWorkSessionFor(employee);
		Thread.sleep(7000);
		sessionService.finishWorkSession(session);
		assertTrue(session.getTotalDuration().toSeconds() > 5, "Es sollte mehr als 5 Sekunden Dauer gemessen werden.");
		assertTrue(session.getTotalDuration().toSeconds() < 9,
				"Es sollten weniger als 9 Sekunden Dauer gemessen werden.");
	}

	@Test
	@Rollback(false)
	void calculatesCorrectLegalNetDuration_ofSevenHourShift() throws InterruptedException {
		var employee = getTrainingAccount().getEmployee();
		ZonedDateTime searchStartingPoint = ZonedDateTime.now().minusHours(7).minusMinutes(1);
		WorkSession session = sessionService.createAndStartWorkSessionFor(employee);
		ZonedDateTime sessionStart = session.getStartTimeStamp();
		session.setStartTimeStamp(sessionStart.minusHours(7));
		sessionService.finishWorkSession(session);

		WorkSession searchResult = sessionService.findFirstWorkSessionAfter(searchStartingPoint, employee);
		Duration expectedBreakDuration = LegalShiftType.REGULAR_SHIFT.getLegalBreakDuration();
		Duration actualBreakDuration = searchResult.getBreakDuration();
		assertEquals(expectedBreakDuration.toMinutes(), actualBreakDuration.toMinutes(),
				"Die erwartete Pausenlänge (in Minuten) sollte übereinstimmen");
	}

//    @Test
//    @Rollback(false)
//    void sumUpMultipleMeasurmentsOfOneEmployee() throws InterruptedException {
//		var employee = getTrainingAccount().getEmployee();
//    	createTenMeasurementsFor(employee);
//    	
//    	ZonedDateTime start = null;
//    	ZonedDateTime end = null;
//    	Duration duration = sessionService.calculateNetWorkDurationBetween(start, end);
//    }

	/**
	 * Creates in interval of 1 hour 10 worksessions, climbing upwards from 10 days
	 * ago to current date
	 * 
	 * <p>
	 * starts at a duration of 1 minute and ends at a duration of 10 hours and 1
	 * minute
	 * 
	 * @param employee
	 */
	private void createTenMeasurementsFor(Employee employee) {
		for (int daysBack = 9; daysBack >= 0; daysBack--) {
			var session = sessionService.createAndStartWorkSessionFor(employee);
			sessionService.finishWorkSession(session);

			ZonedDateTime start = session.getStartTimeStamp();
			start = start.minusDays(daysBack);

			ZonedDateTime end = session.getEndTimeStamp();
			end = end.minusDays(daysBack).plusMinutes(30);
			int additionalHours = daysBack;
			end = end.plusHours(additionalHours);

			session.setStartTimeStamp(start);
			session.setEndTimeStamp(end);

			sessionService.saveWorkSession(session);
		}
	}
}
