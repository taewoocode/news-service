package com.example.news_service.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.news_service.user.domain.User;
import com.example.news_service.user.dto.UserDeleteInfo;
import com.example.news_service.user.dto.UserReadInfo;
import com.example.news_service.user.dto.UserSignUpInfo;
import com.example.news_service.user.dto.UserUpdateInfo;
import com.example.news_service.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserConverter userConverter;

	@Override
	@Transactional
	public UserSignUpInfo.UserSignUpResponse signUp(UserSignUpInfo.UserSignUpRequest request) {
		log.info("회원가입 요청: {}", request.getEmail());

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + request.getEmail());
		}
		User user = userConverter.createSignUpEntity(request);
		user.isValidPassword();

		User savedUser = userRepository.save(user);
		log.info("회원가입 완료: {}", savedUser.getEmail());
		return userConverter.createSignUpResponse(savedUser);
	}

	@Override
	@Transactional(readOnly = true)
	public UserReadInfo.UserReadResponse findById(Long id) {
		log.info("사용자 조회 요청: {}", id);

		User user = userRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + id));

		return userConverter.createReadResponse(user);
	}

	@Override
	@Transactional
	public UserUpdateInfo.UserUpdateResponse update(Long id, UserUpdateInfo.UserUpdateRequest request) {
		log.info("사용자 수정 요청: {}", id);

		User user = userRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + id));

		user.updateProfile(request.getName(), request.getPhone(), request.getProfileImage());
		User updatedUser = userRepository.save(user);

		log.info("사용자 수정 완료: {}", updatedUser.getEmail());
		return userConverter.createUpdateResponse(updatedUser);
	}

	@Override
	@Transactional
	public UserDeleteInfo.UserDeleteResponse delete(Long id) {
		log.info("사용자 삭제 요청: {}", id);

		User user = userRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + id));

		userRepository.delete(user);
		log.info("사용자 삭제 완료: {}", user.getEmail());
		return userConverter.createDeleteResponse(id);
	}
}
