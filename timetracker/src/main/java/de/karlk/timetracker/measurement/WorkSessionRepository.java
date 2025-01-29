package de.karlk.timetracker.measurement;

import java.time.ZonedDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface WorkSessionRepository extends JpaRepository<WorkSession, Long> {
	
	WorkSession findByStartTimeStamp(ZonedDateTime zonedDateTime);

}