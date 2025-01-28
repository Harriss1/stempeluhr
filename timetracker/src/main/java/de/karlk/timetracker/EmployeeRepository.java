package de.karlk.timetracker;
import org.springframework.data.jpa.repository.JpaRepository;

interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Employee findByFirstName(String firstName);

}