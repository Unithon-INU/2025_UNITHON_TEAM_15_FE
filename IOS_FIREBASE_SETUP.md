    # iOS Firebase Analytics 설정 가이드

## 개요
iOS 앱에서 Firebase Analytics를 연동하기 위한 설정 가이드입니다.
Android는 이미 Firebase Analytics가 연동되어 있으며, iOS도 동일한 방식으로 analytics를 수집할 수 있습니다.

## 현재 상태
- ✅ Kotlin 코드에서 Firebase Analytics wrapper 구조 완성
- ✅ Swift에서 Firebase Analytics Manager 구현 완료
- ⏳ Firebase SDK 추가 필요 (SPM 또는 CocoaPods)
- ⏳ GoogleService-Info.plist 추가 필요

## 1단계: Firebase 프로젝트에서 iOS 앱 추가

1. [Firebase Console](https://console.firebase.google.com/)에 접속
2. 현재 프로젝트 선택 (Android와 동일한 프로젝트 사용)
3. **프로젝트 설정** → **일반** → **앱 추가** → **iOS** 선택
4. **iOS 번들 ID** 입력: `unithon.helpjob` (build.gradle.kts에 정의된 applicationId와 동일)
5. **앱 등록** 후 `GoogleService-Info.plist` 파일 다운로드

## 2단계: GoogleService-Info.plist 추가

1. 다운로드한 `GoogleService-Info.plist` 파일을 다음 위치에 추가:
   ```
   iosApp/iosApp/GoogleService-Info.plist
   ```

2. Xcode에서 프로젝트 열기:
   ```bash
   open iosApp/iosApp.xcodeproj
   ```

3. Xcode의 Project Navigator에서 `GoogleService-Info.plist`를 드래그하여 `iosApp` 폴더에 추가
   - ✅ **Copy items if needed** 체크
   - ✅ **Add to targets**: `iosApp` 선택

## 3단계: Firebase SDK 추가 (Swift Package Manager 권장)

### 방법 1: Swift Package Manager (SPM) - 권장

1. Xcode에서 **File** → **Add Package Dependencies...**
2. 다음 URL 입력:
   ```
   https://github.com/firebase/firebase-ios-sdk
   ```
3. **Dependency Rule**: `Up to Next Major Version` → `11.6.0` 선택
4. **Add Package**
5. 패키지 목록에서 다음 항목 선택:
   - ✅ **FirebaseAnalytics**
   - ✅ **FirebaseCore**
6. **Add Package** 클릭

### 방법 2: CocoaPods (선택사항)

CocoaPods를 이미 사용 중인 경우:

1. `iosApp/Podfile` 생성 또는 수정:
   ```ruby
   platform :ios, '14.0'
   use_frameworks!

   target 'iosApp' do
     pod 'FirebaseAnalytics', '~> 11.6.0'
   end
   ```

2. 터미널에서 실행:
   ```bash
   cd iosApp
   pod install
   ```

3. 이후 `iosApp.xcworkspace` 사용

## 4단계: FirebaseAnalyticsManager.swift 수정

`iosApp/iosApp/FirebaseAnalyticsManager.swift` 파일 상단에 import 추가:

```swift
import Foundation
import ComposeApp
import FirebaseAnalytics  // 👈 추가

class FirebaseAnalyticsManager: FirebaseAnalyticsWrapper {
    // ... 기존 코드 ...

    func logEvent(name: String, params: [String : Any]?) {
        #if DEBUG
        print("📊 [Firebase Analytics] Event: \(name)")
        if let params = params {
            params.forEach { key, value in
                print("   - \(key): \(value)")
            }
        }
        #endif

        // 실제 Firebase Analytics 호출 (주석 해제)
        Analytics.logEvent(name, parameters: params)  // 👈 주석 해제
    }
}
```

## 5단계: iOSApp.swift에서 Firebase 초기화

`iosApp/iosApp/iOSApp.swift` 파일 수정:

```swift
import SwiftUI
import ComposeApp
import FirebaseCore  // 👈 추가

@main
struct iOSApp: App {

    init() {
        // Firebase 초기화 (주석 해제)
        FirebaseApp.configure()  // 👈 주석 해제

        // Koin 초기화 (Firebase Analytics Wrapper 주입)
        IosKoinKt.doInitKoin(analyticsWrapper: FirebaseAnalyticsManager.shared)
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
```

## 6단계: 빌드 및 테스트

1. 터미널에서 프로젝트 루트로 이동
2. Gradle sync 실행:
   ```bash
   ./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
   ```

3. Xcode에서 앱 실행
4. Analytics 이벤트가 정상적으로 로그되는지 확인

## 테스트

앱에서 Analytics를 사용하는 예시:

```kotlin
// Kotlin 코드 어디서든 사용 가능
Analytics.logEvent("screen_view", mapOf(
    "screen_name" to "HomeScreen",
    "user_id" to "12345"
))
```

Xcode 콘솔에서 다음과 같은 로그 확인:
```
📊 [Firebase Analytics] Event: screen_view
   - screen_name: HomeScreen
   - user_id: 12345
```

Firebase Console에서 실시간 이벤트 확인:
1. [Firebase Console](https://console.firebase.google.com/) → 프로젝트 선택
2. **Analytics** → **DebugView** 또는 **이벤트** 탭에서 확인

## 구조 설명

### KMP 아키텍처
```
commonMain/
  └─ util/Analytics.kt (expect)
  └─ data/analytics/AnalyticsService.kt (interface)

androidMain/
  └─ util/Analytics.kt (actual)
  └─ data/analytics/AndroidAnalyticsService.kt
  └─ HelpJobApplication.kt (Firebase 초기화)

iosMain/
  └─ util/Analytics.kt (actual)
  └─ data/analytics/IOSAnalyticsService.kt
  └─ data/analytics/FirebaseAnalyticsWrapper (interface)
  └─ di/IosKoin.kt (Koin 초기화)

iosApp/
  └─ iOSApp.swift (Swift - Firebase 초기화)
  └─ FirebaseAnalyticsManager.swift (Swift - Wrapper 구현)
  └─ GoogleService-Info.plist (Firebase 설정)
```

### 장점
- ✅ Android/iOS 동일한 Kotlin API로 analytics 사용
- ✅ 각 플랫폼에 최적화된 네이티브 Firebase SDK 사용
- ✅ Koin DI를 통한 깔끔한 의존성 관리
- ✅ 테스트 환경에서 Mock으로 쉽게 교체 가능

## 문제 해결

### "GoogleService-Info.plist not found" 오류
→ 2단계를 다시 확인하고 파일이 올바른 위치에 있는지, Xcode 프로젝트에 추가되었는지 확인

### "Module 'FirebaseAnalytics' not found" 오류
→ 3단계를 다시 확인하고 SPM 패키지가 올바르게 추가되었는지 확인

### Firebase Console에 이벤트가 나타나지 않음
→ 실시간 데이터는 최대 24시간 지연될 수 있음. **DebugView**를 사용하면 즉시 확인 가능

### DebugView 활성화 방법
Xcode에서 Scheme 편집:
1. **Product** → **Scheme** → **Edit Scheme...**
2. **Run** → **Arguments** 탭
3. **Arguments Passed On Launch**에 추가:
   ```
   -FIRDebugEnabled
   ```

## 참고 자료
- [Firebase iOS SDK GitHub](https://github.com/firebase/firebase-ios-sdk)
- [Firebase Analytics 문서](https://firebase.google.com/docs/analytics/get-started?platform=ios)
- [Kotlin Multiplatform 공식 문서](https://kotlinlang.org/docs/multiplatform.html)
