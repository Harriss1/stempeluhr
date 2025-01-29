package de.karlk.timetracker.measurement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.karlk.timetracker.Employee;
import de.karlk.timetracker.EmployeeRepository;

@Service
class WorkSessionServiceImpl implements WorkSessionService {

	@Autowired
	EmployeeRepository employeeRepo;
	
	@Autowired
	WorkSessionRepository workSessionRepo;
	
	public WorkSession createAndStartWorkSessionFor(Employee employee) {
		WorkSession session = new WorkSession(employee);
		workSessionRepo.saveAndFlush(session);
		return session;
	}

	@Override
	public void finishWorkSession(WorkSession session) {
		session.finishNow();
	}

}
