# iOS 앱 설정 가이드

이 문서는 Android 개발자가 iOS 앱을 설정하고 실행하는 방법을 안내합니다.

## ✅ 완료된 작업 목록

1. ✅ **Xcode 프로젝트 구조 생성**
   - `iosApp/iosApp/` 디렉토리 생성
   - 필요한 하위 디렉토리 구조 설정

2. ✅ **iOS 앱 진입점 파일 작성**
   - `iOSApp.swift`: SwiftUI 앱 진입점, Koin 초기화
   - `ContentView.swift`: Compose UI를 SwiftUI로 연동

3. ✅ **빌드 설정 파일 생성**
   - `Config.xcconfig`: 팀 ID, 번들 ID, 버전 등 설정
   - `Info.plist`: 앱 메타데이터, 권한, 다국어 설정
   - `Assets.xcassets`: 앱 아이콘 자산

4. ✅ **iOS DI 모듈 구현**
   - `composeApp/src/iosMain/kotlin/unithon/helpjob/di/IosKoin.kt`
   - `composeApp/src/iosMain/kotlin/unithon/helpjob/di/platformModule.kt`
   - HttpClient (Darwin 엔진), DataStore, ViewModel 등 설정

5. ✅ **iosMain actual 구현 완료**
   - `PlatformActions.kt`: OSS 라이선스 화면 (stub)
   - `Analytics.kt`: Firebase iOS SDK 연동 준비 (stub)
   - `NumberFormatter.kt`: NSNumberFormatter 사용
   - `DataStore.kt`: iOS DataStore 설정
   - `EmailValidator.kt`: 이메일 검증
   - `AppConfig.kt`: BuildKonfig 설정 접근

6. ✅ **IosLanguageRepository 구현**
   - `composeApp/src/iosMain/kotlin/unithon/helpjob/data/repository/IosLanguageRepository.kt`
   - DataStore 기반 언어 설정 저장/복원

7. ✅ **MainViewController 추가**
   - `composeApp/src/iosMain/kotlin/unithon/helpjob/MainViewController.kt`
   - Compose UI를 UIViewController로 래핑

8. ✅ **Gradle 빌드 설정**
   - iOS 타겟 추가 (iosX64, iosArm64, iosSimulatorArm64)
   - ComposeApp 프레임워크 생성 설정
   - Ktor Darwin 엔진 의존성 추가
   - BuildKonfig iOS 설정

## 📋 남은 작업

### 1. Xcode 프로젝트 파일 생성

현재 Swift 파일과 설정 파일만 있고, Xcode 프로젝트 파일(`.xcodeproj`)은 수동으로 생성해야 합니다.

**옵션 A: Xcode에서 새 프로젝트 생성 (권장)**

1. Xcode 열기
2. File → New → Project
3. iOS → App 선택
4. 프로젝트 설정:
   - Product Name: `iosApp`
   - Bundle Identifier: `unithon.helpjob`
   - Interface: `SwiftUI`
   - Language: `Swift`
   - 저장 위치: 프로젝트 루트의 `iosApp/` 디렉토리
5. 생성된 기본 파일 삭제하고, 우리가 만든 파일로 대체:
   - 기존 `iosAppApp.swift` → 삭제 후 `iOSApp.swift` 사용
   - 기존 `ContentView.swift` → 우리 버전으로 대체
6. Build Phases에서 Compile Sources 설정
7. Build Settings에서 Framework Search Paths 추가:
   ```
   $(SRCROOT)/../composeApp/build/bin/iosSimulatorArm64/debugFramework
   ```
8. General → Frameworks, Libraries 섹션에서 `ComposeApp.framework` 추가

**옵션 B: KMP 템플릿 사용**

```bash
# KMP 템플릿에서 Xcode 프로젝트 복사
git clone https://github.com/Kotlin/KMP-App-Template.git /tmp/kmp-template
cp -r /tmp/kmp-template/iosApp/iosApp.xcodeproj ./iosApp/
```

그 후 프로젝트 설정을 HelpJob에 맞게 수정:
- Bundle Identifier: `unithon.helpjob`
- Display Name: `HelpJob`
- Framework 경로 수정

### 2. Firebase iOS SDK 연동 (선택사항)

Analytics를 실제로 사용하려면:

1. Firebase Console에서 iOS 앱 추가
2. `GoogleService-Info.plist` 다운로드
3. Xcode 프로젝트에 추가
4. CocoaPods 또는 SPM으로 Firebase SDK 추가:
   ```ruby
   # Podfile
   pod 'FirebaseAnalytics'
   ```
5. `Analytics.kt`의 stub 구현을 실제 Firebase API 호출로 변경

### 3. OSS 라이선스 화면 구현 (선택사항)

현재 `PlatformActions.kt`의 `openOssLicenses()`는 stub입니다.

구현 방법:
- WebView로 HTML 라이선스 페이지 표시
- 또는 SwiftUI List로 네이티브 화면 구현
- 또는 서드파티 라이브러리 사용

## 🚀 빌드 및 실행 방법

### 1. 팀 ID 설정

`iosApp/iosApp/Configuration/Config.xcconfig` 파일 수정:

```
TEAM_ID = YOUR_TEAM_ID_HERE
```

팀 ID는 [Apple Developer Account](https://developer.apple.com/account)에서 확인할 수 있습니다.
- 로그인 → Membership → Team ID

### 2. Kotlin Framework 빌드

```bash
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
```

이 명령은 다음을 수행합니다:
- Kotlin 공유 코드를 iOS framework로 컴파일
- `composeApp/build/bin/iosSimulatorArm64/debugFramework/ComposeApp.framework` 생성

### 3. Xcode 프로젝트 열기

```bash
open iosApp/iosApp.xcodeproj
```

### 4. 시뮬레이터 선택 및 실행

1. Xcode 상단에서 시뮬레이터 선택 (예: iPhone 15 Pro)
2. ⌘ + R 또는 Run 버튼 클릭
3. 앱이 시뮬레이터에서 실행됩니다

### 5. 실제 기기에서 실행 (선택)

1. iPhone을 Mac에 연결
2. Xcode에서 디바이스 선택
3. Signing & Capabilities에서 Team 선택
4. ⌘ + R로 실행

## 🔧 트러블슈팅

### ❌ "Framework not found ComposeApp"

```bash
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
```

Framework를 빌드하지 않았거나 경로가 잘못되었습니다.

**해결 방법:**
1. Framework 빌드 실행
2. Xcode Build Settings → Framework Search Paths 확인
3. 경로가 올바른지 확인

### ❌ "No such module 'ComposeApp'"

Swift 코드에서 `import ComposeApp`를 찾을 수 없습니다.

**해결 방법:**
1. Framework가 빌드되었는지 확인
2. Xcode → General → Frameworks, Libraries에 추가되었는지 확인
3. Clean Build Folder (⌘ + Shift + K) 후 재빌드

### ❌ "Koin not initialized"

`IosKoinKt.doInitKoin()`이 호출되지 않았습니다.

**해결 방법:**
- `iOSApp.swift`의 `init()`에서 Koin 초기화 코드 확인
- 함수명이 `initKoin()`에서 `doInitKoin()`으로 변환되었는지 확인

### ❌ "Could not find module 'MainViewControllerKt'"

**해결 방법:**
- `MainViewController.kt` 파일이 올바른 위치에 있는지 확인
- Framework 재빌드 후 Xcode Clean Build

### ⚠️ 빌드는 되지만 화면이 안 나옴

**해결 방법:**
1. Xcode 콘솔에서 에러 메시지 확인
2. `ContentView.swift`가 올바르게 작성되었는지 확인
3. `HelpJobApp()` Composable이 정의되어 있는지 확인

### 🐌 시뮬레이터가 느림

**M1/M2/M3 Mac:**
- `iosSimulatorArm64` 타겟 사용 (기본값)

**Intel Mac:**
- `iosX64` 타겟으로 빌드:
  ```bash
  ./gradlew :composeApp:linkDebugFrameworkIosX64
  ```

## 📁 생성된 파일 구조

```
2025_UNITHON_TEAM_15_FE/
├── iosApp/
│   ├── iosApp/
│   │   ├── iOSApp.swift                    # ✅ 생성 완료
│   │   ├── ContentView.swift               # ✅ 생성 완료
│   │   ├── Info.plist                      # ✅ 생성 완료
│   │   ├── Assets.xcassets/                # ✅ 생성 완료
│   │   │   ├── Contents.json
│   │   │   └── AppIcon.appiconset/
│   │   │       └── Contents.json
│   │   └── Configuration/
│   │       └── Config.xcconfig             # ✅ 생성 완료
│   ├── iosApp.xcodeproj/                   # ❌ 수동 생성 필요
│   └── README.md                           # ✅ 생성 완료
│
├── composeApp/
│   ├── src/
│   │   ├── iosMain/kotlin/unithon/helpjob/
│   │   │   ├── MainViewController.kt       # ✅ 생성 완료
│   │   │   ├── di/
│   │   │   │   ├── IosKoin.kt             # ✅ 생성 완료
│   │   │   │   └── platformModule.kt       # ✅ 생성 완료
│   │   │   ├── data/repository/
│   │   │   │   └── IosLanguageRepository.kt # ✅ 생성 완료
│   │   │   ├── ui/components/
│   │   │   │   ├── HtmlWebView.kt         # ✅ 기존 파일
│   │   │   │   └── UrlWebView.kt          # ✅ 기존 파일
│   │   │   └── util/
│   │   │       ├── Analytics.kt           # ✅ 개선 완료
│   │   │       ├── AppConfig.kt           # ✅ 기존 파일
│   │   │       ├── DataStore.kt           # ✅ 생성 완료
│   │   │       ├── EmailValidator.kt      # ✅ 기존 파일
│   │   │       ├── NumberFormatter.kt     # ✅ 기존 파일
│   │   │       └── PlatformActions.kt     # ✅ 개선 완료
│   │   └── ...
│   └── build.gradle.kts                    # ✅ iOS 타겟 추가 완료
│
├── gradle/
│   └── libs.versions.toml                  # ✅ ktor-client-darwin 추가 완료
│
├── README.md                               # ✅ iOS 섹션 추가 완료
└── iOS_SETUP_GUIDE.md                      # ✅ 이 파일
```

## 📝 다음 단계

1. **Xcode 프로젝트 생성** (위의 "남은 작업" 섹션 참고)
2. **첫 빌드 실행** 및 에러 수정
3. **Firebase Analytics 연동** (선택)
4. **실제 기기 테스트**
5. **App Store 배포 준비** (필요 시)

## 💡 개발 팁

### Kotlin 코드 수정 시 워크플로우

1. `composeApp/src/commonMain` 또는 `iosMain`에서 코드 수정
2. Framework 재빌드:
   ```bash
   ./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
   ```
3. Xcode Clean Build (⌘ + Shift + K)
4. 재실행 (⌘ + R)

### 자동 빌드 스크립트 설정

Xcode Build Phases에서 "Run Script" 추가:

```bash
cd "$SRCROOT/../.."
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
```

이렇게 하면 Xcode에서 빌드할 때마다 자동으로 Kotlin framework가 빌드됩니다.

## 🔗 참고 자료

- [KMP App Template](https://github.com/Kotlin/KMP-App-Template) - 공식 KMP 템플릿
- [Compose Multiplatform iOS](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-getting-started.html)
- [Kotlin Multiplatform Mobile Docs](https://kotlinlang.org/docs/multiplatform-mobile-getting-started.html)
- [iosApp/README.md](iosApp/README.md) - iOS 앱 상세 문서

## ❓ 도움이 필요하면

- Slack에서 iOS 팀원에게 문의
- 또는 이 가이드의 트러블슈팅 섹션 참고
- KMP 공식 슬랙: [kotlinlang.slack.com](https://kotlinlang.slack.com)

---

작성자: Claude Code
마지막 업데이트: 2025-11-17
