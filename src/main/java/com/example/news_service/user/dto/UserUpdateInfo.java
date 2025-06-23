package com.example.news_service.user.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserUpdateInfo {

	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class UserUpdateRequest {
		@Size(min = 2, max = 20, message = "이름은 2자 이상 20자 이하여야 합니다.")
		private String name;

		@Pattern(regexp = "^[0-9]{10,11}$", message = "올바른 전화번호 형식이 아닙니다.")
		private String phone;

		private String profileImage;
	}

	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class UserUpdateResponse {
		private Long id;
		private String email;
		private String name;
		private String phone;
		private String profileImage;
		private LocalDateTime updatedAt;
	}
} 