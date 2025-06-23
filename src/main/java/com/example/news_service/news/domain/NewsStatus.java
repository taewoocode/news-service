package com.example.news_service.news.domain;

public enum NewsStatus {
	ACTIVE("활성"),
	INACTIVE("비활성"),
	DELETED("삭제됨");

	private final String description;

	NewsStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
} 