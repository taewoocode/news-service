package com.example.news_service.news.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NewsReadInfo {

	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class NewsReadRequest {
		private Long id;
	}

	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class NewsReadResponse {
		private Long id;
		private String title;
		private String content;
		private String summary;
		private String originalUrl;
		private String source;
		private String category;
		private String categoryName;
		private String status;
		private String imageUrl;
		private LocalDateTime publishedAt;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;
	}
} 