package com.example.news_service.news.client;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.news_service.news.domain.News;
import com.example.news_service.news.domain.NewsCategory;
import com.example.news_service.news.domain.NewsStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class NaverNewsClient {

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	@Value("${naver.news.client-id}")
	private String clientId;

	@Value("${naver.news.client-secret}")
	private String clientSecret;

	private static final String NAVER_NEWS_API_URL = "https://openapi.naver.com/v1/search/news.json";

	/**
	 * 네이버 뉴스 API에서 뉴스를 가져옵니다.
	 */
	public List<News> fetchNewsFromNaver(NewsCategory category, int display) {
		try {
			// 카테고리명 대신 더 일반적인 검색어 사용
			String query = getSearchQueryForCategory(category);

			// UriComponentsBuilder를 사용하여 안전한 URL 생성
			URI uri = UriComponentsBuilder
				.fromHttpUrl(NAVER_NEWS_API_URL)
				.queryParam("query", query)
				.queryParam("display", display)
				.queryParam("sort", "date")
				.build()
				.encode()
				.toUri();

			log.info("네이버 API 호출 URL: {}", uri);
			log.info("검색 쿼리: {}", query);
			log.info("Client ID: {}", clientId);
			log.info("Client Secret: {}", clientSecret);

			HttpHeaders headers = new HttpHeaders();
			headers.set("X-Naver-Client-Id", clientId);
			headers.set("X-Naver-Client-Secret", clientSecret);
			headers.set("Content-Type", "application/json");
			headers.set("Accept", "application/json");

			HttpEntity<String> entity = new HttpEntity<>(headers);

			log.info("API 호출 시작...");
			ResponseEntity<String> response = restTemplate.exchange(
				uri, HttpMethod.GET, entity, String.class);

			log.info("API 응답 상태: {}", response.getStatusCode());
			log.info("API 응답 헤더: {}", response.getHeaders());

			if (response.getStatusCode().is2xxSuccessful()) {
				return parseNewsResponse(response.getBody(), category);
			} else {
				log.error("API 호출 실패: {}", response.getStatusCode());
				log.error("응답 본문: {}", response.getBody());
				return new ArrayList<>();
			}

		} catch (Exception e) {
			log.error("네이버 뉴스 API 호출 중 오류 발생: {}", e.getMessage(), e);
			return new ArrayList<>();
		}
	}

	/**
	 * 카테고리에 따른 검색 쿼리를 반환합니다.
	 */
	private String getSearchQueryForCategory(NewsCategory category) {
		switch (category) {
			case POLITICS:
				return "정치";
			case ECONOMY:
				return "경제";
			case SOCIETY:
				return "사회";
			case LIFE_CULTURE:
				return "생활문화";
			case WORLD:
				return "세계";
			case IT_SCIENCE:
				return "IT과학";
			case ENTERTAINMENT:
				return "연예";
			case SPORTS:
				return "스포츠";
			default:
				return "뉴스";
		}
	}

	/**
	 * 네이버 뉴스 API 응답을 파싱합니다.
	 */
	private List<News> parseNewsResponse(String responseBody, NewsCategory category) {
		List<News> newsList = new ArrayList<>();

		try {
			log.info("응답 본문: {}", responseBody);

			JsonNode rootNode = objectMapper.readTree(responseBody);
			JsonNode itemsNode = rootNode.get("items");

			if (itemsNode != null && itemsNode.isArray()) {
				log.info("뉴스 아이템 개수: {}", itemsNode.size());

				for (JsonNode itemNode : itemsNode) {
					News news = parseNewsItem(itemNode, category);
					if (news != null) {
						newsList.add(news);
					}
				}
			} else {
				log.warn("뉴스 아이템을 찾을 수 없습니다.");
			}

		} catch (IOException e) {
			log.error("뉴스 응답 파싱 중 오류 발생: {}", e.getMessage(), e);
		}

		return newsList;
	}

	/**
	 * 개별 뉴스 아이템을 파싱합니다.
	 */
	private News parseNewsItem(JsonNode itemNode, NewsCategory category) {
		try {
			String title = cleanHtmlTags(itemNode.get("title").asText());
			String description = cleanHtmlTags(itemNode.get("description").asText());
			String link = itemNode.get("link").asText();
			String originallink = itemNode.has("originallink") ? itemNode.get("originallink").asText() : link;
			String pubDate = itemNode.get("pubDate").asText();

			// 출처 추출 (title에서 <b> 태그 사이의 내용)
			String source = extractSource(title);

			// HTML 태그 제거
			title = removeHtmlTags(title);
			description = removeHtmlTags(description);

			// 발행일 파싱
			LocalDateTime publishedAt = parsePubDate(pubDate);

			return News.builder()
				.title(title)
				.originalUrl(originallink)
				.source(source)
				.category(category)
				.status(NewsStatus.ACTIVE)
				.publishedAt(publishedAt)
				.build();

		} catch (Exception e) {
			log.error("뉴스 아이템 파싱 중 오류 발생: {}", e.getMessage(), e);
			return null;
		}
	}

	/**
	 * HTML 태그를 제거
	 */
	private String cleanHtmlTags(String text) {
		if (text == null)
			return "";
		return text.replaceAll("<[^>]*>", "");
	}

	/**
	 * HTML 태그를 제거
	 */
	private String removeHtmlTags(String text) {
		if (text == null)
			return "";
		return text.replaceAll("<[^>]*>", "").trim();
	}

	/**
	 * 출처를 추출
	 */
	private String extractSource(String title) {
		try {
			// <b>태그 사이의 내용이 출처인 경우가 많음
			int startIndex = title.indexOf("<b>");
			int endIndex = title.indexOf("</b>");

			if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
				return title.substring(startIndex + 3, endIndex).trim();
			}

			// 다른 패턴으로 출처 추출 시도
			if (title.contains("|")) {
				String[] parts = title.split("\\|");
				if (parts.length > 1) {
					return parts[0].trim();
				}
			}

			return "네이버뉴스";
		} catch (Exception e) {
			return "네이버뉴스";
		}
	}

	/**
	 * 발행일을 파싱
	 */
	private LocalDateTime parsePubDate(String pubDate) {
		try {
			// RFC 822 형식: "Wed, 22 Jun 2025 14:30:00 +0900"
			DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
			return LocalDateTime.parse(pubDate, formatter);
		} catch (Exception e) {
			log.warn("발행일 파싱 실패: {}, 현재 시간 사용", pubDate);
			return LocalDateTime.now();
		}
	}
} 