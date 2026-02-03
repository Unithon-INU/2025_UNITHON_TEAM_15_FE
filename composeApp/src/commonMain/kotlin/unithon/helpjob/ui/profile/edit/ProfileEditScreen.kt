package unithon.helpjob.ui.profile.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.onboarding_business_setup_title
import helpjob.composeapp.generated.resources.onboarding_done_button
import helpjob.composeapp.generated.resources.onboarding_language_level_setup_title
import helpjob.composeapp.generated.resources.onboarding_next_button
import helpjob.composeapp.generated.resources.onboarding_top_bar_title
import helpjob.composeapp.generated.resources.onboarding_track_setup_title
import helpjob.composeapp.generated.resources.onboarding_visa_setup_d2_description
import helpjob.composeapp.generated.resources.onboarding_visa_setup_d2_title
import helpjob.composeapp.generated.resources.onboarding_visa_setup_d4_description
import helpjob.composeapp.generated.resources.onboarding_visa_setup_d4_title
import helpjob.composeapp.generated.resources.onboarding_visa_setup_title
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import unithon.helpjob.data.model.Business
import unithon.helpjob.data.model.EnglishLevel
import unithon.helpjob.data.model.LanguageTrack
import unithon.helpjob.data.model.ProfileField
import unithon.helpjob.data.model.TopikLevel
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.components.HelpJobTopAppBar
import unithon.helpjob.ui.onboarding.PlatformBackHandler
import unithon.helpjob.ui.onboarding.components.OnboardingCard
import unithon.helpjob.ui.theme.Grey700

@Composable
fun ProfileEditScreen(
    viewModel: ProfileEditViewModel,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onSaveSuccess()
        }
    }

    LaunchedEffect(viewModel.snackbarMessage) {
        viewModel.snackbarMessage.collect { messageRes ->
            snackbarHostState.showSnackbar(
                message = getString(messageRes)
            )
        }
    }

    val onBackAction: () -> Unit = {
        if (uiState.profileField == ProfileField.LANGUAGE_LEVEL && !uiState.isTrackStep) {
            viewModel.goBackToTrackStep()
        } else {
            onBack()
        }
    }

    PlatformBackHandler(enabled = true, onBack = onBackAction)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        HelpJobTopAppBar(
            title = Res.string.onboarding_top_bar_title,
            onBack = onBackAction
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopCenter)
            ) {
                Spacer(Modifier.height(44.dp))

                Text(
                    text = stringResource(getPageTitle(uiState)),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 22.sp,
                        lineHeight = 32.sp
                    ),
                    color = Grey700
                )
                Spacer(Modifier.height(39.dp))

                when (uiState.profileField) {
                    ProfileField.VISA_TYPE -> VisaEditContent(
                        selectedVisa = uiState.selectedVisa,
                        onVisaSelected = viewModel::selectVisa
                    )
                    ProfileField.LANGUAGE_LEVEL -> {
                        if (uiState.isTrackStep) {
                            TrackEditContent(
                                selectedTrack = uiState.selectedTrack,
                                onTrackSelected = viewModel::selectTrack
                            )
                        } else {
                            when (uiState.selectedTrack) {
                                LanguageTrack.KOREAN -> KoreanLevelEditContent(
                                    selectedLevel = uiState.selectedTopikLevel,
                                    onLevelSelected = viewModel::selectTopikLevel
                                )
                                LanguageTrack.ENGLISH -> EnglishLevelEditContent(
                                    selectedLevel = uiState.selectedEnglishLevel,
                                    onLevelSelected = viewModel::selectEnglishLevel
                                )
                                null -> { /* 트랙 미선택 시 도달 불가 */ }
                            }
                        }
                    }
                    ProfileField.INDUSTRY -> BusinessEditContent(
                        selectedBusinesses = uiState.selectedBusinesses,
                        onBusinessToggle = viewModel::toggleBusiness
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                val isLanguageTrackStep = uiState.profileField == ProfileField.LANGUAGE_LEVEL && uiState.isTrackStep

                HelpJobButton(
                    text = stringResource(
                        if (isLanguageTrackStep) Res.string.onboarding_next_button
                        else Res.string.onboarding_done_button
                    ),
                    onClick = {
                        if (isLanguageTrackStep) {
                            viewModel.proceedToLevelStep()
                        } else {
                            viewModel.save()
                        }
                    },
                    enabled = if (isLanguageTrackStep) uiState.isTrackValid else uiState.isValid,
                    isLoading = uiState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun getPageTitle(uiState: ProfileEditUiState) = when (uiState.profileField) {
    ProfileField.VISA_TYPE -> Res.string.onboarding_visa_setup_title
    ProfileField.LANGUAGE_LEVEL -> {
        if (uiState.isTrackStep) Res.string.onboarding_track_setup_title
        else Res.string.onboarding_language_level_setup_title
    }
    ProfileField.INDUSTRY -> Res.string.onboarding_business_setup_title
}

@Composable
private fun VisaEditContent(
    selectedVisa: String,
    onVisaSelected: (String) -> Unit
) {
    val visaOptions = listOf(
        stringResource(Res.string.onboarding_visa_setup_d2_title) to
                stringResource(Res.string.onboarding_visa_setup_d2_description),
        stringResource(Res.string.onboarding_visa_setup_d4_title) to
                stringResource(Res.string.onboarding_visa_setup_d4_description)
    )

    visaOptions.forEachIndexed { index, (mainTitle, subTitle) ->
        OnboardingCard(
            modifier = Modifier
                .height(62.dp)
                .fillMaxWidth(),
            mainTitle = mainTitle,
            subTitle = subTitle,
            onClick = { onVisaSelected(mainTitle) },
            contentPosition = Arrangement.Center,
            enabled = selectedVisa == mainTitle
        )
        if (index < visaOptions.size - 1) {
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

@Composable
private fun TrackEditContent(
    selectedTrack: LanguageTrack?,
    onTrackSelected: (LanguageTrack) -> Unit
) {
    LanguageTrack.entries.forEachIndexed { index, track ->
        val displayName = track.getDisplayName()
        OnboardingCard(
            modifier = Modifier
                .height(62.dp)
                .fillMaxWidth(),
            mainTitle = displayName,
            onClick = { onTrackSelected(track) },
            contentPosition = Arrangement.Center,
            enabled = selectedTrack == track
        )
        if (index < LanguageTrack.entries.size - 1) {
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

@Composable
private fun KoreanLevelEditContent(
    selectedLevel: TopikLevel?,
    onLevelSelected: (TopikLevel) -> Unit
) {
    TopikLevel.entries.forEachIndexed { index, level ->
        val displayName = level.getDisplayName()
        OnboardingCard(
            modifier = Modifier
                .height(62.dp)
                .fillMaxWidth(),
            mainTitle = displayName,
            onClick = { onLevelSelected(level) },
            contentPosition = Arrangement.Center,
            enabled = selectedLevel == level
        )
        if (index < TopikLevel.entries.size - 1) {
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

@Composable
private fun EnglishLevelEditContent(
    selectedLevel: EnglishLevel?,
    onLevelSelected: (EnglishLevel) -> Unit
) {
    EnglishLevel.entries.forEachIndexed { index, level ->
        val displayName = level.getDisplayName()
        OnboardingCard(
            modifier = Modifier
                .height(62.dp)
                .fillMaxWidth(),
            mainTitle = displayName,
            onClick = { onLevelSelected(level) },
            contentPosition = Arrangement.Center,
            enabled = selectedLevel == level
        )
        if (index < EnglishLevel.entries.size - 1) {
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

@Composable
private fun BusinessEditContent(
    selectedBusinesses: List<Business>,
    onBusinessToggle: (Business) -> Unit
) {
    LazyColumn {
        itemsIndexed(Business.entries) { index, business ->
            val displayName = business.getDisplayName()
            OnboardingCard(
                modifier = Modifier
                    .height(46.dp)
                    .fillMaxWidth(),
                mainTitle = displayName,
                onClick = { onBusinessToggle(business) },
                enabled = business in selectedBusinesses
            )
            if (index < Business.entries.size - 1) {
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}
