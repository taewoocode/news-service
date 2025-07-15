# 📰 News Service

> 최신 뉴스를 크롤링하고, AI 분석을 통해 다양한 인사이트를 제공하는 뉴스 서비스

---

## 📌 프로젝트 소개

- News Service는 네이버 등 다양한 뉴스 소스에서 뉴스를 수집하고, OpenAI GPT를 활용해 요약·분석 결과를 제공합니다.
- 사용자 회원가입, 로그인, 뉴스 좋아요, AI 요약 등 다양한 기능을 제공합니다.

---

## 🚀 주요 기능

- 네이버 뉴스 API 연동: 실시간 뉴스 데이터 수집
- 뉴스 본문 스크래핑: Jsoup 기반 뉴스 본문/이미지 추출
- AI 뉴스 요약/분석: OpenAI GPT API 연동
- 카테고리별 뉴스 제공: 정치, 경제, 사회 등
- 회원 관리: 회원가입, 로그인, 프로필 관리
- 뉴스 좋아요/통계: 사용자별 좋아요, 통계 제공
- RESTful API: 프론트엔드/외부 서비스 연동
- Swagger 문서화: API 명세 자동화

---

## 🛠️ 기술 스택

| 분류          | 기술명/라이브러리                        | 사용 목적/위치 예시     |
|-------------|----------------------------------|-----------------|
| Language    | Java 21                          | 전체 서비스          |
| Framework   | Spring Boot 3.5.3                | 전체 서비스          |
| DB          | MySQL                            | 데이터 저장          |
| ORM         | JPA, QueryDSL                    | DB 연동, 동적 쿼리    |
| AI          | OpenAI GPT API                   | 뉴스 요약/분석        |
| 크롤링         | Jsoup                            | 뉴스 본문/이미지 추출    |
| 외부 API      | 네이버 뉴스 OpenAPI                   | 뉴스 데이터 수집       |
| REST Client | RestTemplate                     | 외부 API 호출       |
| 보안          | Spring Security, BCrypt          | 인증/인가, 비밀번호 암호화 |
| 문서화         | Swagger (springdoc-openapi)      | API 문서 자동화      |
| 빌드          | Gradle 8.14.2                    | 프로젝트 빌드         |
| 테스트         | JUnit, Spring Boot Test, Reactor | 단위/통합 테스트       |
| 기타          | Lombok, Devtools                 | 코드 간결화, 개발 편의   |

---

## 📂 프로젝트 구조

```
src/
  main/
    java/
      com.example.news_service/
        config/         # 환경설정 (보안, Swagger, Web)
        news/           # 뉴스 도메인 (client, controller, service, entity)
        user/           # 사용자 도메인 (controller, service, entity)
    resources/
      application.yml   # 환경설정 파일 (DB, 외부 API 키)
      static/
      templates/
  test/
    java/
      com.example.news_service/
        ...             # 테스트 코드
```

---

## ⚡️ 빠른 시작

### 1. 환경 변수 설정

- `src/main/resources/application.yml`에서 DB, 네이버/오픈AI API 키 등 환경에 맞게 수정

### 2. 빌드 및 실행

```bash
./gradlew build
./gradlew bootRun
```

---

## 🖥️ 주요 화면 (스크린샷)

|                                            로딩화면                                            |                                           로그인화면                                           |                                           메인화면                                           |                                           요약화면                                           |
|:------------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------:|
| ![로딩화면17](https://github.com/user-attachments/assets/4b4d8858-7ccd-41a4-9831-573c6d986f17) | ![로그인화면](https://github.com/user-attachments/assets/e91f0e3d-46e3-44c3-87cf-1e309163783e) | ![메인화면](https://github.com/user-attachments/assets/587b0b8e-a535-4221-96d4-925c7e290f3c) | ![요약화면](https://github.com/user-attachments/assets/e723a4c0-affa-45ff-8232-96bda73e1162) |

---

## 🧩 주요 연동/구현 기술 상세

- 네이버 뉴스 API: `NaverNewsClient`에서 RestTemplate으로 연동, `application.yml`에 키 관리
- OpenAI GPT API: `GptApiClient`에서 요약/분석, `GptConfig`에서 서비스 Bean 등록
- Jsoup: `NewsScraper`에서 뉴스 본문/이미지 추출
- Spring Security: `SecurityConfig`에서 인증/인가, BCrypt로 비밀번호 암호화
- Swagger: `SwaggerConfig`에서 API 문서 자동화, JWT 인증 연동
- JPA/QueryDSL: Repository, 동적 쿼리, 엔티티 관리
- 스케줄링: `@EnableScheduling`, `NewsCrawlingService`에서 뉴스 자동 수집
- 테스트: `NewsServiceApplicationTests` 등에서 JUnit 기반 테스트

---

## 🧑‍💻 기여 방법

1. 이슈 등록 및 포크(Fork)
2. 새로운 브랜치 생성 (`feature/이슈번호-설명`)
3. 기능 개발 및 테스트
4. PR(Pull Request) 생성

---