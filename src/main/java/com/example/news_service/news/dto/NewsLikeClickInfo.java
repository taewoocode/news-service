package com.example.news_service.news.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NewsLikeClickInfo {

	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class NewsLikeClickResponse {
		private boolean success;
		private String message;
		private Long newsId;
		private Long userId;
		private boolean isLiked;
		private long likeCount;
	}

	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class NewsLikeClickRequest {
		private Long newsId;
		private Long userId;

	}
}
