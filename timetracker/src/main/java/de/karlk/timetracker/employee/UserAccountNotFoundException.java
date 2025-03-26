package de.karlk.timetracker.employee;

@SuppressWarnings("serial")
public class UserAccountNotFoundException extends RuntimeException {

  UserAccountNotFoundException(String name) {
	    super("Could not find userAccount '" + name + "'");
  }
}
