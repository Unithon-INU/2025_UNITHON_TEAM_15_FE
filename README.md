# HelpJob - 외국인 근로자 취업 지원 앱

KMP(Kotlin Multiplatform) 기반 Android/iOS 크로스 플랫폼 앱

## 📱 플랫폼

- ✅ **Android**: 완료 (API 23+)
- 🚧 **iOS**: 개발 중 (iOS 15.0+, 브랜치: `kmp/ios`)

## 🚀 빌드 방법

### Android

```bash
./gradlew :composeApp:assembleDebug
```

### iOS

iOS 빌드 방법은 [iosApp/README.md](iosApp/README.md)를 참고하세요.

**빠른 시작:**

1. **팀 ID 설정**: `iosApp/iosApp/Configuration/Config.xcconfig`에서 `TEAM_ID` 입력
2. **Framework 빌드**: `./gradlew :composeApp:embedAndSignAppleFrameworkForXcode`
3. **Xcode 실행**: `open iosApp/iosApp.xcodeproj`
4. **시뮬레이터 실행**: ⌘ + R

## 🔧 환경 설정

`local.properties` 파일에 다음 내용을 추가하세요:

```properties
API_BASE_URL=https://your-api-server.com

# Android 릴리즈 빌드용 (선택)
KEYSTORE_FILE=../helpjob-release.keystore.jks
KEYSTORE_PASSWORD=your_password
KEY_ALIAS=your_alias
KEY_PASSWORD=your_password
```

## 🌿 브랜치 전략

### 네이밍 규칙
- `main`: 프로덕션
- `kmp/*`: KMP 마이그레이션
- `refactor/*`: 리팩토링
- `feature/*`: 새 기능
- `hotfix/*`: 긴급 수정

### 워크플로우
1. `git checkout -b [타입]/[이름]`
2. 작업 후 PR → main
3. 머지 후 브랜치 삭제

## 📚 기술 스택

- **공통**: Kotlin 2.1.20, Compose Multiplatform 1.7.3
- **네트워크**: Ktor 3.1.3
- **DI**: Koin 4.1.0
- **상태관리**: ViewModel, StateFlow
- **데이터 저장**: DataStore
- **Android**: OkHttp, Firebase Analytics
- **iOS**: Darwin Engine, NSUserDefaults