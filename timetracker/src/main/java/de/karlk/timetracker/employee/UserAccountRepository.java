package de.karlk.timetracker.employee;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

	/**
	 * questionable solution, because there can only be one userAccount by the unique name
	 * 
	 * <p>should be changed as soon as best practises in this regard are known
	 */
	List<UserAccount> findByName(String name);
	Optional<UserAccount> findFirstByName(String name);

}