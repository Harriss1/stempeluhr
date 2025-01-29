package de.karlk.timetracker.measurement;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.karlk.timetracker.Employee;

@Repository
interface WorkSessionRepository extends JpaRepository<WorkSession, Long> {
	
	List<WorkSession> findByStartTimeStampAfterAndEmployeeOrderByStartTimeStampAsc(ZonedDateTime searchStartingPoint, Employee employee);

	List<WorkSession> findByStartTimeStampAfterAndEndTimeStampBeforeAndEmployee(ZonedDateTime start, ZonedDateTime end,
			Employee employee);

}