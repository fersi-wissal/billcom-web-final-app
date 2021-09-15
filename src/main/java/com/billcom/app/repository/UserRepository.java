package com.billcom.app.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.billcom.app.entity.UserApp;
@Repository
public interface UserRepository  extends JpaRepository<UserApp, Long>{

	boolean existsByEmail(String email);
    boolean existsByResetToken(String resetToken);
	Optional<UserApp> findUserByEmail(String email);

    UserApp findOneByEmail(String email);
	Optional<UserApp> findUserByResetToken(String resetToken);
	Optional<UserApp> getById(long id);
	Optional<UserApp> findByEmail(String email);
	Optional<UserApp> getMemberById(Long idList);
	Optional<UserApp> findByPassword(String password);

}
