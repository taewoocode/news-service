package com.example.news_service.user.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String name;

	private String phone;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole role;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserStatus status;

	private String profileImage;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@Builder
	public User(String email, String password, String name, String phone, UserRole role, UserStatus status,
		String profileImage) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.phone = phone;
		this.role = role;
		this.status = status;
		this.profileImage = profileImage;
	}

	public void updateProfile(String name, String phone, String profileImage) {
		this.name = name;
		this.phone = phone;
		this.profileImage = profileImage;
	}

	public void changePassword(String newPassword) {
		this.password = newPassword;
	}

	public void isValidPassword() {
		if (password == null || password.length() < 8) {
			throw new IllegalArgumentException("비밀번호는 최소 8자리 이상이여야 합니다.");
		}
	}
}