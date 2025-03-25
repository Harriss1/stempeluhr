package de.karlk.timetracker.employee;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import de.karlk.timetracker.worksession.WorkSession;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "employee")
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Getter
	private Long id;

	@Getter
	@Setter
	private String firstName;

	@Getter
	@Setter
	private String lastName;
	
	@Getter
	@OneToOne(mappedBy = "employee")
	@JsonManagedReference
	private UserAccount userAccount;
	
	@Getter
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
	@JsonBackReference
	private List<WorkSession> workSessions;

//	@Getter
//	@Setter
//	@Nullable
//	// allows the creation of rules to forbid new worksessions if one is active for this employee
//	private WorkSession activeWorkSession; 	

	protected Employee() {
	}

	public Employee(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Employee))
			return false;
		Employee user = (Employee) o;
		return Objects.equals(this.id, user.id) && Objects.equals(this.firstName, user.firstName)
				&& Objects.equals(this.lastName, user.lastName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.firstName, this.lastName);
	}
}
