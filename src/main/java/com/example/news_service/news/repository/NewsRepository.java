package com.example.news_service.news.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.news_service.news.domain.News;
import com.example.news_service.news.domain.NewsCategory;
import com.example.news_service.news.domain.NewsStatus;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
	
	/**
	 * 카테고리별 뉴스 조회 (활성 상태만)
	 */
	Page<News> findByCategoryAndStatusOrderByPublishedAtDesc(NewsCategory category, NewsStatus status, Pageable pageable);
	
	/**
	 * 카테고리별 뉴스 조회 (상태 무관)
	 */
	Page<News> findByCategoryOrderByPublishedAtDesc(NewsCategory category, Pageable pageable);
	
	/**
	 * 상태별 뉴스 조회
	 */
	Page<News> findByStatusOrderByPublishedAtDesc(NewsStatus status, Pageable pageable);
	
	/**
	 * 제목으로 뉴스 검색
	 */
	@Query("SELECT n FROM News n WHERE n.title LIKE %:keyword% AND n.status = :status ORDER BY n.publishedAt DESC")
	Page<News> findByTitleContainingAndStatus(@Param("keyword") String keyword, @Param("status") NewsStatus status, Pageable pageable);
	
	/**
	 * 내용으로 뉴스 검색
	 */
	@Query("SELECT n FROM News n WHERE n.content LIKE %:keyword% AND n.status = :status ORDER BY n.publishedAt DESC")
	Page<News> findByContentContainingAndStatus(@Param("keyword") String keyword, @Param("status") NewsStatus status, Pageable pageable);
	
	/**
	 * 출처별 뉴스 조회
	 */
	Page<News> findBySourceAndStatusOrderByPublishedAtDesc(String source, NewsStatus status, Pageable pageable);
	
	/**
	 * 특정 기간 내 뉴스 조회
	 */
	@Query("SELECT n FROM News n WHERE n.publishedAt BETWEEN :startDate AND :endDate AND n.status = :status ORDER BY n.publishedAt DESC")
	Page<News> findByPublishedAtBetweenAndStatus(@Param("startDate") java.time.LocalDateTime startDate, 
												@Param("endDate") java.time.LocalDateTime endDate, 
												@Param("status") NewsStatus status, 
												Pageable pageable);
	
	/**
	 * URL 중복 체크
	 */
	boolean existsByOriginalUrl(String originalUrl);
	
	/**
	 * 카테고리별 뉴스 개수 조회
	 */
	long countByCategoryAndStatus(NewsCategory category, NewsStatus status);
} 