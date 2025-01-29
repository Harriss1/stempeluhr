package de.karlk.timetracker.measurements;

import java.time.Duration;
import java.time.ZonedDateTime;

import de.karlk.timetracker.Employee;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

//@Entity
//@Table(name = "work_session")
public class WorkSession {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Getter
	private Long id;
	
	@Nonnull
	@Getter
	/**
	 * API-specification: created worksessions should never be able to edit the assigned employee
	 */
	private Employee employee;
	
	@Getter
	@Setter
	private ZonedDateTime startTimeStamp; // not sure if this ZonedDateTime class is supported by jpa and how it works
	
	@Getter
	private ZonedDateTime endTimeStamp;
	
	@Getter
	@Setter
	// should be NULL as long as the endTimeStamp is not set and the breakduration calculated
	// Long? must be tested if Hibernate supports Duration, online ressources say from version 5 onwards
	private Duration breakDuration;
	
	protected WorkSession() {}
	
	public WorkSession(Employee employee) {
		this.employee = employee;
		this.startTimeStamp = ZonedDateTime.now();
	}
	
	public void setEndTimeStamp(ZonedDateTime time) {
		this.endTimeStamp=time;
		manageBreakDuration();
	}
	
	public void finishNow() {
		this.endTimeStamp = ZonedDateTime.now();
		manageBreakDuration();
	}
	
	private void manageBreakDuration() {
		LegalShiftType legalShiftType = LegalShiftType.byTotalShiftDuration(getTotalDuration());
		this.breakDuration = legalShiftType.getLegalBreakDuration();
	}
	
	public Duration getTotalDuration() {
		long end = endTimeStamp.toEpochSecond();
		long start = startTimeStamp.toEpochSecond();
		return Duration.ofSeconds(end - start);
	}
}
