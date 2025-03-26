package de.karlk.timetracker.employee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAccountController {
	@Autowired UserAccountRepository userAccountRepository;
	
	@GetMapping("/users")
	List<UserAccount> all(){
		return userAccountRepository.findAll();
	}
	
	
	@GetMapping("/users/{userAccountName}")
	UserAccount one(@PathVariable String userAccountName){
		return userAccountRepository.findFirstByName(userAccountName)
				.orElseThrow(() -> new UserAccountNotFoundException(userAccountName));
	}
}
