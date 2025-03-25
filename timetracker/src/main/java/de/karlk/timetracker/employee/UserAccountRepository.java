package de.karlk.timetracker.employee;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

	/**
	 * name of UserAccount is unique
	 */
	List<UserAccount> findByName(String name);

}