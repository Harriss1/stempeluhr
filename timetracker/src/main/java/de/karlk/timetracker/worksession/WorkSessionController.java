package de.karlk.timetracker.worksession;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import de.karlk.timetracker.employee.UserAccount;
import de.karlk.timetracker.employee.UserAccountNotFoundException;
import de.karlk.timetracker.employee.UserAccountRepository;

@RestController
public class WorkSessionController {
	
	@Autowired UserAccountRepository userAccountRepository;
	@Autowired WorkSessionService workSessionService;
	@Autowired WorkSessionAssembler workSessionAssembler;
	
	@PostMapping("/users/{userAccountName}/worksessions")
	WorkSession create(@PathVariable String userAccountName){
		UserAccount userAccount = userAccountRepository.findByName(userAccountName).get(0);
		var employee = userAccount.getEmployee();
		return workSessionService.createAndStartWorkSessionNowFor(employee);
	}

	@GetMapping("/users/{userAccountName}/worksessions")
	CollectionModel<EntityModel<WorkSession>> all(@PathVariable String userAccountName) {
		validate(userAccountName);
		List<EntityModel<WorkSession>> worksessions = workSessionService.getAll(userAccountName) //
				.stream() //
				.map(workSessionAssembler::toModel)
				.collect(Collectors.toList());

		return CollectionModel.of(worksessions, linkTo(methodOn(WorkSessionController.class).all(userAccountName)).withSelfRel());
	}

	@GetMapping("/users/{userAccountName}/worksessions/{worksessionId}")
	EntityModel<WorkSession> one(@PathVariable String userAccountName, @PathVariable String worksessionId) {
		validate(userAccountName);
		WorkSession session = workSessionService.findById(Long.parseLong(worksessionId)) //
				.orElseThrow(null);

		return workSessionAssembler.toModel(session);
	}

	private UserAccount validate(String userAccountName) {
		UserAccount userAccount = userAccountRepository.findFirstByName(userAccountName)
				.orElseThrow(() -> new UserAccountNotFoundException(userAccountName));
		return userAccount;
	}
}
