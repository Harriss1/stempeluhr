package de.karlk.timetracker.measurements;

import java.time.Duration;

import lombok.Getter;

public enum LegalShiftType {

	NO_BREAK(Duration.ofMinutes(0), Duration.ofMinutes(0)),
	REGULAR_SHIFT(Duration.ofMinutes(30), Duration.ofHours(6)),
	LONG_SHIFT(Duration.ofMinutes(45), Duration.ofHours(9));

	@Getter
	private final Duration legalBreakDuration;
	private final Duration lowerShiftDurationLimit;
	
	private LegalShiftType(Duration breakDuration, Duration lowerShiftDurationLimit) {
		this.legalBreakDuration = breakDuration;
		this.lowerShiftDurationLimit = lowerShiftDurationLimit;
	}
	
	public static LegalShiftType byTotalShiftDuration(Duration totalShiftDuration) {
		if(totalShiftDuration.compareTo(LONG_SHIFT.lowerShiftDurationLimit) >= 0) {
			return LONG_SHIFT;
		}
		if(totalShiftDuration.compareTo(REGULAR_SHIFT.lowerShiftDurationLimit) >= 0) {
			return REGULAR_SHIFT;
		}
		return NO_BREAK;
	}
}
