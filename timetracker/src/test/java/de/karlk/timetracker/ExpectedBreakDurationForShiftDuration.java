package de.karlk.timetracker;

import java.time.Duration;

public class ExpectedBreakDurationForShiftDuration {
	Duration totalShiftDuration;
	Duration expectedBreakDuration;
	public ExpectedBreakDurationForShiftDuration(Duration totalShiftDuration, Duration expectedBreakDuration) {
		this.expectedBreakDuration = expectedBreakDuration;
		this.totalShiftDuration = totalShiftDuration;
	}
	
	/**
	 * @param duration in the form of 'h:mm' with ":" as delimitter
	 * @return
	 */
	public static ExpectedBreakDurationForShiftDuration byString(String totalShiftDuration, String expectedBreakDuration) {
		Duration shiftDuration = convertTextToDuration(totalShiftDuration);
		Duration breakDuration = convertTextToDuration(expectedBreakDuration);
		return new ExpectedBreakDurationForShiftDuration(shiftDuration, breakDuration);
	}

	private static Duration convertTextToDuration(String duration) {
		String[] durationParts = duration.split(":");
		if(durationParts.length != 2) throw new IllegalArgumentException("Zeitangabe muss in der Form 'h:mm' erfolgen");
		long shiftHours = Long.parseLong(durationParts[0]);
		long shiftMinutes = Long.parseLong(durationParts[1]);
		Duration shift = Duration.ofHours(shiftHours).plus(Duration.ofMinutes(shiftMinutes));
		return shift;
	}
}
