package de.karlk.timetracker;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "person")
@Getter
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    private String firstname;

    protected Employee() {}
    
    public Employee(String firstName) {
    	this.firstname = firstName;
    }
    
    @Override
    public boolean equals(Object o) {

      if (this == o)
        return true;
      if (!(o instanceof Employee))
        return false;
      Employee employee = (Employee) o;
      return Objects.equals(this.id, employee.id) && Objects.equals(this.firstname, employee.firstname);
    }
    
    @Override
    public int hashCode() {
      return Objects.hash(this.id, this.firstname);
    }
}
