package com.example.news_service.news.repository;

import com.example.news_service.news.entity.NewsAiAnalysis;
import com.example.news_service.news.domain.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsAiAnalysisRepository extends JpaRepository<NewsAiAnalysis, Long> {
    
    /**
     * 특정 뉴스의 가장 최근 AI 분석 결과를 조회
     */
    Optional<NewsAiAnalysis> findTopByNewsOrderByCreatedAtDesc(News news);
    
    /**
     * 특정 뉴스의 모든 AI 분석 히스토리를 조회 (최신순)
     */
    List<NewsAiAnalysis> findByNewsOrderByCreatedAtDesc(News news);
    
    /**
     * 특정 뉴스의 AI 분석 결과가 존재하는지 확인
     */
    boolean existsByNews(News news);
} 