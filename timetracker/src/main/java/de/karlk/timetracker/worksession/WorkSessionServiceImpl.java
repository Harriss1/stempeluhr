package de.karlk.timetracker.worksession;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.karlk.timetracker.employee.Employee;
import de.karlk.timetracker.employee.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
	public void finishNowAndSaveWorkSession(WorkSession session) {
		session.finishNow();
		workSessionRepo.saveAndFlush(session);
	}

	@Override
	public void saveWorkSession(WorkSession session) {
		workSessionRepo.saveAndFlush(session);
	}

	@Override
	public Duration calculateNetWorkDurationBetween(ZonedDateTime start, ZonedDateTime end, Employee employee) {
		log.info("----- Suche zwischen Datumsangaben ------");
		List<WorkSession> sessions = workSessionRepo.findByStartTimeStampAfterAndEndTimeStampBeforeAndEmployee(start, end, employee);
		Duration duration = Duration.ZERO;
		if(sessions.size() == 0) {
			log.warn("keine Einträge im Zeitraum {} - {} gefunden", start, end);
			return duration;
		}
		for(var session : sessions) {
			log.info("session net dur:" + session.getNetDuration().toString());
			duration = duration.plus(session.getNetDuration());
		}
		log.info("total dur:" + duration.toString());
		return duration;
	}

	@Override
	public WorkSession findFirstWorkSessionAfter(ZonedDateTime searchStartingPoint, Employee employee) {
		List<WorkSession> sessions = workSessionRepo.findByStartTimeStampAfterAndEmployeeOrderByStartTimeStampAsc(searchStartingPoint, employee);
		if(sessions.size() == 0)
			throw new IllegalArgumentException("Es gibt für "+ employee.getUserAccount().getName() +" keine Messungen nach " + searchStartingPoint.toString());
		return sessions.get(0);
	}

}
