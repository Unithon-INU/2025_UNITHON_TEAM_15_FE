package unithon.helpjob.ui.onboarding.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.ic_check
import helpjob.composeapp.generated.resources.onboarding_language_setup_title
import org.jetbrains.compose.resources.stringResource
import unithon.helpjob.ui.onboarding.OnboardingData
import unithon.helpjob.ui.onboarding.OnboardingPage

@Composable
fun LanguageSelectionPage(
    languageList: List<OnboardingData>,
    selectedLanguage: String,
    onLanguageSelect: (String) -> Unit
) {
    OnboardingPage(
        title = stringResource(Res.string.onboarding_language_setup_title), // ✅ 직접 호출
        content = {
            languageList.forEachIndexed { index, language ->
                OnboardingButton(
                    modifier = Modifier
                        .height(46.dp)
                        .fillMaxWidth(),
                    mainTitle = language.mainTitle,
                    onClick = { onLanguageSelect(language.mainTitle) },
                    icon = Res.drawable.ic_check,
                    enabled = selectedLanguage == language.mainTitle
                )
                if (index < languageList.size - 1) {
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
    )
}