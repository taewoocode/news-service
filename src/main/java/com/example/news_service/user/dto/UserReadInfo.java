package com.example.news_service.user.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserReadInfo {

	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class UserReadRequest {
		private Long id;
	}

	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class UserReadResponse {
		private Long id;
		private String email;
		private String name;
		private String phone;
		private String profileImage;
		private String role;
		private String status;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;
	}
} 