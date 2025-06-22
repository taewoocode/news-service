package com.example.news_service.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.news_service.user.domain.User;
import com.example.news_service.user.domain.UserRole;
import com.example.news_service.user.domain.UserStatus;
import com.example.news_service.user.dto.UserSignUpInfo;
import com.example.news_service.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private UserConverter userConverter;

	@InjectMocks
	private UserServiceImpl userService;

	@Test
	@DisplayName("회원가입 성공")
	void signUp_Success() {
		// Given
		UserSignUpInfo.UserSignUpRequest request = UserSignUpInfo.UserSignUpRequest.builder()
				.email("test@example.com")
				.password("password123")
				.name("테스트 사용자")
				.phone("01012345678")
				.build();

		User user = User.builder()
				.email("test@example.com")
				.password("encodedPassword")
				.name("테스트 사용자")
				.phone("01012345678")
				.role(UserRole.USER)
				.status(UserStatus.ACTIVE)
				.build();

		UserSignUpInfo.UserSignUpResponse expectedResponse = UserSignUpInfo.UserSignUpResponse.builder()
				.id(1L)
				.email("test@example.com")
				.name("테스트 사용자")
				.phone("01012345678")
				.build();

		when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
		when(userConverter.createSignUpEntity(request)).thenReturn(user);
		when(userRepository.save(user)).thenReturn(user);
		when(userConverter.createSignUpResponse(user)).thenReturn(expectedResponse);

		// When
		UserSignUpInfo.UserSignUpResponse response = userService.signUp(request);

		// Then
		assertThat(response.getEmail()).isEqualTo("test@example.com");
		assertThat(response.getName()).isEqualTo("테스트 사용자");
	}

	@Test
	@DisplayName("회원가입 실패 - 이메일 중복")
	void signUp_Failure_DuplicateEmail() {
		// Given
		UserSignUpInfo.UserSignUpRequest request = UserSignUpInfo.UserSignUpRequest.builder()
				.email("test@example.com")
				.password("password123")
				.name("테스트 사용자")
				.build();

		when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

		// When & Then
		assertThatThrownBy(() -> userService.signUp(request))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("이미 존재하는 이메일입니다: test@example.com");
	}
} 