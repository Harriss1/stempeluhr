package de.karlk.timetracker.employee;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Using the Assembler-Pattern to reduce some repeating code
 * 
 * <p>
 * <i>A key design goal of Spring HATEOAS is to make it easier to do The Right
 * Thing™. In this scenario, that means adding hypermedia to your service
 * without hard coding a thing.</i>
 * 
 * @see <a href=
 *      "https://spring.io/guides/tutorials/rest#_simplifying_link_creation">Baeldung
 *      REST-Tutorial</a>
 */
@Component
public class UserAccountAssembler implements RepresentationModelAssembler<UserAccount, EntityModel<UserAccount>> {

	@Override
	public EntityModel<UserAccount> toModel(UserAccount userAccount) {
		return EntityModel.of(userAccount, //
				linkTo(methodOn(UserAccountController.class).one(userAccount.getName())).withSelfRel(),
				linkTo(methodOn(UserAccountController.class).all()).withRel("users"));
	}

}
