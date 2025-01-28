package de.karlk.timetracker;
import org.springframework.data.jpa.repository.JpaRepository;

interface EmployeeRepository extends JpaRepository<User, Long> {

	User findByFirstName(String name);

}