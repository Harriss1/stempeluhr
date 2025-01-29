package de.karlk.timetracker.measurement;

import java.io.Serializable;
import java.time.Duration;
import java.time.ZonedDateTime;

import de.karlk.timetracker.Employee;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "work_session")
@Slf4j
public class WorkSession  implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Getter
	private Long id;
	
	@Nonnull
	@Getter
	// no setter, because created worksessions should never be able to edit the assigned employee
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
	private Employee employee;
	
	@Getter
	private ZonedDateTime startTimeStamp; // not sure if this ZonedDateTime class is supported by jpa and how it works
	
	@Getter
	@Nullable
	private ZonedDateTime endTimeStamp;
	
	@Getter
	@Setter
	// should be NULL as long as the endTimeStamp is not set and the breakduration calculated
	// Long? must be tested if Hibernate supports Duration, online ressources say from version 5 onwards
	private Duration breakDuration;
	
	protected WorkSession() {}
	
	/**
	 * starts the measurment of time
	 *  
	 * @param employee
	 */
	public WorkSession(Employee employee) {
		this.employee = employee;
		this.startTimeStamp = ZonedDateTime.now();
	}
	
	public void setEndTimeStamp(ZonedDateTime time) {
		this.endTimeStamp=time;
		manageBreakDuration();
	}
	
	public void setStartTimeStamp(ZonedDateTime time) {
		this.startTimeStamp = time;
		if(endTimeStamp != null) {
			manageBreakDuration();
		}
	}
	
	public void finishNow() {
		this.endTimeStamp = ZonedDateTime.now();
		manageBreakDuration();
	}
	
	private void manageBreakDuration() {
		LegalShiftType legalShiftType = LegalShiftType.byTotalShiftDuration(getTotalDuration());
		this.breakDuration = legalShiftType.getLegalBreakDuration();
	}
	
	public Duration getElapsedDuration() {
		long start = startTimeStamp.toEpochSecond();
		return Duration.ofSeconds(ZonedDateTime.now().toEpochSecond() - start);
	}
	
	public Duration getTotalDuration() {
		if(endTimeStamp == null) {
			throw new IllegalStateException("Die Gesamtzeit kann erst nach Beendigung der Schicht ermittelt werden.");
		}
		long end = endTimeStamp.toEpochSecond();
		long start = startTimeStamp.toEpochSecond();
		return Duration.ofSeconds(end - start);
	}

	public Duration getNetDuration() {
		return getTotalDuration().minus(breakDuration);
	}
}
