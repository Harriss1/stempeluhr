package de.karlk.timetracker;
import org.springframework.data.jpa.repository.JpaRepository;

interface UserRepository extends JpaRepository<User, Long> {

	User findByName(String name);

}