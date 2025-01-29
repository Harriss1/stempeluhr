package de.karlk.timetracker;
import org.springframework.data.jpa.repository.JpaRepository;

interface UserRepository extends JpaRepository<UserAccount, Long> {

	UserAccount findByName(String name);

}