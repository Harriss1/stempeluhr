package de.karlk.timetracker.worksession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import de.karlk.timetracker.employee.UserAccount;
import de.karlk.timetracker.employee.UserAccountRepository;

@RestController
public class WorkSessionController {
	
	@Autowired UserAccountRepository userAccountRepository;
	@Autowired WorkSessionService workSessionService;
	
	@PostMapping("/users/{userAccountName}/worksessions")
	WorkSession createWorkSession(@PathVariable String userAccountName){
		UserAccount userAccount = userAccountRepository.findByName(userAccountName).get(0);
		var employee = userAccount.getEmployee();
		return workSessionService.createAndStartWorkSessionNowFor(employee);
	}
	
}
