package com.example.news_service.news.domain;

public enum NewsCategory {
	POLITICS("100", "정치"),
	ECONOMY("101", "경제"),
	SOCIETY("102", "사회"),
	LIFE_CULTURE("103", "생활/문화"),
	WORLD("104", "세계"),
	IT_SCIENCE("105", "IT/과학"),
	ENTERTAINMENT("106", "연예"),
	SPORTS("107", "스포츠");

	private final String code;
	private final String name;

	NewsCategory(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public static NewsCategory fromCode(String code) {
		for (NewsCategory category : values()) {
			if (category.code.equals(code)) {
				return category;
			}
		}
		throw new IllegalArgumentException("Unknown category code: " + code);
	}
} 