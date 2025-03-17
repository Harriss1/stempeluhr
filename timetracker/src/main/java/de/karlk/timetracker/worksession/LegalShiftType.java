package de.karlk.timetracker.worksession;

import java.time.Duration;

import lombok.Getter;

/**
 * According to <u>Arbeitszeitgesetz (ArbZG) § 4 Ruhepausen</u>
 * <p>
 * <i>Die Arbeit ist durch im voraus feststehende Ruhepausen von mindestens 30
 * Minuten bei einer Arbeitszeit von mehr als sechs bis zu neun Stunden und 45
 * Minuten bei einer Arbeitszeit von mehr als neun Stunden insgesamt zu
 * unterbrechen.</i> 
 * 
 * <p>example: 6 hours = no break, 6 hours and 1 second = 30 min
 * break
 */
public enum LegalShiftType {

	NO_BREAK(Duration.ofMinutes(0), Duration.ofHours(6)), 
	REGULAR_SHIFT(Duration.ofMinutes(30), Duration.ofHours(9)),
	LONG_SHIFT(Duration.ofMinutes(45), Duration.ofHours(10));

	@Getter
	private final Duration legalBreakDuration;
	@Getter
	private final Duration maxShiftDuration;

	private LegalShiftType(Duration breakDuration, Duration maxShiftDuration) {
		this.legalBreakDuration = breakDuration;
		this.maxShiftDuration = maxShiftDuration;
	}

	public static LegalShiftType byTotalShiftDuration(Duration totalShiftDuration) {
		if (totalShiftDuration.toSeconds() > LONG_SHIFT.getMaxShiftDuration().toSeconds()) {
			throw new UnsupportedOperationException("Es werden nur Arbeitszeiten bis einschließlich 10 Stunden unterstützt.");
		}
		if (totalShiftDuration.toSeconds() > REGULAR_SHIFT.getMaxShiftDuration().toSeconds()) {
			return LONG_SHIFT;
		}
		if (totalShiftDuration.toSeconds() > NO_BREAK.getMaxShiftDuration().toSeconds()) {
			return REGULAR_SHIFT;
		}
		return NO_BREAK;
	}
}
