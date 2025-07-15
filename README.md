# 📰 News Service

> **최신 뉴스를 크롤링하고, AI 분석을 통해 다양한 인사이트를 제공하는 뉴스 서비스**

---

## 📌 프로젝트 소개

- **News Service**는 다양한 뉴스 소스를 크롤링하여 최신 뉴스를 수집하고, AI 기반 분석을 통해 뉴스의 주요 키워드, 감정, 트렌드 등을 제공합니다.
- 사용자들은 뉴스 검색, 카테고리별 뉴스 조회, 뉴스 좋아요, AI 분석 결과 확인 등 다양한 기능을 이용할 수 있습니다.

---

## 🚀 주요 기능

- **뉴스 크롤링**: 네이버 등 다양한 소스에서 실시간 뉴스 수집
- **AI 뉴스 분석**: GPT API를 활용한 뉴스 요약, 감정 분석, 키워드 추출
- **카테고리별 뉴스 제공**: 정치, 경제, 사회 등 다양한 카테고리 지원
- **뉴스 좋아요/통계**: 사용자별 뉴스 좋아요 및 통계 제공
- **RESTful API**: 프론트엔드/외부 서비스 연동을 위한 표준 API 제공

---

## 🛠️ 기술 스택

| 분류         | 기술명                       |
| ------------ | --------------------------- |
| Language     | Java 17                     |
| Framework    | Spring Boot 3.x             |
| DB           | (예: MySQL, H2 등)          |
| ORM          | JPA, QueryDSL               |
| AI           | OpenAI GPT API              |
| Build Tool   | Gradle                      |
| 기타         | Swagger, Spring Security 등  |

---

## 📂 프로젝트 구조

```
src/
  main/
    java/
      com.example.news_service/
        config/         # 환경설정 (Security, Swagger, Web 등)
        news/           # 뉴스 도메인 (controller, service, repository, entity 등)
        user/           # 사용자 도메인
    resources/
      application.yml   # 환경설정 파일
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

- `src/main/resources/application.yml` 파일을 환경에 맞게 수정

### 2. 빌드 및 실행

```bash
# Gradle 빌드
./gradlew build

# 애플리케이션 실행
./gradlew bootRun
```

### 3. API 문서 확인

- Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 🖥️ 주요 화면 (스크린샷)

| 로딩화면 | 로그인화면 | 메인화면 | 요약화면 |
| :------: | :--------: | :------: | :------: |
| ![로딩화면](https://github.com/user-attachments/assets/4b4d8858-7ccd-41a4-9831-573c6d986f17) | ![로그인화면](https://github.com/user-attachments/assets/e91f0e3d-46e3-44c3-87cf-1e309163783e) | ![메인화면](https://github.com/user-attachments/assets/587b0b8e-a535-4221-96d4-925c7e290f3c) | ![요약화면](https://github.com/user-attachments/assets/e723a4c0-affa-45ff-8232-96bda73e1162) |

---

## 🧑‍💻 기여 방법

1. 이슈 등록 및 포크(Fork)
2. 새로운 브랜치 생성 (`feature/이슈번호-설명`)
3. 기능 개발 및 테스트
4. PR(Pull Request) 생성

---

## 📄 라이선스

- MIT License

---

## 🙏 참고/문의

- 궁금한 점이나 제안사항은 [Issues](https://github.com/your-repo/issues) 또는 이메일로 문의해주세요. 