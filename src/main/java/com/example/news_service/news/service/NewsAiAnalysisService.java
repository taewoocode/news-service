package com.example.news_service.news.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.news_service.news.client.GptApiClient;
import com.example.news_service.news.domain.News;
import com.example.news_service.news.entity.NewsAiAnalysis;
import com.example.news_service.news.repository.NewsAiAnalysisRepository;
import com.example.news_service.news.repository.NewsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsAiAnalysisService {

	private final NewsAiAnalysisRepository newsAiAnalysisRepository;
	private final NewsRepository newsRepository;
	private final GptApiClient gptApiClient;

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
	 * 특정 뉴스의 최신 AI 분석 결과를 조회
	 */
	public Optional<NewsAiAnalysis> getLatestAnalysis(Long newsId) {
		Optional<News> news = newsRepository.findById(newsId);
		if (news.isEmpty()) {
			return Optional.empty();
		}
		return newsAiAnalysisRepository.findTopByNewsOrderByCreatedAtDesc(news.get());
	}

	/**
	 * 특정 뉴스의 모든 AI 분석 히스토리를 조회
	 */
	public List<NewsAiAnalysis> getAnalysisHistory(Long newsId) {
		Optional<News> news = newsRepository.findById(newsId);
		if (news.isEmpty()) {
			return List.of();
		}
		return newsAiAnalysisRepository.findByNewsOrderByCreatedAtDesc(news.get());
	}

	/**
	 * 특정 뉴스에 대해 새로운 AI 분석을 생성
	 */
	@Transactional
	public NewsAiAnalysis createNewAnalysis(Long newsId, String customPrompt) {
		Optional<News> newsOpt = newsRepository.findById(newsId);
		if (newsOpt.isEmpty()) {
			throw new IllegalArgumentException("뉴스를 찾을 수 없습니다: " + newsId);
		}

		News news = newsOpt.get();
		String content = news.getContent();
		
		if (content == null || content.isBlank()) {
			throw new IllegalStateException("뉴스 본문이 없습니다: " + newsId);
		}

		// 기본 프롬프트 또는 사용자 정의 프롬프트 사용
		String prompt;
		if (customPrompt != null && !customPrompt.isBlank()) {
			prompt = customPrompt + "\\n\\n" + content;
		} else {
			prompt = String.format(DEFAULT_AI_PROMPT_TEMPLATE, content);
		}

		String aiResponse = gptApiClient.summarizeText(prompt);

		NewsAiAnalysis aiAnalysis = NewsAiAnalysis.builder()
			.news(news)
			.aiRequest(prompt)
			.aiResponse(aiResponse)
			.build();

		return newsAiAnalysisRepository.save(aiAnalysis);
	}

	/**
	 * 특정 뉴스에 대해 기본 프롬프트로 새로운 AI 분석을 생성
	 */
	@Transactional
	public NewsAiAnalysis createDefaultAnalysis(Long newsId) {
		return createNewAnalysis(newsId, null);
	}
} 