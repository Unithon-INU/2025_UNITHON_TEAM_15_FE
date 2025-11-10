package unithon.helpjob.ui.onboarding.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.icerock.moko.resources.compose.stringResource
import androidx.compose.ui.unit.dp
import unithon.helpjob.R
import unithon.helpjob.resources.MR
import unithon.helpjob.ui.onboarding.OnboardingData
import unithon.helpjob.ui.onboarding.OnboardingPage

@Composable
fun LanguageSelectionPage(
    languageList: List<OnboardingData>,
    selectedLanguage: String,
    onLanguageSelect: (String) -> Unit
) {
    OnboardingPage(
        title = stringResource(MR.strings.onboarding_language_setup_title), // ✅ 직접 호출
        content = {
            languageList.forEachIndexed { index, language ->
                OnboardingButton(
                    modifier = Modifier
                        .height(46.dp)
                        .fillMaxWidth(),
                    mainTitle = language.mainTitle,
                    onClick = { onLanguageSelect(language.mainTitle) },
                    icon = R.drawable.ic_check,
                    enabled = selectedLanguage == language.mainTitle
                )
                if (index < languageList.size - 1) {
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
    )
}