package de.karlk.timetracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import de.karlk.timetracker.employee.UserAccountRepository;
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
	private WorkSessionService workSessionService;

	@Autowired
	UserAccountRepository userAccountRepository;
	
	@BeforeEach
	public void givenDemoUser() {
		if(userAccountRepository.findByName(TimetrackerApplication.DEMO_USER_NAME).size() < 1) {
			throw new IllegalStateException("'DemoUser' Account muss existieren für Tests");
		}
	}
	
	@Test
	void givenWorkSession_getAllWorkSessions_includesWorkSession() throws Exception {
		/* 1. create WorkSession in Mock-Repository
		 * 2. do request
		 * 3. assert that created session and its details exists
		 */
	}
}
