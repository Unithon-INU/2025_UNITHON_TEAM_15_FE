package unithon.helpjob.ui.setting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.icerock.moko.resources.compose.stringResource
import dev.icerock.moko.resources.desc.desc
import org.koin.compose.viewmodel.koinViewModel
import unithon.helpjob.data.repository.GlobalLanguageState
import unithon.helpjob.data.repository.LanguageAwareScreen
import unithon.helpjob.resources.MR
import unithon.helpjob.ui.components.HelpJobTopAppBar
import unithon.helpjob.ui.main.HomeViewModel
import unithon.helpjob.ui.theme.Grey000
import unithon.helpjob.ui.theme.Grey200
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.title2
import unithon.helpjob.util.noRippleClickable

@Composable
fun LanguageSettingScreen(
    onBack: () -> Unit,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    viewModel: LanguageSettingViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel.snackbarMessage) {
        viewModel.snackbarMessage.collect { messageRes ->
            snackbarHostState.showSnackbar(
                message = messageRes.desc().toString(context)
            )
        }
    }

    LanguageAwareScreen{
        Column(
            modifier = modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            HelpJobTopAppBar(
                title = MR.strings.setting_app_language,
                onBack = onBack
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 14.dp, start = 20.dp, end = 20.dp)
            ) {
                // 현재 언어 섹션 헤더
                Text(
                    text = stringResource(MR.strings.language_setting_current_language),
                    style = MaterialTheme.typography.title2,
                    color = Grey600
                )

                Spacer(modifier = Modifier.height(9.dp))

                // 언어 선택 드롭다운
                LanguageDropdown(
                    items = uiState.availableLanguages,
                    selectedItem = uiState.currentLanguage,
                    onItemSelected = { language ->
                        viewModel.setLanguage(language)
                        // homeViewModel.refresh() 제거: HomeScreen LaunchedEffect가 자동 처리
                        GlobalLanguageState.updateLanguage(language)
                    },
                    itemToString = { it.displayName }
                )
            }
        }
    }
}

@Composable
private fun <T> LanguageDropdown(
    modifier: Modifier = Modifier,
    selectedItem: T?,
    items: List<T>,
    onItemSelected: (T) -> Unit,
    itemToString: (T) -> String
) {
    var expanded by remember { mutableStateOf(false) }
    var rowSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }

    Box(modifier = modifier) {
        // 드롭다운 박스 (전체 너비)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .onGloballyPositioned { layoutCoordinates ->
                    rowSize = layoutCoordinates.size.toSize()
                }
                .border(
                    width = 1.dp,
                    color = Grey200, // 고정 색상
                    shape = RoundedCornerShape(10.dp)
                )
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(10.dp)
                )
                .noRippleClickable { expanded = !expanded }
                .padding(start = 18.dp, end = 10.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedItem?.let(itemToString) ?: "",
                    style = MaterialTheme.typography.title2,
                    color = Grey600,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Grey600,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // DropdownMenu (텍스트 크기에 맞게, 오른쪽 정렬)
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.wrapContentWidth(), // 텍스트 크기에 맞게
            border = BorderStroke(
                width = 1.dp,
                color = Grey200,
            ),
            shape = RoundedCornerShape(10.dp),
            containerColor = Grey000,
            shadowElevation = 0.dp,
            offset = DpOffset(
                y = 6.dp,
                x = with(LocalDensity.current) {
                    (rowSize.width.toDp() - 110.dp) // 대략적인 오른쪽 정렬
                }
            )
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = itemToString(item),
                            style = MaterialTheme.typography.titleSmall,
                            color = Grey600
                        )
                    },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    },
                    contentPadding = PaddingValues(
                        vertical = 11.5.dp,
                        horizontal = 12.dp
                    )
                )
            }
        }
    }
}