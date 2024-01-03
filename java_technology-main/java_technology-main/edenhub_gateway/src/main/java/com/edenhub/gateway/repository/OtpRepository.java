package com.edenhub.gateway.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edenhub.gateway.domain.Otp;

/**
 * Spring Data SQL repository for the Otp entity.
 */
@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
	
	Optional<Otp> findByUserName(String userName);

	Optional<Otp> findByOtpAndUserName(String otp, String userName);
	
	Optional<Otp> findByActiveKey(String activateKey); //primary

	Optional<Otp> findByUserNameAndActiveKey(String userName, String activeKey);
	
}
