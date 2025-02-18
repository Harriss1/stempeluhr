package de.karlk.timetracker;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UserRepository extends JpaRepository<UserAccount, Long> {

	List<UserAccount> findByName(String name);

}