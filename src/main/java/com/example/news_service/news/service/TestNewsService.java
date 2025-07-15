package com.example.news_service.news.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.news_service.news.domain.News;
import com.example.news_service.news.domain.NewsCategory;
import com.example.news_service.news.domain.NewsStatus;
import com.example.news_service.news.repository.NewsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestNewsService {

	private final NewsRepository newsRepository;
	private final Random random = new Random();

	/**
	 * 테스트용 더미 뉴스 데이터를 생성합니다.
	 */
	@Transactional
	public void generateTestNews() {
		log.info("테스트 뉴스 데이터 생성 시작");

		List<News> testNewsList = new ArrayList<>();

		// 각 카테고리별로 테스트 뉴스 생성
		for (NewsCategory category : NewsCategory.values()) {
			for (int i = 1; i <= 5; i++) {
				News news = createTestNews(category, i);
				testNewsList.add(news);
			}
		}

		// 중복 URL 체크 후 저장
		int savedCount = 0;
		for (News news : testNewsList) {
			if (!newsRepository.existsByOriginalUrl(news.getOriginalUrl())) {
				newsRepository.save(news);
				savedCount++;
			}
		}

		log.info("테스트 뉴스 데이터 생성 완료: {}개 저장됨", savedCount);
	}

	/**
	 * 특정 카테고리의 테스트 뉴스를 생성합니다.
	 */
	private News createTestNews(NewsCategory category, int index) {
		String[] titles = getTitlesForCategory(category);
		String[] contents = getContentsForCategory(category);
		String[] sources = getSourcesForCategory(category);
		String[] imageUrls = getImageUrlsForCategory(category);

		String title = titles[random.nextInt(titles.length)] + " - " + index;
		String content = contents[random.nextInt(contents.length)];
		String source = sources[random.nextInt(sources.length)];
		String imageUrl = imageUrls[random.nextInt(imageUrls.length)];

		return News.builder()
			.title(title)
			.content(content)
			.originalUrl("https://test-news.com/" + category.getCode() + "/" + index)
			.source(source)
			.category(category)
			.status(NewsStatus.ACTIVE)
			.imageUrl(imageUrl)
			.publishedAt(LocalDateTime.now().minusHours(random.nextInt(24)))
			.build();
	}

	/**
	 * 카테고리별 제목 배열을 반환합니다.
	 */
	private String[] getTitlesForCategory(NewsCategory category) {
		switch (category) {
			case POLITICS:
				return new String[]{
					"정치 개혁안 발표", "국회 의원 선거 결과", "정부 정책 변경 소식",
					"외교 관계 개선", "정치 스캔들 진실"
				};
			case ECONOMY:
				return new String[]{
					"주식 시장 급등락", "경제 성장률 발표", "부동산 정책 변경",
					"금리 인상 결정", "기업 실적 발표"
				};
			case SOCIETY:
				return new String[]{
					"사회 이슈 해결책", "교육 정책 개혁", "의료 서비스 개선",
					"교통 사고 예방", "환경 보호 운동"
				};
			case LIFE_CULTURE:
				return new String[]{
					"문화 행사 개최", "생활 정보 제공", "요리 레시피 소개",
					"여행 정보 안내", "건강 관리 팁"
				};
			case WORLD:
				return new String[]{
					"세계 경제 동향", "국제 관계 소식", "글로벌 이슈 분석",
					"해외 여행 정보", "세계 문화 소개"
				};
			case IT_SCIENCE:
				return new String[]{
					"새로운 기술 발표", "AI 발전 소식", "과학 연구 성과",
					"디지털 혁신", "스마트폰 신제품"
				};
			case ENTERTAINMENT:
				return new String[]{
					"연예인 결혼 소식", "영화 개봉 정보", "음악 차트 순위",
					"드라마 시청률", "예능 프로그램 소식"
				};
			case SPORTS:
				return new String[]{
					"축구 경기 결과", "야구 선수 이적", "올림픽 준비 소식",
					"골프 대회 결과", "스포츠 스타 인터뷰"
				};
			default:
				return new String[]{"일반 뉴스", "주요 소식", "핫 이슈"};
		}
	}

	/**
	 * 카테고리별 내용 배열을 반환합니다.
	 */
	private String[] getContentsForCategory(NewsCategory category) {
		switch (category) {
			case POLITICS:
				return new String[]{
					"정치계에서 중요한 변화가 일어나고 있습니다. 국민들의 관심이 집중되고 있는 가운데, 전문가들은 이번 변화가 미칠 영향을 분석하고 있습니다.",
					"새로운 정책이 발표되어 사회적 논란이 일고 있습니다. 다양한 의견이 나오고 있지만, 정부는 이 정책의 필요성을 강조하고 있습니다.",
					"정치 개혁을 위한 법안이 국회에서 논의되고 있습니다. 여야 간의 의견 차이가 있지만, 국민을 위한 합의점을 찾기 위해 노력하고 있습니다."
				};
			case ECONOMY:
				return new String[]{
					"경제 지표가 예상보다 좋은 결과를 보여주고 있습니다. 전문가들은 이번 결과가 경제 회복의 신호라고 분석하고 있습니다.",
					"주식 시장에서 새로운 변화가 일어나고 있습니다. 투자자들의 관심이 집중되고 있으며, 시장 동향을 주목해야 할 시기입니다.",
					"부동산 시장의 새로운 정책이 발표되어 시장에 변화가 예상됩니다. 전문가들은 신중한 접근이 필요하다고 조언하고 있습니다."
				};
			case SOCIETY:
				return new String[]{
					"사회적 이슈가 새로운 해결책을 찾고 있습니다. 시민들의 참여가 중요한 시점이며, 함께 고민해야 할 문제입니다.",
					"교육 현장에서 혁신적인 변화가 일어나고 있습니다. 학생들의 창의력과 사고력을 키우는 새로운 교육 방법이 도입되고 있습니다.",
					"의료 서비스의 개선을 위한 노력이 계속되고 있습니다. 환자 중심의 의료 서비스 제공을 위해 다양한 시도가 이루어지고 있습니다."
				};
			case LIFE_CULTURE:
				return new String[]{
					"문화의 다양성이 더욱 풍부해지고 있습니다. 다양한 문화 행사가 개최되어 시민들의 문화 생활이 활발해지고 있습니다.",
					"일상생활에서 유용한 정보들이 공유되고 있습니다. 실용적인 팁들이 많은 사람들에게 도움이 되고 있습니다.",
					"건강한 생활을 위한 다양한 방법들이 소개되고 있습니다. 전문가들의 조언을 통해 더 나은 삶을 살 수 있는 방법을 찾아보세요."
				};
			case WORLD:
				return new String[]{
					"세계 각국의 다양한 소식들이 전해지고 있습니다. 글로벌 이슈에 대한 이해가 중요한 시기입니다.",
					"국제 관계에서 새로운 변화가 일어나고 있습니다. 각국의 협력과 대화가 더욱 중요해지고 있습니다.",
					"세계 경제의 동향이 우리나라에 미치는 영향이 커지고 있습니다. 글로벌 시각으로 경제를 바라보는 것이 필요합니다."
				};
			case IT_SCIENCE:
				return new String[]{
					"기술의 발전이 우리의 삶을 더욱 편리하게 만들고 있습니다. 새로운 기술들이 다양한 분야에서 활용되고 있습니다.",
					"인공지능 기술의 발전이 가속화되고 있습니다. AI가 우리 생활에 미치는 영향이 점점 커지고 있습니다.",
					"과학 연구의 성과가 우리의 미래를 밝게 하고 있습니다. 혁신적인 연구 결과들이 다양한 분야에서 활용되고 있습니다."
				};
			case ENTERTAINMENT:
				return new String[]{
					"연예계에서 다양한 소식들이 전해지고 있습니다. 스타들의 활동과 새로운 작품들이 많은 관심을 받고 있습니다.",
					"영화와 드라마에서 새로운 작품들이 선보이고 있습니다. 다양한 장르의 작품들이 시청자들에게 즐거움을 제공하고 있습니다.",
					"음악계에서 새로운 트렌드가 나타나고 있습니다. 다양한 장르의 음악이 청취자들에게 새로운 경험을 제공하고 있습니다."
				};
			case SPORTS:
				return new String[]{
					"스포츠계에서 흥미진진한 소식들이 전해지고 있습니다. 선수들의 활약과 경기 결과가 많은 관심을 받고 있습니다.",
					"올림픽과 월드컵 등 주요 대회 준비가 활발하게 진행되고 있습니다. 국가 대표 선수들의 노력이 빛을 발하고 있습니다.",
					"스포츠 스타들의 인터뷰와 소식들이 전해지고 있습니다. 선수들의 이야기를 통해 스포츠의 매력을 느껴보세요."
				};
			default:
				return new String[]{
					"다양한 소식들이 전해지고 있습니다. 관심 있는 분야의 뉴스를 확인해보세요.",
					"새로운 이슈들이 등장하고 있습니다. 사회의 변화를 함께 지켜보세요."
				};
		}
	}

	/**
	 * 카테고리별 출처 배열을 반환합니다.
	 */
	private String[] getSourcesForCategory(NewsCategory category) {
		switch (category) {
			case POLITICS:
				return new String[]{"정치뉴스", "국회일보", "정책리뷰"};
			case ECONOMY:
				return new String[]{"경제일보", "금융뉴스", "시장동향"};
			case SOCIETY:
				return new String[]{"사회뉴스", "시민일보", "공동체소식"};
			case LIFE_CULTURE:
				return new String[]{"생활문화", "문화일보", "라이프스타일"};
			case WORLD:
				return new String[]{"세계뉴스", "국제일보", "글로벌리포트"};
			case IT_SCIENCE:
				return new String[]{"IT뉴스", "과학일보", "테크리뷰"};
			case ENTERTAINMENT:
				return new String[]{"연예뉴스", "엔터테인먼트", "스타일"};
			case SPORTS:
				return new String[]{"스포츠뉴스", "체육일보", "스포츠리뷰"};
			default:
				return new String[]{"종합뉴스", "일반뉴스", "뉴스데일리"};
		}
	}

	/**
	 * 카테고리별 이미지 URL 배열을 반환합니다.
	 */
	private String[] getImageUrlsForCategory(NewsCategory category) {
		switch (category) {
			case POLITICS:
				return new String[]{
					"https://images.unsplash.com/photo-1557804506-669a67965ba0?w=400&h=300&fit=crop",
					"https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=400&h=300&fit=crop",
					"https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&h=300&fit=crop"
				};
			case ECONOMY:
				return new String[]{
					"https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=400&h=300&fit=crop",
					"https://images.unsplash.com/photo-1559526324-4b87b5e36e44?w=400&h=300&fit=crop",
					"https://images.unsplash.com/photo-1554224155-6726b3ff858f?w=400&h=300&fit=crop"
				};
			case SOCIETY:
				return new String[]{
					"https://images.unsplash.com/photo-1521791136064-7986c2920216?w=400&h=300&fit=crop",
					"https://images.unsplash.com/photo-1559027615-cd4628902d4a?w=400&h=300&fit=crop",
					"https://images.unsplash.com/photo-1559028006-448665bd7c7f?w=400&h=300&fit=crop"
				};
			case LIFE_CULTURE:
				return new String[]{
					"https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=400&h=300&fit=crop",
					"https://images.unsplash.com/photo-1513151233558-d860c5398176?w=400&h=300&fit=crop",
					"https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400&h=300&fit=crop"
				};
			case WORLD:
				return new String[]{
					"https://images.unsplash.com/photo-1526772662000-3f88f10405ff?w=400&h=300&fit=crop",
					"https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400&h=300&fit=crop",
					"https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400&h=300&fit=crop"
				};
			case IT_SCIENCE:
				return new String[]{
					"https://images.unsplash.com/photo-1518709268805-4e9042af2176?w=400&h=300&fit=crop",
					"https://images.unsplash.com/photo-1518186285589-2f7649de83e0?w=400&h=300&fit=crop",
					"https://images.unsplash.com/photo-1518186285589-2f7649de83e0?w=400&h=300&fit=crop"
				};
			case ENTERTAINMENT:
				return new String[]{
					"https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=400&h=300&fit=crop",
					"https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=400&h=300&fit=crop",
					"https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=400&h=300&fit=crop"
				};
			case SPORTS:
				return new String[]{
					"https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400&h=300&fit=crop",
					"https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400&h=300&fit=crop",
					"https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400&h=300&fit=crop"
				};
			default:
				return new String[]{
					"https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400&h=300&fit=crop",
					"https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400&h=300&fit=crop",
					"https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400&h=300&fit=crop"
				};
		}
	}
} 