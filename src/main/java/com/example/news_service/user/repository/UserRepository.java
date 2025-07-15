package com.example.news_service.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.news_service.user.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	/**
	 * 이메일로 사용자 찾기
	 */
	Optional<User> findByEmail(String email);
	
	/**
	 * 이메일 중복 체크
	 */
	boolean existsByEmail(String email);
}
