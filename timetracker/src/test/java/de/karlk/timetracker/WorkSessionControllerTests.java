package de.karlk.timetracker;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.Duration;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import de.karlk.timetracker.employee.UserAccount;
import de.karlk.timetracker.employee.UserAccountRepository;
import de.karlk.timetracker.worksession.WorkSession;
import de.karlk.timetracker.worksession.WorkSessionService;
import lombok.extern.slf4j.Slf4j;

/**
 * Frontend Test der REST-Schnittstelle
 * 
 * WIP-Versuch im TDD-Stil
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = TimetrackerApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@TestMethodOrder(OrderAnnotation.class)
@Slf4j
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
	@Order(1)
	@Rollback(false)
	void getAllWorkSessions_includesWorkSession() throws Exception {
		createWorkSession(ZonedDateTime.parse("2019-10-20T07:30:00+02:00"), Duration.ofHours(9));
		
		mvc.perform(get("/users/DemoUser/worksessions").contentType(MediaType.APPLICATION_JSON)) //
			.andExpect(MockMvcResultMatchers.jsonPath("$..[1].startTimeStamp").value("2019-10-20T07:30:00+02:00"))//
			.andExpect(MockMvcResultMatchers.jsonPath("$..[1].endTimeStamp").value("2019-10-20T16:30:00+02:00"));
	}

	@Test
	@Order(2)
	@Rollback(false)
	void getWorkSession_returnsWorkSession() throws Exception {
		createWorkSession(ZonedDateTime.parse("2020-12-24T07:30:00+02:00"), Duration.ofHours(9));
		
		// Erkannte Problemstellung: Zeiterfassungseinträge sollten nicht durch eine ID ansteuerbar sein,
		// sondern durch einen eindeutigen, nicht mehr änderbaren Schlüssel. z.B. 'e8r2x' erlaubt 34^5 Einträge je Mitarbeiter.
		// Grund: IDs in der Datenbank können sich ändern. Außerdem werden somit Datenbank-Details preisgegeben.
		// Und vor allem müsste ja die REST-API bei Datenbankänderung trotzdem die ID gleichmäßig (UNIFORM) im Link haben,
		// was ist aber, falls irgendwann die Datenbank aus zwei Datenbanken besteht, und man bei der Zweiten mit der ID wieder bei 1 anfängt?
		// Idee: worksessions/20251201-1400 -> YYYYMMTT-HHMM -> Constraint: es ist nur eine neue Session je Minute erstellbar
		// Problem: was ist wenn wir die Daten ändern? :D Dann gibt es wieder keinen Key...hahahah
		
		mvc.perform(get("/users/DemoUser/worksessions/3").contentType(MediaType.APPLICATION_JSON)) //
			.andDo(result -> {
				log.info(result.getResponse().getContentAsString());
			})
			.andExpect(MockMvcResultMatchers.jsonPath("$['startTimeStamp']").value("2020-12-24T07:30:00+02:00"))//
			.andExpect(MockMvcResultMatchers.jsonPath("$['endTimeStamp']").value("2020-12-24T16:30:00+02:00"));
	}
}
