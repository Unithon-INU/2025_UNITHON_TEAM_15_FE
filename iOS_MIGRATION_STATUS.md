# iOS 마이그레이션 현재 상태

날짜: 2025-11-17
브랜치: `kmp/ios`

## ✅ 완료된 작업

### 1. iOS 프로젝트 구조 및 설정 (100% 완료)
- [x] iosApp 디렉토리 및 Swift 파일 생성
- [x] iOS DI 모듈 구현 (Koin)
- [x] iOS actual 구현 (PlatformActions, Analytics, NumberFormatter 등)
- [x] IosLanguageRepository 및 DataStore 구현
- [x] Gradle 빌드 설정 (iOS 타겟 추가)
- [x] 의존성 설정 (ktor-client-darwin 등)
- [x] Logger 유틸리티 구현 (Timber 대체)

### 2. 플랫폼 독립적 코드 분리 (70% 완료)
- [x] Timber → Logger로 교체 (commonMain에서 Android 전용 제거)
- [x] NumberFormatter actual 구현
- [x] Analytics actual 구현
- [x] PlatformActions actual 구현

## 🚧 남은 작업

### 핵심 이슈: commonMain에 Android 전용 코드 존재

원래 이 프로젝트는 Android 전용이었기 때문에, `commonMain`에 Android 전용 API가 많이 섞여 있습니다:

**발견된 Android 전용 코드:**
1. **LocalContext** (Compose Android 전용)
   - 위치: `DocumentScreen.kt`, `WorkplaceInfo*.kt`
   - 영향: 문서 생성 화면들

2. **Preview** (Android Studio 프리뷰 전용)
   - 위치: 여러 화면 파일
   - 영향: 빌드 시 에러 (프리뷰는 실행에 영향 없음)

3. **String.format()** (Java 전용)
   - 위치: `CalculatorScreen.kt`, `WorkplaceInfo4Screen.kt`
   - 영향: 날짜 포맷팅

4. **DataStore Path 타입**
   - 위치: `DataStore.kt` (iosMain)
   - 문제: String을 Path로 변환 필요

### 해결 방법

#### 옵션 A: 단계적 마이그레이션 (권장)

**1단계: 기본 프레임워크만 빌드 (완료됨)**
- iOS 프로젝트 구조 ✅
- DI 설정 ✅
- 기본 actual 구현 ✅

**2단계: Android 전용 코드 분리 (진행 필요)**
```kotlin
// Before (commonMain - 에러 발생)
val context = LocalContext.current

// After 1: expect/actual로 분리
expect fun getCurrentContext(): PlatformContext

// After 2: 또는 Android-specific으로 이동
// commonMain에서 제거하고 androidMain으로 이동
```

**3단계: UI 계층 수정**
- `LocalContext` → `rememberPlatformContext()` 또는 제거
- `Preview` → Android-only 파일로 이동
- `String.format()` → KMP 호환 함수로 교체

#### 옵션 B: 빠른 우회 방법

일단 빌드를 통과시키려면:

1. **문제가 있는 화면들을 일시적으로 Android-only로 처리:**
   ```kotlin
   // commonMain
   expect val isAndroid: Boolean

   @Composable
   fun DocumentScreen() {
       if (isAndroid) {
           // Android 화면
       } else {
           // iOS는 "준비 중" 메시지
           Text("iOS 버전 준비 중")
       }
   }
   ```

2. **DataStore Path 문제 해결:**
   ```kotlin
   // iosMain/util/DataStore.kt
   import okio.Path.Companion.toPath

   return PreferenceDataStoreFactory.createWithPath {
       producePath().toPath()  // String을 Path로 변환
   }
   ```

## 🔧 즉시 수정 가능한 코드

### 1. DataStore.kt 수정

<parameter>
```kotlin
// composeApp/src/iosMain/kotlin/unithon/helpjob/util/DataStore.kt
package unithon.helpjob.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
fun createDataStore(): DataStore<Preferences> {
    return androidx.datastore.preferences.core.PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null,
            )
            val path = requireNotNull(documentDirectory).path + "/app_prefs.preferences_pb"
            path.toPath()  // String을 Path로 변환
        }
    )
}
```
