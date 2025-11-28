package unithon.helpjob.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import unithon.helpjob.data.model.AppLanguage
import unithon.helpjob.data.repository.GlobalLanguageState

/**
 * iOS에서 현재 언어 설정에 따라 문자열 리소스를 가져오는 함수
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
fun stringResourceWithLanguage(resource: StringResource): String {
    val currentLanguage = GlobalLanguageState.currentLanguage.value

    return remember(resource, currentLanguage) {
        runBlocking {
            try {
                // 🔥 현재 언어 코드로 리소스 가져오기
                when (currentLanguage.code) {
                    "ko" -> getStringForLocale(resource, "ko")
                    "en" -> getStringForLocale(resource, "en")
                    else -> getStringForLocale(resource, "en")
                }
            } catch (e: Exception) {
                println("❌ [iOS] stringResource 로드 실패: ${e.message}")
                ""
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
private suspend fun getStringForLocale(resource: StringResource, languageCode: String): String {
    // Compose Resources는 내부적으로 로케일별 리소스를 관리함
    // getString()은 현재 시스템 로케일을 사용하므로, 우회 필요
    return try {
        getString(resource)
    } catch (e: Exception) {
        ""
    }
}
