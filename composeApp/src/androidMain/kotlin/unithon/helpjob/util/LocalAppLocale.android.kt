package unithon.helpjob.util

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

/**
 * Android 구현: Configuration.locale를 통한 언어 코드 관리
 *
 * JetBrains 공식 패턴 적용
 * 참고: https://kotlinlang.org/docs/multiplatform/compose-resource-environment.html
 */
actual object LocalAppLocale {
    /**
     * 현재 Configuration의 언어 코드 반환
     */
    actual val current: String
        @Composable
        get() {
            val configuration = LocalConfiguration.current
            return configuration.locales[0].language
        }

    /**
     * CompositionLocalProvider에 제공할 Configuration 생성
     *
     * @param value 변경할 언어 코드 (예: "ko", "en")
     */
    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val context = LocalContext.current
        val currentConfiguration = LocalConfiguration.current

        // value가 null이면 현재 Configuration 그대로 사용
        val targetLocale = value?.let { Locale.forLanguageTag(it) }
            ?: currentConfiguration.locales[0]

        // 새로운 Configuration 생성
        val newConfiguration = Configuration(currentConfiguration).apply {
            setLocale(targetLocale)
        }

        return LocalConfiguration provides newConfiguration
    }
}
