package de.karlk.timetracker.measurement;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.karlk.timetracker.Employee;
import de.karlk.timetracker.EmployeeRepository;
import de.karlk.timetracker.UserAccount;

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
	public void finishAndSaveWorkSession(WorkSession session) {
		session.finishNow();
		workSessionRepo.saveAndFlush(session);
	}

	@Override
	public void saveWorkSession(WorkSession session) {
		workSessionRepo.saveAndFlush(session);
	}

	@Override
	public Duration calculateNetWorkDurationBetween(ZonedDateTime start, ZonedDateTime end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorkSession findFirstWorkSessionAfter(ZonedDateTime searchStartingPoint, Employee employee) {
		List<WorkSession> sessions = workSessionRepo.findFirstByStartTimeStampAfterAndEmployee(searchStartingPoint, employee);
		if(sessions.size() == 0)
			throw new IllegalArgumentException("Es gibt für "+ employee.getUserAccount().getName() +" keine Messungen nach " + searchStartingPoint.toString());
		return sessions.get(0);
	}

}
