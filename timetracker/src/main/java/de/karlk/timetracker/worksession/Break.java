package de.karlk.timetracker.worksession;

import java.time.ZonedDateTime;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
/**
 * Idee um auch zu erlauben Pausenzeiten zu stempeln.
 * 
 * <p>Meines Erachtens nach ist dies aber zusätzlicher Zeitaufwand und das Fertigstellen der Hauptaufgaben hat Vorrang.
 */
//@Entity
//@Table(name = "break")
class Break {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Getter
	private Long id;
	
	@Getter
	@Setter
	private ZonedDateTime startTimeStamp;
	
	@Getter
	@Setter
	private ZonedDateTime endTimeStamp;
}
