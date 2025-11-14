package unithon.helpjob.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue

/**
 * JetBrains 공식 패턴: 런타임 언어 변경을 위한 expect/actual 인터페이스
 *
 * 참고: https://kotlinlang.org/docs/multiplatform/compose-resource-environment.html
 */
expect object LocalAppLocale {
    /**
     * 현재 설정된 언어 코드 (예: "ko", "en")
     */
    val current: String @Composable get

    /**
     * CompositionLocalProvider에 제공할 값 생성
     *
     * @param value 변경할 언어 코드 (null이면 현재 값 유지)
     */
    @Composable
    infix fun provides(value: String?): ProvidedValue<*>
}
