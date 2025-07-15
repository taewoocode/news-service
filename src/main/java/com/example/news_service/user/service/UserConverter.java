package com.example.news_service.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.news_service.user.domain.User;
import com.example.news_service.user.domain.UserRole;
import com.example.news_service.user.domain.UserStatus;
import com.example.news_service.user.dto.UserDeleteInfo;
import com.example.news_service.user.dto.UserReadInfo;
import com.example.news_service.user.dto.UserSignUpInfo;
import com.example.news_service.user.dto.UserUpdateInfo;

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

	/**
	 * User 엔티티를 UserReadResponse로 변환
	 */
	public UserReadInfo.UserReadResponse createReadResponse(User user) {
		return UserReadInfo.UserReadResponse.builder()
			.id(user.getId())
			.email(user.getEmail())
			.name(user.getName())
			.phone(user.getPhone())
			.profileImage(user.getProfileImage())
			.role(user.getRole().name())
			.status(user.getStatus().name())
			.createdAt(user.getCreatedAt())
			.updatedAt(user.getUpdatedAt())
			.build();
	}

	/**
	 * User 엔티티를 UserUpdateResponse로 변환
	 */
	public UserUpdateInfo.UserUpdateResponse createUpdateResponse(User user) {
		return UserUpdateInfo.UserUpdateResponse.builder()
			.id(user.getId())
			.email(user.getEmail())
			.name(user.getName())
			.phone(user.getPhone())
			.profileImage(user.getProfileImage())
			.updatedAt(user.getUpdatedAt())
			.build();
	}

	/**
	 * UserDeleteResponse 생성
	 */
	public UserDeleteInfo.UserDeleteResponse createDeleteResponse(Long userId) {
		return UserDeleteInfo.UserDeleteResponse.builder()
			.message("사용자가 성공적으로 삭제되었습니다.")
			.deletedUserId(userId)
			.build();
	}
}
