package com.example.news_service.news.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NewsListInfo {

	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class NewsListRequest {
		private String category;
		private Integer page;
		private Integer size;
		private String keyword;
	}

	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class NewsListResponse {
		private List<NewsItem> newsList;
		private int currentPage;
		private int totalPages;
		private long totalElements;
		private boolean hasNext;
		private boolean hasPrevious;
	}

	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class NewsItem {
		private Long id;
		private String title;
		private String summary;
		private String originalUrl;
		private String source;
		private String category;
		private String categoryName;
		private String imageUrl;
		private LocalDateTime publishedAt;
	}
} 