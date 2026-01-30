package unithon.helpjob.data.model

import androidx.compose.runtime.Composable
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.onboarding_track_english
import helpjob.composeapp.generated.resources.onboarding_track_korean
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * 언어 트랙 관리 Enum
 * - 한국어 트랙 / 영어 트랙
 */
enum class LanguageTrack(
    val displayNameRes: StringResource
) {
    KOREAN(displayNameRes = Res.string.onboarding_track_korean),
    ENGLISH(displayNameRes = Res.string.onboarding_track_english);

    @Composable
    fun getDisplayName(): String {
        return stringResource(displayNameRes)
    }
}
