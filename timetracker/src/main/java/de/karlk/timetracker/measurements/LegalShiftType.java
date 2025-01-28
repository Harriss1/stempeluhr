package de.karlk.timetracker.measurements;

import java.time.Duration;

import lombok.Getter;

public enum LegalShiftType {

	NO_BREAK(Duration.ofMinutes(0), Duration.ofMinutes(0)),
	REGULAR_SHIFT(Duration.ofMinutes(30), Duration.ofHours(6)),
	LONG_SHIFT(Duration.ofMinutes(45), Duration.ofHours(9));

	@Getter
	private final Duration legalBreakDuration;
	private final Duration totalShiftDuration;
	
	private LegalShiftType(Duration breakDuration, Duration totalShiftDuration) {
		this.legalBreakDuration = breakDuration;
		this.totalShiftDuration = totalShiftDuration;
	}
	
	public static LegalShiftType byTotalShiftDuration(Duration workDuration) {
		if(workDuration.compareTo(LONG_SHIFT.totalShiftDuration) >= 0) {
			return LONG_SHIFT;
		}
		if(workDuration.compareTo(REGULAR_SHIFT.totalShiftDuration) >= 0) {
			return REGULAR_SHIFT;
		}
		return NO_BREAK;
	}
}
