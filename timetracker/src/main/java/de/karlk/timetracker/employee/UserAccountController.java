package de.karlk.timetracker.employee;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class UserAccountController {
	@Autowired UserAccountRepository userAccountRepository;
	
	@GetMapping("/userAccounts")
	List<UserAccount> allNotRestful(){
		return userAccountRepository.findAll();
	}
	
	@GetMapping("/users")
	CollectionModel<EntityModel<UserAccount>> all(){
		List<EntityModel<UserAccount>> users = userAccountRepository.findAll().stream().map(userAccount -> EntityModel.of(userAccount,
				linkTo(methodOn(UserAccountController.class).one(userAccount.getName())).withSelfRel(),
				linkTo(methodOn(UserAccountController.class).all()).withRel("users")))
				.collect(Collectors.toList());
		
		return CollectionModel.of(users, linkTo(methodOn(UserAccountController.class).all()).withSelfRel());
	}
	
	@GetMapping("/users/{userAccountName}")
	EntityModel<UserAccount> one(@PathVariable String userAccountName){
		
		UserAccount acc = userAccountRepository.findFirstByName(userAccountName)
				.orElseThrow(() -> new UserAccountNotFoundException(userAccountName));
		
		return EntityModel.of(acc, 
				linkTo(methodOn(UserAccountController.class).one(userAccountName)).withSelfRel(),
				linkTo(methodOn(UserAccountController.class).all()).withRel("users"));
	}
}
