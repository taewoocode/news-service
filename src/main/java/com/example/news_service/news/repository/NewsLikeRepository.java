package com.example.news_service.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.news_service.news.domain.NewsLike;

@Repository
public interface NewsLikeRepository extends JpaRepository<NewsLike, Long> {
}
