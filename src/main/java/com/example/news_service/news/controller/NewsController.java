package com.example.news_service.news.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.news_service.news.dto.NewsListInfo;
import com.example.news_service.news.dto.NewsReadInfo;
import com.example.news_service.news.service.NewsCrawlingService;
import com.example.news_service.news.service.NewsService;
import com.example.news_service.news.service.TestNewsService;
import com.example.news_service.news.service.NewsAiAnalysisService;
import com.example.news_service.news.entity.NewsAiAnalysis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
@Tag(name = "News", description = "뉴스 관련 API")
public class NewsController {

	private final NewsService newsService;
	private final NewsCrawlingService newsCrawlingService;
	private final TestNewsService testNewsService;
	private final NewsAiAnalysisService newsAiAnalysisService;

	@GetMapping("/{id}")
	@Operation(summary = "뉴스 상세 조회", description = "ID로 특정 뉴스의 상세 정보를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "뉴스 조회 성공",
			content = @Content(schema = @Schema(implementation = NewsReadInfo.NewsReadResponse.class))),
		@ApiResponse(responseCode = "404", description = "뉴스를 찾을 수 없음"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	public ResponseEntity<NewsReadInfo.NewsReadResponse> getNewsById(
		@Parameter(description = "뉴스 ID", example = "1") @PathVariable Long id) {
		log.info("뉴스 상세 조회 API 호출: {}", id);
		
		try {
			NewsReadInfo.NewsReadResponse response = newsService.findById(id);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			log.warn("뉴스 조회 실패: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("뉴스 조회 중 오류 발생: {}", e.getMessage(), e);
			throw e;
		}
	}

	@GetMapping("/category/{category}")
	@Operation(summary = "카테고리별 뉴스 목록", description = "특정 카테고리의 뉴스 목록을 페이지네이션과 함께 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "뉴스 목록 조회 성공",
			content = @Content(schema = @Schema(implementation = NewsListInfo.NewsListResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 카테고리"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	public ResponseEntity<NewsListInfo.NewsListResponse> getNewsByCategory(
		@Parameter(description = "뉴스 카테고리", example = "POLITICS", 
			schema = @Schema(allowableValues = {"POLITICS", "ECONOMY", "SOCIETY", "LIFE", "WORLD", "TECH", "SPORTS", "ENTERTAINMENT"})) 
		@PathVariable String category,
		@Parameter(description = "페이지 번호 (0부터 시작)", example = "0") 
		@RequestParam(defaultValue = "0") int page,
		@Parameter(description = "페이지당 뉴스 개수", example = "10") 
		@RequestParam(defaultValue = "10") int size) {
		
		log.info("카테고리별 뉴스 목록 API 호출: category={}, page={}, size={}", category, page, size);
		
		try {
			NewsListInfo.NewsListResponse response = newsService.findByCategory(category, page, size);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			log.warn("카테고리별 뉴스 조회 실패: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("카테고리별 뉴스 조회 중 오류 발생: {}", e.getMessage(), e);
			throw e;
		}
	}

	@GetMapping("/search")
	@Operation(summary = "뉴스 검색", description = "키워드로 뉴스를 검색하고 페이지네이션과 함께 결과를 반환합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "뉴스 검색 성공",
			content = @Content(schema = @Schema(implementation = NewsListInfo.NewsListResponse.class))),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	public ResponseEntity<NewsListInfo.NewsListResponse> searchNews(
		@Parameter(description = "검색 키워드", example = "정치") 
		@RequestParam String keyword,
		@Parameter(description = "페이지 번호 (0부터 시작)", example = "0") 
		@RequestParam(defaultValue = "0") int page,
		@Parameter(description = "페이지당 뉴스 개수", example = "10") 
		@RequestParam(defaultValue = "10") int size) {
		
		log.info("뉴스 검색 API 호출: keyword={}, page={}, size={}", keyword, page, size);
		
		try {
			NewsListInfo.NewsListResponse response = newsService.searchByKeyword(keyword, page, size);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			log.error("뉴스 검색 중 오류 발생: {}", e.getMessage(), e);
			throw e;
		}
	}

	@GetMapping("/latest")
	@Operation(summary = "최신 뉴스 목록", description = "최신 뉴스 목록을 페이지네이션과 함께 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "최신 뉴스 조회 성공",
			content = @Content(schema = @Schema(implementation = NewsListInfo.NewsListResponse.class))),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	public ResponseEntity<NewsListInfo.NewsListResponse> getLatestNews(
		@Parameter(description = "페이지 번호 (0부터 시작)", example = "0") 
		@RequestParam(defaultValue = "0") int page,
		@Parameter(description = "페이지당 뉴스 개수", example = "10") 
		@RequestParam(defaultValue = "10") int size) {
		
		log.info("최신 뉴스 목록 API 호출: page={}, size={}", page, size);
		
		try {
			NewsListInfo.NewsListResponse response = newsService.findLatestNews(page, size);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			log.error("최신 뉴스 조회 중 오류 발생: {}", e.getMessage(), e);
			throw e;
		}
	}

	@RequestMapping(value = "/crawl", method = {RequestMethod.GET, RequestMethod.POST})
	@Operation(summary = "뉴스 크롤링", description = "모든 카테고리의 뉴스를 네이버 API에서 크롤링합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "뉴스 크롤링 완료"),
		@ApiResponse(responseCode = "500", description = "크롤링 중 오류 발생")
	})
	public ResponseEntity<String> crawlNews() {
		log.info("뉴스 크롤링 API 호출");
		try {
			newsCrawlingService.manualCrawl();
			return ResponseEntity.ok("뉴스 크롤링이 완료되었습니다.");
		} catch (Exception e) {
			log.error("뉴스 크롤링 중 오류 발생: {}", e.getMessage(), e);
			return ResponseEntity.internalServerError().body("뉴스 크롤링 중 오류가 발생했습니다: " + e.getMessage());
		}
	}

	@PostMapping("/crawl/{category}")
	@Operation(summary = "카테고리별 뉴스 크롤링", description = "특정 카테고리의 뉴스를 네이버 API에서 크롤링합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "카테고리별 뉴스 크롤링 완료"),
		@ApiResponse(responseCode = "400", description = "잘못된 카테고리"),
		@ApiResponse(responseCode = "500", description = "크롤링 중 오류 발생")
	})
	public ResponseEntity<String> crawlNewsByCategory(
		@Parameter(description = "뉴스 카테고리", example = "SPORTS",
			schema = @Schema(allowableValues = {"POLITICS", "ECONOMY", "SOCIETY", "LIFE", "WORLD", "TECH", "SPORTS", "ENTERTAINMENT"})) 
		@PathVariable String category) {
		log.info("카테고리별 뉴스 크롤링 API 호출: category={}", category);
		
		try {
			com.example.news_service.news.domain.NewsCategory newsCategory = 
				com.example.news_service.news.domain.NewsCategory.valueOf(category.toUpperCase());
			
			newsCrawlingService.crawlAndSaveNews(newsCategory, 10);
			return ResponseEntity.ok(category + " 카테고리 뉴스 크롤링이 완료되었습니다.");
		} catch (IllegalArgumentException e) {
			log.warn("잘못된 카테고리: {}", category);
			return ResponseEntity.badRequest().body("잘못된 카테고리입니다: " + category);
		} catch (Exception e) {
			log.error("카테고리별 뉴스 크롤링 중 오류 발생: {}", e.getMessage(), e);
			return ResponseEntity.internalServerError().body("뉴스 크롤링 중 오류가 발생했습니다: " + e.getMessage());
		}
	}

	@PostMapping("/test-data")
	@Operation(summary = "테스트 뉴스 데이터 생성", description = "테스트용 더미 뉴스 데이터를 생성합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "테스트 데이터 생성 완료"),
		@ApiResponse(responseCode = "500", description = "데이터 생성 중 오류 발생")
	})
	public ResponseEntity<String> generateTestData() {
		log.info("테스트 뉴스 데이터 생성 API 호출");
		
		try {
			testNewsService.generateTestNews();
			return ResponseEntity.ok("테스트 뉴스 데이터 생성이 완료되었습니다.");
		} catch (Exception e) {
			log.error("테스트 뉴스 데이터 생성 중 오류 발생: {}", e.getMessage(), e);
			return ResponseEntity.internalServerError().body("테스트 뉴스 데이터 생성 중 오류가 발생했습니다: " + e.getMessage());
		}
	}

	@GetMapping("/{newsId}/ai-analysis")
	@Operation(summary = "뉴스 AI 분석 결과 조회", description = "특정 뉴스의 최신 AI 분석 결과를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "AI 분석 결과 조회 성공"),
		@ApiResponse(responseCode = "404", description = "뉴스 또는 AI 분석 결과를 찾을 수 없음"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	public ResponseEntity<NewsAiAnalysis> getLatestAiAnalysis(
		@Parameter(description = "뉴스 ID", example = "1") @PathVariable Long newsId) {
		log.info("뉴스 AI 분석 결과 조회 API 호출: newsId={}", newsId);
		
		try {
			NewsAiAnalysis analysis = newsAiAnalysisService.getLatestAnalysis(newsId)
				.orElseThrow(() -> new IllegalArgumentException("AI 분석 결과를 찾을 수 없습니다: " + newsId));
			return ResponseEntity.ok(analysis);
		} catch (IllegalArgumentException e) {
			log.warn("AI 분석 결과 조회 실패: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("AI 분석 결과 조회 중 오류 발생: {}", e.getMessage(), e);
			throw e;
		}
	}

	@GetMapping("/{newsId}/ai-analysis/history")
	@Operation(summary = "뉴스 AI 분석 히스토리 조회", description = "특정 뉴스의 모든 AI 분석 히스토리를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "AI 분석 히스토리 조회 성공"),
		@ApiResponse(responseCode = "404", description = "뉴스를 찾을 수 없음"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	public ResponseEntity<java.util.List<NewsAiAnalysis>> getAiAnalysisHistory(
		@Parameter(description = "뉴스 ID", example = "1") @PathVariable Long newsId) {
		log.info("뉴스 AI 분석 히스토리 조회 API 호출: newsId={}", newsId);
		
		try {
			java.util.List<NewsAiAnalysis> history = newsAiAnalysisService.getAnalysisHistory(newsId);
			return ResponseEntity.ok(history);
		} catch (Exception e) {
			log.error("AI 분석 히스토리 조회 중 오류 발생: {}", e.getMessage(), e);
			throw e;
		}
	}

	@PostMapping("/{newsId}/ai-analysis")
	@Operation(summary = "새로운 AI 분석 생성", description = "특정 뉴스에 대해 새로운 AI 분석을 생성합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "AI 분석 생성 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "404", description = "뉴스를 찾을 수 없음"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	public ResponseEntity<NewsAiAnalysis> createNewAiAnalysis(
		@Parameter(description = "뉴스 ID", example = "1") @PathVariable Long newsId,
		@Parameter(description = "사용자 정의 프롬프트 (선택사항)", example = "이 뉴스를 전문가 관점에서 분석해줘") 
		@RequestParam(required = false) String customPrompt) {
		
		log.info("새로운 AI 분석 생성 API 호출: newsId={}, customPrompt={}", newsId, customPrompt);
		
		try {
			NewsAiAnalysis aiAnalysis;
			if (customPrompt != null && !customPrompt.isBlank()) {
				aiAnalysis = newsAiAnalysisService.createNewAnalysis(newsId, customPrompt);
			} else {
				aiAnalysis = newsAiAnalysisService.createDefaultAnalysis(newsId);
			}
			return ResponseEntity.ok(aiAnalysis);
		} catch (IllegalArgumentException e) {
			log.warn("AI 분석 생성 실패: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("AI 분석 생성 중 오류 발생: {}", e.getMessage(), e);
			throw e;
		}
	}

	@PostMapping("/{newsId}/update-content")
	@Operation(summary = "뉴스 내용 업데이트", description = "특정 뉴스의 content를 업데이트합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "뉴스 내용 업데이트 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "404", description = "뉴스를 찾을 수 없음"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	public ResponseEntity<String> updateNewsContent(
		@Parameter(description = "뉴스 ID", example = "1") @PathVariable Long newsId,
		@Parameter(description = "새로운 뉴스 내용") 
		@RequestParam String content) {
		
		log.info("뉴스 내용 업데이트 API 호출: newsId={}", newsId);
		
		try {
			newsService.updateContent(newsId, content);
			return ResponseEntity.ok("뉴스 내용이 성공적으로 업데이트되었습니다.");
		} catch (IllegalArgumentException e) {
			log.warn("뉴스 내용 업데이트 실패: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("뉴스 내용 업데이트 중 오류 발생: {}", e.getMessage(), e);
			throw e;
		}
	}
} 