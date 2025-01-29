package de.karlk.timetracker;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

interface EmployeeRepository extends JpaRepository<Employee, Long> {

	List<Employee> findByFirstName(String firstName);

}