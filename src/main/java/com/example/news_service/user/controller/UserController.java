package com.example.news_service.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.news_service.user.dto.UserDeleteInfo;
import com.example.news_service.user.dto.UserReadInfo;
import com.example.news_service.user.dto.UserSignUpInfo;
import com.example.news_service.user.dto.UserUpdateInfo;
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
			throw e;
		} catch (Exception e) {
			log.error("회원가입 중 오류 발생: {}", e.getMessage(), e);
			throw e;
		}
	}

	@GetMapping("/me")
	@Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = @Content(schema = @Schema(implementation = UserReadInfo.UserReadResponse.class))),
		@ApiResponse(responseCode = "401", description = "인증 필요"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	public ResponseEntity<UserReadInfo.UserReadResponse> getMyInfo() {
		log.info("내 정보 조회 API 호출");

		try {
			// TODO: 현재 로그인한 사용자 ID를 가져오는 로직 필요
			// Long currentUserId = getCurrentUserId();
			// UserReadInfo.UserReadResponse response = userService.findById(currentUserId);
			// return ResponseEntity.ok(response);
			throw new UnsupportedOperationException("아직 구현되지 않았습니다.");
		} catch (Exception e) {
			log.error("내 정보 조회 중 오류 발생: {}", e.getMessage(), e);
			throw e;
		}
	}

	@PutMapping("/me")
	@Operation(summary = "내 정보 수정", description = "현재 로그인한 사용자의 정보를 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "수정 성공",
			content = @Content(schema = @Schema(implementation = UserUpdateInfo.UserUpdateResponse.class))),
		@ApiResponse(responseCode = "401", description = "인증 필요"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	public ResponseEntity<UserUpdateInfo.UserUpdateResponse> updateMyInfo(
		@Valid @RequestBody UserUpdateInfo.UserUpdateRequest request) {

		log.info("내 정보 수정 API 호출");

		try {
			// TODO: 현재 로그인한 사용자 ID를 가져오는 로직 필요
			// Long currentUserId = getCurrentUserId();
			// UserUpdateInfo.UserUpdateResponse response = userService.update(currentUserId, request);
			// return ResponseEntity.ok(response);
			throw new UnsupportedOperationException("아직 구현되지 않았습니다.");
		} catch (Exception e) {
			log.error("내 정보 수정 중 오류 발생: {}", e.getMessage(), e);
			throw e;
		}
	}

	@DeleteMapping("/me")
	@Operation(summary = "회원 탈퇴", description = "현재 로그인한 사용자의 계정을 삭제합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "탈퇴 성공",
			content = @Content(schema = @Schema(implementation = UserDeleteInfo.UserDeleteResponse.class))),
		@ApiResponse(responseCode = "401", description = "인증 필요"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	public ResponseEntity<UserDeleteInfo.UserDeleteResponse> deleteMyAccount() {
		log.info("회원 탈퇴 API 호출");

		try {
			// TODO: 현재 로그인한 사용자 ID를 가져오는 로직 필요
			// Long currentUserId = getCurrentUserId();
			// UserDeleteInfo.UserDeleteResponse response = userService.delete(currentUserId);
			// return ResponseEntity.ok(response);
			throw new UnsupportedOperationException("아직 구현되지 않았습니다.");
		} catch (Exception e) {
			log.error("회원 탈퇴 중 오류 발생: {}", e.getMessage(), e);
			throw e;
		}
	}
}
