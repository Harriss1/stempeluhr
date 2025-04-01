package de.karlk.timetracker;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import de.karlk.timetracker.employee.UserAccountRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = TimetrackerApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class UserAccountControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private UserAccountRepository userAccountRepository;
	@Test
	public void givenDemoUser_whenGetUsers_thenStatus200() throws Exception {
		if(userAccountRepository.findByName(TimetrackerApplication.DEMO_USER_NAME).size() < 1) {
			throw new IllegalStateException("'DemoUser' Account muss existieren für Tests");
		}
		
		mvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andExpect(MockMvcResultMatchers.jsonPath("$..[0].name").value(TimetrackerApplication.DEMO_USER_NAME));
	}

}
