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
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;

    @Getter @Setter
    private String firstName;

    protected Employee() {}
    
    public Employee(String firstName) {
    	this.firstName = firstName;
    }
    
    @Override
    public boolean equals(Object o) {

      if (this == o)
        return true;
      if (!(o instanceof Employee))
        return false;
      Employee employee = (Employee) o;
      return Objects.equals(this.id, employee.id) && Objects.equals(this.firstName, employee.firstName);
    }
    
    @Override
    public int hashCode() {
      return Objects.hash(this.id, this.firstName);
    }
}
