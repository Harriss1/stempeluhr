package de.karlk.timetracker.worksession;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;

import de.karlk.timetracker.employee.Employee;
import de.karlk.timetracker.employee.UserAccount;

public interface WorkSessionService {
	
	WorkSession createAndStartWorkSessionNowFor(Employee employee);

	void finishNowAndSaveWorkSession(WorkSession session);

	WorkSession saveWorkSession(WorkSession session);

	/**
	 * Adds up the net amount of work durations of all worksessions in the given timespan.
	 * @param start
	 * @param end
	 * @param employee
	 */
	Duration calculateSumOfNetWorkDurations(ZonedDateTime start, ZonedDateTime end, Employee employee);

	/**
	 * @param searchStartingPoint reminder: substract one second to find entries that just got created   
	 * @param employee
	 * @return first found worksession 
	 * @throws IllegalArgumentException if there are no worksessions found
	 */
	WorkSession findFirstWorkSessionAfter(ZonedDateTime searchStartingPoint, Employee employee) throws IllegalArgumentException;

	List<WorkSession> getAll(String userAccountName);

	Optional<WorkSession> findById(long id);
}
