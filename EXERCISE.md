# 네이버 뉴스 스크래퍼 실습 워크북

## 실습 목표

- 도메인에서 외부 어댑터로 확장하는 약식 클린 아키텍처 구현
- 임시 `main` 메서드를 활용한 빠른 동작 확인
- `Scanner` 기반 콘솔 앱을 통한 로컬 실행 흐름 완성
- GitHub Actions와 GitHub Issue를 활용한 자동 수집·발행 흐름 완성
- 포트와 어댑터 교체를 통한 DIP·OCP 가치 확인

## 1단계. 약식 클린 아키텍처 구현

### 1-1. `domain` 핵심 모델 구현

- 외부 API·콘솔·GitHub와 무관한 핵심 데이터부터 확정하는 단계
- 바깥쪽 기술 변경의 영향을 받지 않는 도메인 우선 구현

- [ ] `src/oop/scraper/domain/NewsResult.java` 생성
- [ ] 제목·링크·요약·발행일을 담는 `NewsResult` record 정의
- [ ] 제목과 요약의 HTML 태그 및 주요 HTML·숫자 엔티티 정제 규칙 구현
- [ ] `src/oop/scraper/domain/NewsCategory.java` 생성
- [ ] 정확도순 `SIM`과 최신순 `DATE`를 표현하는 enum 정의
- [ ] 네이버 API의 `sort` 쿼리 값과 사용자용 설명 연결

### 1-2. `application` 포트와 유스케이스 구현

- 도메인만 의존하는 애플리케이션 경계 확정 단계
- 구체적인 네이버 API와 출력 기술보다 포트를 먼저 정의하여 DIP 의존 방향 확보

- [ ] `NewsProvider` 수집 포트 정의
- [ ] 검색어와 개수를 받아 `List<NewsResult>`를 반환하는 계약 작성
- [ ] `NewsPublisher` 발행 포트 정의
- [ ] 검색 주제와 결과 목록을 발행하는 계약 작성
- [ ] `NewsService` 유스케이스 구현
- [ ] 생성자를 통한 `NewsProvider`·`NewsPublisher` 주입
- [ ] 뉴스 수집 후 발행하는 애플리케이션 흐름 작성
- [ ] 콘솔 문자열과 Markdown 문자열 변환 기능 작성

### 1-3. `infrastructure` 외부 연동 구현

- 애플리케이션이 정의한 포트를 외부 기술로 구현하는 단계
- 안쪽 계층이 바깥쪽 구현을 알지 않고, 바깥쪽 구현이 안쪽 포트에 맞추는 DIP 방향 유지

- [ ] `AbstractHttpScraper` 추상 골격 구현
- [ ] URL 인코딩, HTTP 호출, 상태 코드 검사, 응답 파싱의 공통 흐름 작성
- [ ] 요청 생성과 정렬 값 결정을 하위 클래스에 맡기는 템플릿 메서드 구조 작성
- [ ] 검색어 공백 및 검색 개수 `1`~`100` 검증
- [ ] `NaverNewsProvider` 수집 어댑터 구현
- [ ] 네이버 뉴스 검색 API 엔드포인트와 인증 헤더 연결
- [ ] `NewsCategory`에 따른 정확도순·최신순 쿼리 연결
- [ ] `GitHubIssuePublisher` 발행 어댑터 구현
- [ ] 검색 주제와 날짜를 포함한 Issue 제목 구성
- [ ] `NewsService.toMarkdown` 결과를 Issue 본문으로 구성
- [ ] GitHub REST API 인증 헤더와 JSON 요청 본문 구성

## 2단계. 임시 `main` 메서드로 중간 검증

- 정식 테스트 프레임워크와 TDD를 아직 도입하지 않는 단계
- 각 모델·계층·어댑터 구현 직후 임시 `public static void main(String[] args)`를 추가하여 직접 호출하는 방식
- 작은 단위의 입력과 출력을 눈으로 확인하여 다음 구현 전 오류를 줄이기 위한 빠른 피드백 목적
- 최종 설계의 일부가 아니며 추후 JUnit 기반 정식 테스트로 대체할 임시 검증

### 2-1. 도메인과 애플리케이션 검증

- [ ] `NewsResult`에 HTML 태그와 엔티티가 포함된 문자열 전달
- [ ] 생성된 record의 제목과 요약 정제 결과 출력 및 확인
- [ ] `NewsCategory.SIM`·`NewsCategory.DATE`의 쿼리 값 출력 및 확인
- [ ] 가짜 `NewsProvider`와 가짜 `NewsPublisher`를 간단한 람다 또는 익명 클래스로 작성
- [ ] `NewsService.search` 호출 시 수집 결과가 발행 포트까지 전달되는지 확인
- [ ] 빈 결과의 콘솔·Markdown 문구와 여러 결과의 서식 확인

### 2-2. HTTP 수집 어댑터 검증

- [ ] `.env` 또는 실행 구성에 `NAVER_CLIENT_ID`·`NAVER_CLIENT_SECRET` 준비
- [ ] `NaverNewsProvider` 구현 직후 임시 `main`에서 실제 API 호출
- [ ] 검색어와 개수를 직접 지정하여 반환된 `NewsResult` 목록 출력
- [ ] 제목의 HTML 태그 제거, 엔티티 변환, 링크·발행일·요약 파싱 결과 확인
- [ ] `SIM`과 `DATE` 변경에 따른 정렬 결과 차이 확인
- [ ] 빈 검색어와 범위 밖 검색 개수에 대한 예외 확인

### 2-3. 발행 어댑터 검증

- [ ] `GitHubIssuePublisher`에 테스트용 저장소 정보와 토큰 주입
- [ ] 소량의 가짜 `NewsResult` 목록을 임시 `main`에서 발행
- [ ] Issue 제목의 검색어·날짜와 본문의 Markdown 형식 확인
- [ ] 인증 실패 또는 잘못된 저장소 지정 시 오류 메시지 확인
- [ ] 검증 완료 후 각 클래스의 임시 `main` 제거

## 3단계. `Scanner` 기반 콘솔 앱 완성

- 검증된 컴포넌트를 하나의 로컬 실행 앱으로 조립하는 1차 샌드박스 단계
- 사용자 입력을 애플리케이션 유스케이스에 전달하고 결과를 콘솔로 발행하는 구성

### 3-1. 환경변수 준비

- [ ] 프로젝트 루트의 `.env.sample`을 `.env`로 복사
- [ ] `.env`에 `NAVER_CLIENT_ID`·`NAVER_CLIENT_SECRET` 입력
- [ ] IntelliJ Run/Debug Configuration에서 `.env` 파일 연동
- [ ] 인증정보를 소스 코드와 분리하고 `.env` 커밋 제외 상태 확인

### 3-2. 콘솔 발행 어댑터 구현

- [ ] `ConsolePublisher`가 `NewsPublisher`를 구현하도록 작성
- [ ] 검색어와 결과 수를 포함한 헤더 출력
- [ ] `NewsService.toConsoleText`를 이용한 뉴스 목록 출력

### 3-3. 대화형 실행 진입점 구현

- [ ] `ConsoleNewsApp`의 `main` 메서드 작성
- [ ] `Scanner`로 검색 키워드와 검색 개수 입력 처리
- [ ] 빈 검색어, 숫자가 아닌 개수, `1`~`100` 범위 밖 개수 검증
- [ ] 환경변수에서 네이버 인증정보 조회
- [ ] `NaverNewsProvider`와 `ConsolePublisher`를 `NewsService`에 주입
- [ ] 유스케이스 실행 후 콘솔 출력 확인
- [ ] 로컬 콘솔 앱에서 정확도순 `NewsCategory.SIM` 사용 확인

## 4단계. GitHub Actions 자동화로 이전

- 동일한 `NewsService`를 자동화 환경에 맞는 어댑터로 재조립하는 단계
- `Scanner` 제거와 환경변수 기반 입력 전환
- 발행처를 `ConsolePublisher`에서 `GitHubIssuePublisher`로 교체하면서 서비스 코드 무수정 확인
- 포트 기반 확장에 따른 OCP와 DIP의 실제 효과 확인

### 4-1. 자동화 실행 진입점 구현

- [ ] `AutomationNewsApp`의 `main` 메서드 작성
- [ ] `NAVER_CLIENT_ID`·`NAVER_CLIENT_SECRET` 필수값 조회
- [ ] `GITHUB_TOKEN`·`GITHUB_REPOSITORY` 필수값 조회
- [ ] `NEWS_QUERY`·`NEWS_DISPLAY` 선택값 및 기본값 처리
- [ ] `GITHUB_REPOSITORY`의 `owner/repo` 형식 검증
- [ ] 최신순 `NewsCategory.DATE`를 사용하는 `NaverNewsProvider` 구성
- [ ] `GitHubIssuePublisher` 구성 후 기존 `NewsService`에 주입
- [ ] `Scanner` 없이 환경변수만으로 수집·발행 완료 확인

### 4-2. GitHub Actions 워크플로우 작성

- [ ] `.github/workflows/news-scraper.yml` 생성
- [ ] 저장소 체크아웃 단계 구성
- [ ] Temurin JDK 17 설정 단계 구성
- [ ] `javac`를 이용한 `src/oop` 전체 컴파일 단계 구성
- [ ] `AutomationNewsApp` 실행 단계 구성
- [ ] `contents: read`·`issues: write` 권한 설정
- [ ] Actions 탭에서 직접 실행 가능한 `workflow_dispatch` 설정
- [ ] cron 스케줄을 통한 중단 없는 주기적 자동 수집·Issue 발행 설정
- [ ] cron이 UTC 기준이라는 점을 고려한 실행 시각 확인

### 4-3. 저장소 환경 설정 및 실행 확인

- [ ] Repository Secrets에 `NAVER_CLIENT_ID` 등록
- [ ] Repository Secrets에 `NAVER_CLIENT_SECRET` 등록
- [ ] Repository Variables에 `NEWS_QUERY` 등록
- [ ] Repository Variables에 `NEWS_DISPLAY` 등록
- [ ] GitHub 자동 제공 `GITHUB_TOKEN`과 `${{ github.repository }}` 연결 확인
- [ ] 수동 실행으로 컴파일·수집·Issue 발행 성공 확인
- [ ] 예약 실행으로 동일 흐름의 반복 동작 확인
- [ ] 생성된 Issue의 제목, 뉴스 순서, 링크, 발행일, 요약 확인

## 완료 점검

- [ ] 의존성 방향이 `presentation`·`infrastructure`에서 `application`과 `domain`을 향하는 구조 확인
- [ ] `NewsService`가 네이버 API와 GitHub API의 구체 구현을 직접 참조하지 않는 상태 확인
- [ ] 콘솔 앱과 자동화 앱이 동일 유스케이스를 서로 다른 조합으로 사용하는 상태 확인
- [ ] 인증정보가 코드와 저장소에 포함되지 않는 상태 확인
- [ ] 로컬 실행과 GitHub Actions 실행의 결과 확인

## 다음 단계(향후)

- 임시 `main` 기반 검증을 JUnit 테스트와 TDD 기반의 반복 가능한 자동 검증으로 전환
