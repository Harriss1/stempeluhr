package de.karlk.timetracker;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

interface UserRepository extends JpaRepository<UserAccount, Long> {

	List<UserAccount> findByName(String name);

}