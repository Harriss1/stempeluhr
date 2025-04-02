package de.karlk.timetracker.worksession;

@SuppressWarnings("serial")
public class WorkSessionNotFoundException extends RuntimeException {

  public WorkSessionNotFoundException(String id) {
	    super("Could not find worksession of ID '" + id + "'");
  }
}
