package com.example.news_service.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.news_service.user.domain.User;
import com.example.news_service.user.domain.UserRole;
import com.example.news_service.user.domain.UserStatus;
import com.example.news_service.user.dto.UserSignUpInfo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserConverter {
	
	private final PasswordEncoder passwordEncoder;
	
	/**
	 * UserSignUpRequest를 User 엔티티로 변환
	 */
	public User createSignUpEntity(UserSignUpInfo.UserSignUpRequest request) {
		return User.builder()
			.email(request.getEmail())
			.password(passwordEncoder.encode(request.getPassword()))
			.name(request.getName())
			.phone(request.getPhone())
			.profileImage(request.getProfileImage())
			.role(UserRole.USER)
			.status(UserStatus.ACTIVE)
			.build();
	}

	public UserSignUpInfo.UserSignUpResponse createSignUpResponse(User savedUser) {
		return UserSignUpInfo.UserSignUpResponse.builder()
			.id(savedUser.getId())
			.email(savedUser.getEmail())
			.name(savedUser.getName())
			.phone(savedUser.getPhone())
			.profileImage(savedUser.getProfileImage())
			.createdAt(savedUser.getCreatedAt())
			.build();
	}
}
