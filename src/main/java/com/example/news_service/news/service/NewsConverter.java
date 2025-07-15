package com.example.news_service.news.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.example.news_service.news.domain.News;
import com.example.news_service.news.dto.NewsListInfo;
import com.example.news_service.news.dto.NewsReadInfo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NewsConverter {

	/**
	 * News 엔티티를 NewsReadResponse로 변환
	 */
	public NewsReadInfo.NewsReadResponse createReadResponse(News news) {
		// content 필드에 이미 AI 요약본이 저장되어 있음
		String summary = news.getContent() != null ? news.getContent() : "요약 정보가 없습니다.";
		
		return NewsReadInfo.NewsReadResponse.builder()
			.id(news.getId())
			.title(news.getTitle())
			.content(news.getContent())
			.summary(summary)
			.originalUrl(news.getOriginalUrl())
			.source(news.getSource())
			.category(news.getCategory().name())
			.categoryName(news.getCategory().getName())
			.status(news.getStatus().name())
			.imageUrl(news.getImageUrl())
			.publishedAt(news.getPublishedAt())
			.createdAt(news.getCreatedAt())
			.updatedAt(news.getUpdatedAt())
			.build();
	}

	/**
	 * News 엔티티를 NewsItem으로 변환
	 */
	public NewsListInfo.NewsItem createNewsItem(News news) {
		// content 필드에 이미 AI 요약본이 저장되어 있음
		String summary = news.getContent() != null ? news.getContent() : "요약 정보가 없습니다.";
		
		return NewsListInfo.NewsItem.builder()
			.id(news.getId())
			.title(news.getTitle())
			.summary(summary)
			.originalUrl(news.getOriginalUrl())
			.source(news.getSource())
			.category(news.getCategory().name())
			.categoryName(news.getCategory().getName())
			.imageUrl(news.getImageUrl())
			.publishedAt(news.getPublishedAt())
			.build();
	}

	/**
	 * Page<News>를 NewsListResponse로 변환
	 */
	public NewsListInfo.NewsListResponse createListResponse(Page<News> newsPage) {
		return NewsListInfo.NewsListResponse.builder()
			.newsList(newsPage.getContent().stream()
				.map(this::createNewsItem)
				.toList())
			.currentPage(newsPage.getNumber())
			.totalPages(newsPage.getTotalPages())
			.totalElements(newsPage.getTotalElements())
			.hasNext(newsPage.hasNext())
			.hasPrevious(newsPage.hasPrevious())
			.build();
	}
} 