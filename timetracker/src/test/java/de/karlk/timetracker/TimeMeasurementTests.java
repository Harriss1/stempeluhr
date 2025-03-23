package de.karlk.timetracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import de.karlk.timetracker.employee.Employee;
import de.karlk.timetracker.employee.EmployeeRepository;
import de.karlk.timetracker.employee.UserAccount;
import de.karlk.timetracker.employee.UserRepository;
import de.karlk.timetracker.worksession.LegalShiftType;
import de.karlk.timetracker.worksession.WorkSession;
import de.karlk.timetracker.worksession.WorkSessionService;
import lombok.extern.slf4j.Slf4j;
/**
 * Zeitmessungen werden im TDD-Stil implementiert
 * 
 * Aktuell wird die Datenbank nicht nach jedem Test zurückgesetzt. Ob dies
 * tatsächlich ein gutes Vorgehen ist sollte ich recherchieren.
 * 
 * <p><u>
 * Gemäß Arbeitszeitgesetz (ArbZG) § 4 Ruhepausen</u>
 * 
 * <p>
 * Die Arbeit ist durch im voraus feststehende Ruhepausen von mindestens 30
 * Minuten bei einer Arbeitszeit von mehr als sechs bis zu neun Stunden und 45
 * Minuten bei einer Arbeitszeit von mehr als neun Stunden insgesamt zu
 * unterbrechen.
 */
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
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

	@Autowired
	WorkSessionService sessionService;

	private static final String TRAINING_ACCOUNT_NAME = "training_time_measurements";

	@BeforeEach
	public void setupTrainingAccount() {
		if (userRepository.findByName(TRAINING_ACCOUNT_NAME).size() != 0) {
			log.warn("Trainingsaccount für TimeMeasurementTests sollte noch nicht existieren. "
					+ "(Testumgebung wird vermutlich aktuell erst nach allen Tests zurückgesetzt.)");
			return;
		}

		Employee isabell = new Employee("Ina", "Zinn");
		employeeRepository.save(isabell);
		UserAccount training_as_isabell = new UserAccount(TRAINING_ACCOUNT_NAME);
		training_as_isabell.setEmployee(isabell);
		userRepository.save(training_as_isabell);
		entityManager.flush();
		entityManager.clear();
	}

	UserAccount getTrainingAccount() {
		return userRepository.findByName(TRAINING_ACCOUNT_NAME).get(0);
	}

	@Test
	void startAMeasurement_andCheckDuration() throws InterruptedException {
		var employee = getTrainingAccount().getEmployee();
		var session = sessionService.createAndStartWorkSessionNowFor(employee);

		assertNotNull(session, "WorkSession sollte instanziiert worden sein");
		assertNotNull(session.getEmployee(), "WorkSession sollte einem Mitarbeiter zugeordnet sein");
		Thread.sleep(4000);
		assertTrue(session.getElapsedDuration().toSeconds() > 2,
				"Es sollte mehr als 2 Sekunden Dauer gemessen werden.");
		assertTrue(session.getElapsedDuration().toSeconds() < 6,
				"Es sollten weniger als 6 Sekunden Dauer gemessen werden.");
	}

	@Test
	void finishAMeasurment_andCheckDuration() throws InterruptedException {
		var employee = getTrainingAccount().getEmployee();

		var session = sessionService.createAndStartWorkSessionNowFor(employee);
		Thread.sleep(7000);
		sessionService.finishNowAndSaveWorkSession(session);
		assertTrue(session.getTotalDuration().toSeconds() > 6, "Es sollte mehr als 6 Sekunden Dauer gemessen werden.");
		assertTrue(session.getTotalDuration().toSeconds() < 9,
				"Es sollten weniger als 9 Sekunden Dauer gemessen werden.");
	}

	@Test
	/*
	 * preventing rollbacks in this class was temporary helpful as long as the
	 * testdata is quite shallow
	 * 
	 * this test only showed a failure, because another dataset messed up the result
	 * of an incomplete repository search method
	 */
	void calculatesLegallyRequiredBreakDuration_ofARegularShift() {
		var employee = getTrainingAccount().getEmployee();
		ZonedDateTime searchStartingPoint = ZonedDateTime.now().minusHours(7).minusMinutes(1);
		WorkSession session = sessionService.createAndStartWorkSessionNowFor(employee);
		ZonedDateTime sessionStart = session.getStartTimeStamp();
		session.setStartTimeStamp(sessionStart.minusHours(7));
		sessionService.finishNowAndSaveWorkSession(session);

		WorkSession searchResult = sessionService.findFirstWorkSessionAfter(searchStartingPoint, employee);
		log.info("searchStartingPoint:" + searchStartingPoint.toString());
		log.info("foundSession startingPoint:" + searchResult.getStartTimeStamp().toString());
		Duration expectedBreakDuration = LegalShiftType.REGULAR_SHIFT.getLegalBreakDuration();
		Duration actualBreakDuration = searchResult.getBreakDuration();
		assertEquals(expectedBreakDuration.toMinutes(), actualBreakDuration.toMinutes(),
				"Die erwartete Pausenlänge (in Minuten) sollte übereinstimmen");
	}
	
	@ParameterizedTest
	@MethodSource("getTestData_calculatesNetDuration")
	void calculatesNetDuration(Duration totalDuration, Duration expectedNetDuration) {
		ZonedDateTime end = ZonedDateTime.now();
		ZonedDateTime start = end.minus(totalDuration);
		
		var employee = getTrainingAccount().getEmployee();
		
		WorkSession session = new WorkSession(employee);
		session.setStartTimeStamp(start);
		session.setEndTimeStamp(end);
		sessionService.saveWorkSession(session);

		Duration offsetTolerance = Duration.ofSeconds(1);
		ZonedDateTime startTimeStampForQuery = start.minus(offsetTolerance);
		WorkSession sessionToInspect = sessionService.findFirstWorkSessionAfter(startTimeStampForQuery, employee);
		log.info("searchStartingPoint:" + startTimeStampForQuery.toString());
		log.info("foundSession startingPoint:" + sessionToInspect.getStartTimeStamp().toString());

		assertEquals(expectedNetDuration.toMinutes(), sessionToInspect.getNetDuration().toMinutes(),
				"Die erwartete Nettoarbeitszeit (in Minuten) sollte übereinstimmen");
	}
	
	static Stream<Arguments> getTestData_calculatesNetDuration() {
		return Stream.of(Arguments.of(Duration.ofHours(6), Duration.ofHours(6)),
				Arguments.of(Duration.ofHours(6).plusSeconds(1), Duration.ofHours(5).plusMinutes(30).plusSeconds(1)),
				Arguments.of(Duration.ofHours(7), Duration.ofHours(6).plusMinutes(30)),
				Arguments.of(Duration.ofHours(9), Duration.ofHours(8).plusMinutes(30)),
				Arguments.of(Duration.ofHours(9).plusSeconds(1), Duration.ofHours(8).plusMinutes(15).plusSeconds(1)));
	}

	@ParameterizedTest
	@MethodSource("getTestDataShiftDurations")
	void calculatesCorrectLegalNetDuration_forTotalShiftDuration(Duration shiftDurationWithBreak,
			Duration expectedBreakDuration) {
		var employee = getTrainingAccount().getEmployee();
		ZonedDateTime searchStartingPoint = ZonedDateTime.now().minus(shiftDurationWithBreak).minusMinutes(1);
		WorkSession session = sessionService.createAndStartWorkSessionNowFor(employee);
		ZonedDateTime sessionStart = session.getStartTimeStamp();
		session.setStartTimeStamp(sessionStart.minus(shiftDurationWithBreak));
		sessionService.finishNowAndSaveWorkSession(session);

		WorkSession searchResult = sessionService.findFirstWorkSessionAfter(searchStartingPoint, employee);
		Duration actualBreakDuration = searchResult.getBreakDuration();
		assertEquals(expectedBreakDuration.toMinutes(), actualBreakDuration.toMinutes(),
				"Die erwartete Pausenlänge (in Minuten) sollte übereinstimmen");
	}

	/**
	 * test data set includes all boundary values plus one hour above and below
	 * (zweiwertige Grenzwertprüfung)
	 * 
	 * @return
	 */
	static Stream<Arguments> getTestDataShiftDurations() {
		return Stream.of(Arguments.of(Duration.ofHours(4), LegalShiftType.NO_BREAK.getLegalBreakDuration()),
				Arguments.of(Duration.ofHours(5), LegalShiftType.NO_BREAK.getLegalBreakDuration()),
				Arguments.of(Duration.ofHours(6), LegalShiftType.NO_BREAK.getLegalBreakDuration()),
				Arguments.of(Duration.ofHours(7), LegalShiftType.REGULAR_SHIFT.getLegalBreakDuration()),
				Arguments.of(Duration.ofHours(8), LegalShiftType.REGULAR_SHIFT.getLegalBreakDuration()),
				Arguments.of(Duration.ofHours(9), LegalShiftType.REGULAR_SHIFT.getLegalBreakDuration()),
				Arguments.of(Duration.ofHours(10), LegalShiftType.LONG_SHIFT.getLegalBreakDuration()));
	}

	@Test
	@Rollback(false)
	void sumUpMultipleWorkSessionsOfOneEmployee_intuitiveTestdata() throws InterruptedException {
		var employee = getTrainingAccount().getEmployee();
		int daysSinceStart = 9;
		createAMeasurementForEachDaySince(daysSinceStart, employee);

		ZonedDateTime start = ZonedDateTime.now().minusDays(daysSinceStart).minusMinutes(1);
		ZonedDateTime end = ZonedDateTime.now().minusDays(2);
		Duration duration = sessionService.calculateNetWorkDurationBetween(start, end, employee);
		log.info(duration.toString());
		assertTrue(duration.toHours() > 35, "das Netto Ergebnis sollte ungefähr über 35 Arbeitsstunden liegen");
	}

	/**
	 * Creates in interval of 1 hour the given amount of worksessions, climbing
	 * upwards from amount of days ago to current date
	 * 
	 * <p>
	 * starts at a duration of 1 minute and ends at a duration of given days in
	 * hours and 1 minute
	 * 
	 * <p>
	 * because of this behavior more than 15 days would not make sense
	 * 
	 * @param employee
	 */
	private void createAMeasurementForEachDaySince(int daysAgo, Employee employee) {
		for (int daysBack = daysAgo; daysBack >= 0; daysBack--) {
			var session = sessionService.createAndStartWorkSessionNowFor(employee);
			sessionService.finishNowAndSaveWorkSession(session);

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

	@ParameterizedTest
	@MethodSource("getStructuredTestdataSumOfShifts")
	@Rollback(false)
	void sumUpMultipleWorkSessionsOfOneEmployee_structuredTestdata(ZonedDateTime startOfShifts, ZonedDateTime endOfShifts,
			Duration expectedNetWorkDuration) {
		persistTestDataOnce();
		Duration actualNetWorkDuration = sessionService.calculateNetWorkDurationBetween(startOfShifts, endOfShifts, getTrainingAccount().getEmployee());
		assertEquals(expectedNetWorkDuration.toSeconds(), actualNetWorkDuration.toSeconds(), 
				"Die Nettoarbeitszeit sollte abzüglich Pause '"+expectedNetWorkDuration.toSeconds()+"s' betragen, "
						+ "\nsie beträgt aber '"+actualNetWorkDuration.toSeconds()+"s'."
						+ "\nStart:" + startOfShifts
						+ "\nEnd:" + endOfShifts);
	}

	private boolean legalBoundaryValueTestDataPersisted = false;

	/**
	 * TODO Testdaten sollten einmalig global persistiert werden. Generell ist diese
	 * Strategie nur ein WIP.
	 */
	private void persistTestDataOnce() {
		if (legalBoundaryValueTestDataPersisted)
			return;
		legalBoundaryValueTestDataPersisted = true;
		var employee = getTrainingAccount().getEmployee();
		// TODO UTC+1 muss für komplette Testumgebung zentral festgelegt werden
		ZonedDateTime testDataSetStartingDay = ZonedDateTime.of(2015, 3, 12, 8, 0, 0, 0, ZoneId.of("UTC+1"));
		persistShiftsByBoundaryValueAnalysis(3, employee, testDataSetStartingDay);
	}

	static Stream<Arguments> getStructuredTestdataSumOfShifts() {
		ZonedDateTime startOfShifts = ZonedDateTime.of(2015, 3, 12, 7, 30, 0, 0, ZoneId.of("UTC+1"));
		ZonedDateTime endOfShifts = ZonedDateTime.of(2015, 3, 13, 23, 59, 0, 0, ZoneId.of("UTC+1"));
		Duration expectedNetWorkDuration = Duration.ofHours(11).plusMinutes(59);
		return Stream.of(Arguments.of(startOfShifts, endOfShifts, expectedNetWorkDuration));
	}

	/**
	 * Persists shifts, whose durations are determined by
	 * 2-value-boundary-value-analysis
	 * 
	 * <p>
	 * boundary values are determined by german work law
	 * 
	 * @param timesToRepeat     number of repetions of this set, where each set
	 *                          starts after the day of the last set's shift
	 * @param employee
	 * @param startOfFirstShift be careful to also set the hour, as this determines
	 *                          all starting hours
	 */
	private void persistShiftsByBoundaryValueAnalysis(int timesToRepeat, Employee employee,
			ZonedDateTime startOfFirstShift) {
		int daysAfterStartOfFirstShift = 0;
		while (timesToRepeat > 0) {
			timesToRepeat--;
			for (ExpectedBreakDurationForShiftDuration testDataSet : getLegallyExpectedBreakDurations()) {
				ZonedDateTime start = startOfFirstShift.plusDays(daysAfterStartOfFirstShift);
				ZonedDateTime end = start.plus(testDataSet.totalShiftDuration);
				WorkSession session = new WorkSession(employee);
				session.setStartTimeStamp(start);
				session.setEndTimeStamp(end);
				daysAfterStartOfFirstShift++;
				sessionService.saveWorkSession(session);
			}
		}
	}

	private List<ExpectedBreakDurationForShiftDuration> getLegallyExpectedBreakDurations() {
		List<ExpectedBreakDurationForShiftDuration> expectedBreakDurations = new ArrayList<ExpectedBreakDurationForShiftDuration>();
		expectedBreakDurations.add(ExpectedBreakDurationForShiftDuration.byString("5:59", "0:00"));
		expectedBreakDurations.add(ExpectedBreakDurationForShiftDuration.byString("6:00", "0:00"));
		expectedBreakDurations.add(ExpectedBreakDurationForShiftDuration.byString("6:01", "0:30"));
		expectedBreakDurations.add(ExpectedBreakDurationForShiftDuration.byString("8:59", "0:30"));
		expectedBreakDurations.add(ExpectedBreakDurationForShiftDuration.byString("9:00", "0:30"));
		expectedBreakDurations.add(ExpectedBreakDurationForShiftDuration.byString("9:01", "0:45"));

		return expectedBreakDurations;
	}
}
