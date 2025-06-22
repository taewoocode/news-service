package com.example.news_service.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.news_service.user.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
