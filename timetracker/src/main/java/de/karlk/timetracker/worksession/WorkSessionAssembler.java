package de.karlk.timetracker.worksession;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class WorkSessionAssembler implements RepresentationModelAssembler<WorkSession, EntityModel<WorkSession>> {

	@Override
	public EntityModel<WorkSession> toModel(WorkSession worksession) {
		return EntityModel.of(worksession, //
				linkTo(methodOn(WorkSessionController.class).one(worksession.getEmployee().getUserAccount().getName(), worksession.getId().toString())).withSelfRel(),
				linkTo(methodOn(WorkSessionController.class).all(worksession.getEmployee().getUserAccount().getName())).withRel("worksessions"));
	}

}
