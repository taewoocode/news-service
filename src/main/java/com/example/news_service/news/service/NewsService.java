package com.example.news_service.news.service;

import com.example.news_service.news.dto.NewsListInfo;
import com.example.news_service.news.dto.NewsReadInfo;

public interface NewsService {

	/**
	 * 뉴스 상세 조회
	 */
	NewsReadInfo.NewsReadResponse findById(Long id);

	/**
	 * 카테고리별 뉴스 목록 조회
	 */
	NewsListInfo.NewsListResponse findByCategory(String category, int page, int size);

	/**
	 * 키워드로 뉴스 검색
	 */
	NewsListInfo.NewsListResponse searchByKeyword(String keyword, int page, int size);

	/**
	 * 최신 뉴스 목록 조회
	 */
	NewsListInfo.NewsListResponse findLatestNews(int page, int size);

	/**
	 * 뉴스 내용 업데이트
	 */
	void updateContent(Long newsId, String content);
}



