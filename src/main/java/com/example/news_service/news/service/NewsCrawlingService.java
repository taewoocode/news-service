package com.example.news_service.news.service;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.news_service.news.client.GptApiClient;
import com.example.news_service.news.client.NaverNewsClient;
import com.example.news_service.news.client.NewsScraper;
import com.example.news_service.news.domain.News;
import com.example.news_service.news.domain.NewsCategory;
import com.example.news_service.news.entity.NewsAiAnalysis;
import com.example.news_service.news.repository.NewsAiAnalysisRepository;
import com.example.news_service.news.repository.NewsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsCrawlingService {

	private final NaverNewsClient naverNewsClient;
	private final NewsRepository newsRepository;
	private final NewsScraper newsScraper;
	private final GptApiClient gptApiClient;
	private final NewsAiAnalysisRepository newsAiAnalysisRepository;

	private static final String DEFAULT_AI_PROMPT_TEMPLATE = """
		You are a friendly and witty news editor, like 'Newneek'. Your task is to summarize the following news article.
		- Start with a catchy, questioning title.
		- Use markdown for formatting.
		- Use bullet points (•) and bold text to break down the key events or points chronologically or by importance.
		- The tone should be easy to understand, engaging, and conversational for a young adult audience.
		- Explain the background and why this news is important.
		- Ensure the output is in Korean.

		---
		[News Article]
		%s
		""";

	/**
	 * 특정 카테고리의 뉴스를 크롤링하고 저장합니다.
	 */
	@Transactional
	public void crawlAndSaveNews(NewsCategory category, int display) {
		log.info("{} 카테고리 뉴스 크롤링 시작", category.getName());

		try {
			List<News> newsList = naverNewsClient.fetchNewsFromNaver(category, display);

			int savedCount = 0;
			for (News news : newsList) {
				if (!newsRepository.existsByOriginalUrl(news.getOriginalUrl())) {

					// 뉴스 본문 스크래핑
					String originalContent = newsScraper.scrapeNewsContent(news.getOriginalUrl());
					if (originalContent == null || originalContent.isBlank()) {
						log.warn("뉴스 본문을 스크래핑할 수 없습니다: {}", news.getOriginalUrl());
						continue; // 본문이 없으면 저장하지 않고 다음 뉴스로
					}

					String imageUrl = newsScraper.extractImageUrl(news.getOriginalUrl());
					if (imageUrl != null && !imageUrl.isBlank()) {
						news.setImageUrl(imageUrl);
						log.info("이미지 URL 설정: {}", imageUrl);
					} else {
						log.warn("이미지 URL을 추출할 수 없습니다: {}", news.getOriginalUrl());
					}

					String aiRequest = String.format(DEFAULT_AI_PROMPT_TEMPLATE, originalContent);
					String aiResponse = gptApiClient.summarizeText(aiRequest);

					news.setContent(aiResponse);

					News savedNews = newsRepository.save(news);

					NewsAiAnalysis aiAnalysis = NewsAiAnalysis.builder()
						.news(savedNews)
						.aiRequest(aiRequest)
						.aiResponse(aiResponse)
						.build();

					newsAiAnalysisRepository.save(aiAnalysis);
					savedCount++;
				}
			}

			log.info("{} 카테고리 뉴스 크롤링 완료: {}개 저장됨", category.getName(), savedCount);

		} catch (Exception e) {
			log.error("{} 카테고리 뉴스 크롤링 중 오류 발생: {}", category.getName(), e.getMessage(), e);
		}
	}

	/**
	 * 모든 카테고리의 뉴스를 크롤링
	 */
	@Transactional
	public void crawlAllCategories() {
		log.info("전체 카테고리 뉴스 크롤링 시작");

		for (NewsCategory category : NewsCategory.values()) {
			crawlAndSaveNews(category, 10); // 각 카테고리당 10개씩

			// API 호출 간격 조절 (네이버 API 제한 고려)
			try {
				Thread.sleep(1000); // 1초 대기
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}

		log.info("전체 카테고리 뉴스 크롤링 완료");
	}

	/**
	 * 매일 자정에 뉴스를 크롤링
	 */
	@Scheduled(cron = "0 0 0 * * ?") // 매일 자정
	public void scheduledNewsCrawling() {
		log.info("스케줄된 뉴스 크롤링 시작");
		crawlAllCategories();
	}

	/**
	 * 수동으로 뉴스 크롤링
	 */
	public void manualCrawl() {
		log.info("수동 뉴스 크롤링 시작");
		crawlAllCategories();
	}
} 