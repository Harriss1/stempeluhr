package de.karlk.timetracker.worksession;

import java.time.Duration;
import java.time.ZonedDateTime;

import de.karlk.timetracker.employee.Employee;

public interface WorkSessionService {
	
	WorkSession createAndStartWorkSessionNowFor(Employee employee);

	void finishNowAndSaveWorkSession(WorkSession session);

	void saveWorkSession(WorkSession session);

	Duration sumUpNetWorkDurationBetween(ZonedDateTime start, ZonedDateTime end, Employee employee);

	/**
	 * @param searchStartingPoint reminder: substract one second to find entries that just got created   
	 * @param employee
	 * @return
	 */
	WorkSession findFirstWorkSessionAfter(ZonedDateTime searchStartingPoint, Employee employee);
}
