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
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;

    @Getter @Setter
    private String name;

    protected User() {}
    
    public User(String name) {
    	this.name = name;
    }
    
    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (!(o instanceof User))
        return false;
      User user = (User) o;
      return Objects.equals(this.id, user.id) && Objects.equals(this.name, user.name);
    }
    
    @Override
    public int hashCode() {
      return Objects.hash(this.id, this.name);
    }
}
