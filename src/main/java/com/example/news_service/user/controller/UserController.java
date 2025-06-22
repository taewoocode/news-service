package com.example.news_service.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.news_service.user.dto.UserSignUpInfo;
import com.example.news_service.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 관리 API")
public class UserController {

	private final UserService userService;

	@PostMapping("/signup")
	@Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "회원가입 성공",
			content = @Content(schema = @Schema(implementation = UserSignUpInfo.UserSignUpResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 (이메일 중복, 비밀번호 형식 오류 등)"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	public ResponseEntity<UserSignUpInfo.UserSignUpResponse> signUp(
			@Valid @RequestBody UserSignUpInfo.UserSignUpRequest request) {
		
		log.info("회원가입 API 호출: {}", request.getEmail());
		
		try {
			UserSignUpInfo.UserSignUpResponse response = userService.signUp(request);
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (IllegalArgumentException e) {
			log.warn("회원가입 실패: {}", e.getMessage());
			throw e; // GlobalExceptionHandler에서 처리
		} catch (Exception e) {
			log.error("회원가입 중 오류 발생: {}", e.getMessage(), e);
			throw e;
		}
	}
}
