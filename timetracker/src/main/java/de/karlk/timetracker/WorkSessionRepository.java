package de.karlk.timetracker;

import java.time.ZonedDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

interface WorkSessionRepository extends JpaRepository<WorkSession, Long> {
	
	WorkSession findByStartTimeStamp(ZonedDateTime zonedDateTime);

}