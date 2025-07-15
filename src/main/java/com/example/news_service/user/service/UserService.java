package com.example.news_service.user.service;

import com.example.news_service.user.dto.UserDeleteInfo;
import com.example.news_service.user.dto.UserReadInfo;
import com.example.news_service.user.dto.UserSignUpInfo;
import com.example.news_service.user.dto.UserUpdateInfo;

public interface UserService {

	/**
	 * User 회원가입
	 */
	UserSignUpInfo.UserSignUpResponse signUp(UserSignUpInfo.UserSignUpRequest request);

	/**
	 * User 조회
	 */
	UserReadInfo.UserReadResponse findById(Long id);

	/**
	 * User 수정
	 */
	UserUpdateInfo.UserUpdateResponse update(Long id, UserUpdateInfo.UserUpdateRequest request);

	/**
	 * User 삭제
	 */
	UserDeleteInfo.UserDeleteResponse delete(Long id);
}
