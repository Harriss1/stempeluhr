package de.karlk.timetracker.measurement;

import java.time.Duration;
import java.time.ZonedDateTime;

import de.karlk.timetracker.Employee;

public interface WorkSessionService {
	
	WorkSession createAndStartWorkSessionFor(Employee employee);

	void finishAndSaveWorkSession(WorkSession session);

	void saveWorkSession(WorkSession session);

	Duration calculateNetWorkDurationBetween(ZonedDateTime start, ZonedDateTime end);

	WorkSession findFirstWorkSessionAfter(ZonedDateTime searchStartingPoint, Employee employee);
}
