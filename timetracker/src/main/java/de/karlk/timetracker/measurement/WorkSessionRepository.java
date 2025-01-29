package de.karlk.timetracker.measurement;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.karlk.timetracker.Employee;

@Repository
interface WorkSessionRepository extends JpaRepository<WorkSession, Long> {
	
	List<WorkSession> findByStartTimeStampAfterAndEmployee(ZonedDateTime searchStartingPoint, Employee employee);

}