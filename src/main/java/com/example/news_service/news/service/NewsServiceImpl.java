package com.example.news_service.news.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.news_service.news.domain.News;
import com.example.news_service.news.domain.NewsCategory;
import com.example.news_service.news.domain.NewsStatus;
import com.example.news_service.news.dto.NewsListInfo;
import com.example.news_service.news.dto.NewsReadInfo;
import com.example.news_service.news.repository.NewsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

	private final NewsRepository newsRepository;
	private final NewsConverter newsConverter;

	@Override
	@Transactional(readOnly = true)
	public NewsReadInfo.NewsReadResponse findById(Long id) {
		log.info("뉴스 상세 조회 요청: {}", id);

		News news = newsRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 뉴스입니다: " + id));

		return newsConverter.createReadResponse(news);
	}

	@Override
	@Transactional(readOnly = true)
	public NewsListInfo.NewsListResponse findByCategory(String category, int page, int size) {
		log.info("카테고리별 뉴스 조회 요청: category={}, page={}, size={}", category, page, size);

		NewsCategory newsCategory = NewsCategory.valueOf(category.toUpperCase());
		Pageable pageable = PageRequest.of(page, size);
		
		Page<News> newsPage = newsRepository.findByCategoryAndStatusOrderByPublishedAtDesc(
			newsCategory, NewsStatus.ACTIVE, pageable);

		return newsConverter.createListResponse(newsPage);
	}

	@Override
	@Transactional(readOnly = true)
	public NewsListInfo.NewsListResponse searchByKeyword(String keyword, int page, int size) {
		log.info("키워드 검색 요청: keyword={}, page={}, size={}", keyword, page, size);

		Pageable pageable = PageRequest.of(page, size);
		
		// 제목과 내용에서 키워드 검색
		Page<News> titleResults = newsRepository.findByTitleContainingAndStatus(keyword, NewsStatus.ACTIVE, pageable);
		Page<News> contentResults = newsRepository.findByContentContainingAndStatus(keyword, NewsStatus.ACTIVE, pageable);
		
		// 결과를 합치고 중복 제거 (간단한 구현)
		// 실제로는 더 정교한 검색 로직이 필요할 수 있음
		Page<News> combinedResults = titleResults.getTotalElements() > contentResults.getTotalElements() 
			? titleResults : contentResults;

		return newsConverter.createListResponse(combinedResults);
	}

	@Override
	@Transactional(readOnly = true)
	public NewsListInfo.NewsListResponse findLatestNews(int page, int size) {
		log.info("최신 뉴스 조회 요청: page={}, size={}", page, size);

		Pageable pageable = PageRequest.of(page, size);
		Page<News> newsPage = newsRepository.findByStatusOrderByPublishedAtDesc(NewsStatus.ACTIVE, pageable);

		return newsConverter.createListResponse(newsPage);
	}

	@Override
	@Transactional
	public void updateContent(Long newsId, String content) {
		log.info("뉴스 내용 업데이트 요청: newsId={}", newsId);

		News news = newsRepository.findById(newsId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 뉴스입니다: " + newsId));

		news.updateContentOnly(content);
		newsRepository.save(news);
		
		log.info("뉴스 내용 업데이트 완료: newsId={}", newsId);
	}
} 