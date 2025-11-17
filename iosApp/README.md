# HelpJob iOS App

KMP(Kotlin Multiplatform) 기반 HelpJob 앱의 iOS 버전입니다.

## 📋 요구사항

- macOS (Xcode 실행을 위해 필수)
- Xcode 15.0 이상
- iOS 15.0 이상 지원
- Apple Developer 계정 (실제 기기 테스트 시)

## 🚀 빌드 방법

### 1. Kotlin Framework 빌드

먼저 Kotlin Multiplatform 공유 코드를 iOS framework로 빌드해야 합니다.

```bash
# 루트 프로젝트 디렉토리에서 실행
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
```

또는 Xcode에서 자동으로 빌드되도록 설정할 수 있습니다.

### 2. Xcode 프로젝트 열기

```bash
open iosApp/iosApp.xcodeproj
```

### 3. 팀 ID 설정

`iosApp/iosApp/Configuration/Config.xcconfig` 파일을 열고 팀 ID를 입력합니다:

```
TEAM_ID = YOUR_TEAM_ID_HERE
```

팀 ID는 [Apple Developer](https://developer.apple.com/account) 계정에서 확인할 수 있습니다.

### 4. 시뮬레이터 실행

1. Xcode에서 타겟 디바이스를 선택 (예: iPhone 15 Pro)
2. ⌘ + R 또는 Run 버튼 클릭
3. 앱이 시뮬레이터에서 실행됩니다

## 📁 프로젝트 구조

```
iosApp/
├── iosApp/
│   ├── iOSApp.swift              # 앱 진입점
│   ├── ContentView.swift         # Compose UI 연동
│   ├── Info.plist               # 앱 메타데이터
│   ├── Assets.xcassets/         # 앱 아이콘 등
│   └── Configuration/
│       └── Config.xcconfig      # 빌드 설정
└── README.md                    # 이 파일
```

## 🔧 설정 파일

### Config.xcconfig

앱의 기본 설정을 관리합니다:

- `TEAM_ID`: Apple Developer 팀 ID
- `BUNDLE_ID`: unithon.helpjob
- `VERSION`: 1.0.0
- `IPHONEOS_DEPLOYMENT_TARGET`: 15.0

### Info.plist

앱의 메타데이터와 권한을 정의합니다:

- 다국어 지원 (한국어, 영어)
- 네트워크 권한 설정
- UI 방향 설정

## 🌐 다국어 지원

앱은 한국어와 영어를 지원합니다. 언어 설정은 DataStore에 저장되며, 앱 재시작 시에도 유지됩니다.

## 🔐 API 설정

API 베이스 URL은 `local.properties` 파일에서 설정합니다:

```properties
API_BASE_URL=https://your-api-server.com
```

## 📱 주요 기능

- ✅ 취업 체크리스트 관리
- 💰 급여 계산기
- 📄 서류 생성
- 👤 프로필 관리
- ⚙️ 설정 (언어 전환, 정책 확인 등)

## 🛠️ 개발 가이드

### Kotlin 공유 코드 수정 시

1. `composeApp/src/commonMain` 또는 `composeApp/src/iosMain`에서 코드 수정
2. Kotlin framework 재빌드:
   ```bash
   ./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
   ```
3. Xcode에서 Clean Build Folder (⌘ + Shift + K)
4. 재실행

### iOS 전용 구현 (actual)

iOS 플랫폼 전용 구현은 `composeApp/src/iosMain`에 위치합니다:

- `util/Analytics.kt`: 분석 이벤트 로깅
- `util/PlatformActions.kt`: 플랫폼 특정 동작
- `util/NumberFormatter.kt`: 숫자 포맷팅
- `util/DataStore.kt`: iOS DataStore 설정
- `data/repository/IosLanguageRepository.kt`: 언어 설정 저장소
- `di/platformModule.kt`: iOS DI 모듈

## 🐛 트러블슈팅

### Framework not found

```bash
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
```

### 빌드 실패

1. Xcode Clean Build Folder (⌘ + Shift + K)
2. Derived Data 삭제:
   ```bash
   rm -rf ~/Library/Developer/Xcode/DerivedData
   ```
3. 재빌드

### 시뮬레이터 느림

- M1/M2 Mac의 경우: `iosSimulatorArm64` 타겟 사용
- Intel Mac의 경우: `iosX64` 타겟 사용

## 📚 참고 자료

- [Kotlin Multiplatform Mobile](https://kotlinlang.org/docs/multiplatform-mobile-getting-started.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [KMP App Template](https://github.com/Kotlin/KMP-App-Template)

## 🔄 CI/CD

GitHub Actions를 통한 자동 빌드 설정이 가능합니다. (별도 설정 필요)

## 📝 라이선스

이 프로젝트는 [라이선스 정보]에 따라 배포됩니다.
