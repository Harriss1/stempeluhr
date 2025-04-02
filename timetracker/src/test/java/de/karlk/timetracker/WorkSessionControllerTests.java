package de.karlk.timetracker;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.Duration;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import de.karlk.timetracker.employee.UserAccount;
import de.karlk.timetracker.employee.UserAccountRepository;
import de.karlk.timetracker.worksession.WorkSession;
import de.karlk.timetracker.worksession.WorkSessionService;

/**
 * Frontend Test der REST-Schnittstelle
 * 
 * WIP-Versuch im TDD-Stil
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = TimetrackerApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class WorkSessionControllerTests {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	WorkSessionService workSessionService;

	@Autowired
	UserAccountRepository userAccountRepository;
	
	
	private UserAccount getDemoUser() {
		return userAccountRepository.findByName(TimetrackerApplication.DEMO_USER_NAME).get(0);
	}
	
	@BeforeEach
	public void givenDemoUser() {
		try {
			getDemoUser();
		} catch (Exception e) {
			throw new IllegalStateException("'DemoUser' Account muss existieren für Tests");
		}
	}
	
	void createWorkSession(ZonedDateTime start, Duration duration) {
		WorkSession session = new WorkSession(getDemoUser().getEmployee());
		session.setStartTimeStamp(start);
		session.setEndTimeStamp(start.plus(duration));
		workSessionService.saveWorkSession(session);
	}
	
	@Test
	void givenWorkSession_getAllWorkSessions_includesWorkSession() throws Exception {
		createWorkSession(ZonedDateTime.parse("2019-10-20T07:30:00+02:00"), Duration.ofHours(9));
		
		mvc.perform(get("/users/DemoUser/worksessions").contentType(MediaType.APPLICATION_JSON)) //
			.andExpect(MockMvcResultMatchers.jsonPath("$..[1].startTimeStamp").value("2019-10-20T07:30:00+02:00"))//
			.andExpect(MockMvcResultMatchers.jsonPath("$..[1].endTimeStamp").value("2019-10-20T16:30:00+02:00"));
	}


	@Test
	void givenWorkSession_getWorkSession_returnsWorkSession() throws Exception {
		createWorkSession(ZonedDateTime.parse("2019-12-24T07:30:00+02:00"), Duration.ofHours(9));
		
		mvc.perform(get("/users/DemoUser/worksessions/2").contentType(MediaType.APPLICATION_JSON)) //
			.andExpect(MockMvcResultMatchers.jsonPath("$..startTimeStamp").value("2019-12-24T07:30:00+02:00"))//
			.andExpect(MockMvcResultMatchers.jsonPath("$..endTimeStamp").value("2019-12-24T16:30:00+02:00"));
	}
}
