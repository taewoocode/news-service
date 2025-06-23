package com.example.news_service.news.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "news")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class News {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(columnDefinition = "TEXT")
	private String content;

	@Column(nullable = false)
	private String originalUrl;

	@Column(nullable = false)
	private String source;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private NewsCategory category;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private NewsStatus status;

	private String imageUrl;

	@Column(nullable = false)
	private LocalDateTime publishedAt;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@Builder
	public News(String title, String content, String originalUrl, String source, 
				NewsCategory category, NewsStatus status, String imageUrl, LocalDateTime publishedAt) {
		this.title = title;
		this.content = content;
		this.originalUrl = originalUrl;
		this.source = source;
		this.category = category;
		this.status = status;
		this.imageUrl = imageUrl;
		this.publishedAt = publishedAt;
	}

	public void updateStatus(NewsStatus status) {
		this.status = status;
	}

	public void updateContent(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public void updateContentOnly(String content) {
		this.content = content;
	}
} 