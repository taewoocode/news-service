package com.example.news_service.news.client;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NewsScraper {

	private static final String[] CONTENT_SELECTORS = {
		"article#dic_area",             // 네이버 뉴스 (일반)
		"div#newsct_article",           // 네이버 뉴스 (스포츠)
		"div.news_end",                 // 네이버 뉴스 (연예)
		"div#article-view-content-div", // 기타 언론사 (traveltimes, ksilbo 등)
		"div.article_body"              // 매일경제
	};

	private static final String[] IMAGE_SELECTORS = {
		"meta[property='og:image']",           // Open Graph 이미지
		"meta[name='twitter:image']",          // Twitter 이미지
		"meta[property='og:image:secure_url']", // Open Graph 보안 이미지
		"img.thumb",                           // 썸네일 이미지
		"img.article_img",                     // 기사 이미지
		"img[width='600']",                    // 특정 크기 이미지
		"img[width='500']",                    // 특정 크기 이미지
		"img[width='400']",                    // 특정 크기 이미지
		"img[width='300']",                    // 특정 크기 이미지
		"img[width='200']",                    // 특정 크기 이미지
		"img[width='100']",                    // 특정 크기 이미지
		"img"                                  // 모든 이미지 (마지막 선택자)
	};

	public String scrapeNewsContent(String url) {
		try {
			log.info("뉴스 스크래핑 시작: {}", url);
			Document doc = Jsoup.connect(url)
								.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
								.get();

			for (String selector : CONTENT_SELECTORS) {
				Elements articleBody = doc.select(selector);
				if (!articleBody.isEmpty() && !articleBody.text().isEmpty()) {
					String content = articleBody.text();
					log.info("뉴스 스크래핑 성공 (선택자: {}). 내용 길이: {}", selector, content.length());
					return content;
				}
			}

			log.warn("모든 선택자로 본문 스크래핑 실패: {}", url);
			return ""; // 또는 null

		} catch (IOException e) {
			log.error("뉴스 스크래핑 중 오류 발생: {}", url, e);
			return null;
		}
	}

	/**
	 * 뉴스 기사에서 대표 이미지 URL을 추출합니다.
	 */
	public String extractImageUrl(String url) {
		try {
			log.info("이미지 URL 추출 시작: {}", url);
			Document doc = Jsoup.connect(url)
								.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
								.get();

			// 메타 태그에서 이미지 URL 추출 (우선순위 높음)
			for (String selector : IMAGE_SELECTORS) {
				Elements elements = doc.select(selector);
				for (Element element : elements) {
					String imageUrl = null;
					
					if (selector.startsWith("meta")) {
						// 메타 태그의 경우 content 속성에서 URL 추출
						imageUrl = element.attr("content");
					} else {
						// img 태그의 경우 src 속성에서 URL 추출
						imageUrl = element.attr("src");
					}
					
					if (isValidImageUrl(imageUrl)) {
						// 상대 URL인 경우 절대 URL로 변환
						if (imageUrl.startsWith("//")) {
							imageUrl = "https:" + imageUrl;
						} else if (imageUrl.startsWith("/")) {
							// 도메인 추출
							String domain = extractDomain(url);
							imageUrl = domain + imageUrl;
						}
						
						log.info("이미지 URL 추출 성공: {}", imageUrl);
						return imageUrl;
					}
				}
			}

			log.warn("이미지 URL 추출 실패: {}", url);
			return null;

		} catch (IOException e) {
			log.error("이미지 URL 추출 중 오류 발생: {}", url, e);
			return null;
		}
	}

	/**
	 * 이미지 URL이 유효한지 확인합니다.
	 */
	private boolean isValidImageUrl(String imageUrl) {
		if (imageUrl == null || imageUrl.trim().isEmpty()) {
			return false;
		}
		
		// 일반적인 이미지 확장자 확인
		String lowerUrl = imageUrl.toLowerCase();
		return lowerUrl.contains(".jpg") || lowerUrl.contains(".jpeg") || 
			   lowerUrl.contains(".png") || lowerUrl.contains(".gif") || 
			   lowerUrl.contains(".webp") || lowerUrl.contains(".svg") ||
			   lowerUrl.contains("image") || lowerUrl.contains("photo") ||
			   lowerUrl.contains("img");
	}

	/**
	 * URL에서 도메인을 추출합니다.
	 */
	private String extractDomain(String url) {
		try {
			java.net.URL urlObj = new java.net.URL(url);
			return urlObj.getProtocol() + "://" + urlObj.getHost();
		} catch (Exception e) {
			log.warn("도메인 추출 실패: {}", url);
			return "";
		}
	}
} 