package de.karlk.timetracker;

import java.util.Objects;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_account")
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;

    @Getter @Setter
    @Column(unique=true)
    private String name;

    @Getter @Setter 
    @Nullable
    @OneToOne
	// User is the owning side of the relationship on purpose, so one User is only allowed to timestamp for one Employee
	@JoinColumn(name = "employee_id")
    private Employee employee;

    protected UserAccount() {}
    
    public UserAccount(String name) {
    	this.name = name;
    }
    
    public UserAccount(String name, Employee employee) {
    	this.name = name;
    	this.employee = employee;
    }
    
    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (!(o instanceof UserAccount))
        return false;
      UserAccount user = (UserAccount) o;
      return Objects.equals(this.id, user.id) && Objects.equals(this.name, user.name);
    }
    
    @Override
    public int hashCode() {
      return Objects.hash(this.id, this.name);
    }
    
    @Override
    public String toString() {
    	return employee != null
    			? "{username:" + name+ "},{employee:'"+employee.getFirstName() + " " + employee.getLastName() + "'}" 
    			: "{username:" + name +"},{no employee associated}";
    }
}
