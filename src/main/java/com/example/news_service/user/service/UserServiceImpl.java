package com.example.news_service.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.news_service.user.domain.User;
import com.example.news_service.user.dto.UserSignUpInfo;
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
}
