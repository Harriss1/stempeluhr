package de.karlk.timetracker.measurement;

import de.karlk.timetracker.Employee;

public interface WorkSessionService {
	
	WorkSession createAndStartWorkSessionFor(Employee employee);

	void finishWorkSession(WorkSession session);
}
