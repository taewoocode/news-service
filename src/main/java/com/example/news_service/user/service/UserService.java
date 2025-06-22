package com.example.news_service.user.service;

import com.example.news_service.user.dto.UserSignUpInfo;

public interface UserService {

	/**
	 * User 회원가입
	 * @param request
	 * @return
	 */
	UserSignUpInfo.UserSignUpResponse signUp(UserSignUpInfo.UserSignUpRequest request);
}
