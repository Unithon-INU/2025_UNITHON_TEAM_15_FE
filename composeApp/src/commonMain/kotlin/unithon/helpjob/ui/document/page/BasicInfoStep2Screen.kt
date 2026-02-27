package unithon.helpjob.ui.document.page

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.document_basic_info_1_major_label
import helpjob.composeapp.generated.resources.document_basic_info_1_major_placeholder
import helpjob.composeapp.generated.resources.document_basic_info_2_major_dropdown_placeholder
import helpjob.composeapp.generated.resources.document_basic_info_2_semester_label
import helpjob.composeapp.generated.resources.document_basic_info_2_semester_placeholder
import helpjob.composeapp.generated.resources.document_basic_info_2_university_label
import helpjob.composeapp.generated.resources.document_basic_info_2_university_placeholder
import helpjob.composeapp.generated.resources.document_university_accredited
import helpjob.composeapp.generated.resources.document_university_accredited_hint
import helpjob.composeapp.generated.resources.document_university_not_accredited
import helpjob.composeapp.generated.resources.document_university_not_accredited_proceed
import helpjob.composeapp.generated.resources.document_university_search_placeholder
import helpjob.composeapp.generated.resources.document_university_type_associate
import helpjob.composeapp.generated.resources.document_university_type_bachelor
import helpjob.composeapp.generated.resources.document_university_type_graduate
import helpjob.composeapp.generated.resources.document_university_type_label
import helpjob.composeapp.generated.resources.ic_check
import helpjob.composeapp.generated.resources.search_normal
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import unithon.helpjob.data.model.AppLanguage
import unithon.helpjob.data.model.Semester
import unithon.helpjob.data.model.response.UniversityInfo
import unithon.helpjob.data.repository.GlobalLanguageState
import unithon.helpjob.ui.components.HelpJobDropdown
import unithon.helpjob.ui.components.HelpJobTextField
import unithon.helpjob.ui.theme.Grey000
import unithon.helpjob.ui.theme.Grey200
import unithon.helpjob.ui.theme.Grey300
import unithon.helpjob.ui.theme.Grey400
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.Grey700
import unithon.helpjob.ui.theme.Primary500
import unithon.helpjob.util.noRippleClickable

// TODO[LEGACY]: 기존 대학 검색 API 기반 임포트 — 재통합 시 해제
// import androidx.compose.foundation.BorderStroke
// import androidx.compose.foundation.layout.width
// import androidx.compose.foundation.layout.WindowInsets
// import androidx.compose.foundation.layout.ime
// import androidx.compose.foundation.rememberScrollState
// import androidx.compose.foundation.verticalScroll
// import androidx.compose.foundation.text.KeyboardActions
// import androidx.compose.foundation.text.KeyboardOptions
// import androidx.compose.material3.DropdownMenu
// import androidx.compose.ui.focus.onFocusChanged
// import androidx.compose.ui.geometry.Size
// import androidx.compose.ui.layout.onGloballyPositioned
// import androidx.compose.ui.platform.LocalFocusManager
// import androidx.compose.ui.platform.LocalSoftwareKeyboardController
// import androidx.compose.ui.text.input.ImeAction
// import androidx.compose.ui.unit.IntOffset
// import androidx.compose.ui.unit.IntRect
// import androidx.compose.ui.unit.IntSize
// import androidx.compose.ui.unit.LayoutDirection
// import androidx.compose.ui.unit.toSize
// import androidx.compose.ui.window.Popup
// import androidx.compose.ui.window.PopupPositionProvider
// import androidx.compose.ui.window.PopupProperties
// import unithon.helpjob.ui.components.CustomScrollbar
// import unithon.helpjob.data.model.response.MajorInfo
// import unithon.helpjob.data.model.response.UniversityResponse
// import org.jetbrains.compose.resources.StringResource
// import helpjob.composeapp.generated.resources.document_basic_info_2_major_dropdown_placeholder
// import helpjob.composeapp.generated.resources.searchable_dropdown_no_results
// import unithon.helpjob.ui.theme.Warning

@Composable
fun BasicInfoStep2Screen(
    modifier: Modifier = Modifier,
    step: Int,
    title: String,
    // 인증대 여부 + 대학 검색 다이얼로그
    isAccredited: Boolean?,
    universityName: String?,
    universityType: String?,
    isUniversitySearchDialogOpen: Boolean,
    filteredUniversities: List<UniversityInfo>,
    universityFilterQuery: String,
    isAccreditedUniversitiesLoading: Boolean,
    onOpenUniversitySearchDialog: () -> Unit,
    onCloseUniversitySearchDialog: () -> Unit,
    onUpdateUniversityFilterQuery: (String) -> Unit,
    onSelectAccreditedUniversity: (UniversityInfo) -> Unit,
    onSelectNonAccredited: () -> Unit,
    // 대학 종류
    onUniversityTypeChange: (String) -> Unit,
    // 학과 직접 입력
    major: String,
    onMajorChange: (String) -> Unit,
    // 이수학기
    semesterItems: List<Semester>,
    semesterValue: Semester?,
    onSemesterValueChange: (Semester) -> Unit,
    // 공통
    enabled: Boolean,
    onNext: () -> Unit
) {
    val semesterDisplayMap = semesterItems.associateWith { it.getDisplayName() }
    val universityTypeItems = listOf("BACHELOR", "ASSOCIATE", "GRADUATE")
    val universityTypeDisplayMap = mapOf(
        "BACHELOR" to stringResource(Res.string.document_university_type_bachelor),
        "ASSOCIATE" to stringResource(Res.string.document_university_type_associate),
        "GRADUATE" to stringResource(Res.string.document_university_type_graduate)
    )

    // 대학 검색 다이얼로그
    if (isUniversitySearchDialogOpen) {
        UniversitySearchDialog(
            filterQuery = universityFilterQuery,
            onFilterQueryChange = onUpdateUniversityFilterQuery,
            filteredUniversities = filteredUniversities,
            isLoading = isAccreditedUniversitiesLoading,
            onSelectUniversity = onSelectAccreditedUniversity,
            onSelectNonAccredited = onSelectNonAccredited,
            onDismiss = onCloseUniversitySearchDialog,
            universityTypeDisplayMap = universityTypeDisplayMap
        )
    }

    DocumentInfoScreen(
        modifier = modifier,
        step = step,
        title = title,
        enabled = enabled,
        contentPadding = 20.dp,
        onNext = onNext
    ) {
        Column {
            // 1. 대학교 선택 (인증대 여부)
            Column {
                Text(
                    text = stringResource(Res.string.document_basic_info_2_university_label),
                    style = MaterialTheme.typography.titleSmall,
                    color = Grey600,
                    modifier = Modifier.padding(bottom = 9.dp)
                )

                // HelpJobTextField와 동일 스펙: 46dp 높이, 10dp 모서리, 1dp 테두리
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .background(Grey000, RoundedCornerShape(10.dp))
                        .border(
                            width = 1.dp,
                            color = Grey200,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .noRippleClickable { onOpenUniversitySearchDialog() }
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when {
                                isAccredited == true && universityName != null ->
                                    "${stringResource(Res.string.document_university_accredited)} • $universityName"
                                isAccredited == false ->
                                    stringResource(Res.string.document_university_not_accredited)
                                else ->
                                    stringResource(Res.string.document_basic_info_2_university_placeholder)
                            },
                            style = MaterialTheme.typography.titleSmall,
                            color = if (isAccredited != null) Grey700 else Grey300,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            painter = painterResource(
                                if (isAccredited != null) Res.drawable.ic_check else Res.drawable.search_normal
                            ),
                            contentDescription = null,
                            tint = if (isAccredited != null) Primary500 else Grey600,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // 안내 문구
                Text(
                    text = stringResource(Res.string.document_university_accredited_hint),
                    color = Primary500,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            // 2. 대학 종류 드롭다운
            HelpJobDropdown(
                selectedItem = universityType,
                items = universityTypeItems,
                onItemSelected = onUniversityTypeChange,
                label = stringResource(Res.string.document_university_type_label),
                placeholder = stringResource(Res.string.document_basic_info_2_major_dropdown_placeholder),
                itemToString = { universityTypeDisplayMap[it] ?: it }
            )

            Spacer(Modifier.height(20.dp))

            // 3. 학과 직접 입력
            Column {
                Text(
                    text = stringResource(Res.string.document_basic_info_1_major_label),
                    style = MaterialTheme.typography.titleSmall,
                    color = Grey600,
                    modifier = Modifier.padding(bottom = 9.dp)
                )
                HelpJobTextField(
                    value = major,
                    onValueChange = onMajorChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    placeholder = {
                        Text(
                            text = stringResource(Res.string.document_basic_info_1_major_placeholder),
                            style = MaterialTheme.typography.titleSmall,
                            color = Grey300
                        )
                    }
                )
            }

            Spacer(Modifier.height(20.dp))

            // 4. 이수학기 선택
            HelpJobDropdown(
                selectedItem = semesterValue,
                items = semesterItems,
                onItemSelected = onSemesterValueChange,
                label = stringResource(Res.string.document_basic_info_2_semester_label),
                placeholder = stringResource(Res.string.document_basic_info_2_semester_placeholder),
                itemToString = { semester ->
                    semesterDisplayMap[semester] ?: ""
                }
            )
        }
    }
}

@Composable
private fun UniversitySearchDialog(
    filterQuery: String,
    onFilterQueryChange: (String) -> Unit,
    filteredUniversities: List<UniversityInfo>,
    isLoading: Boolean,
    onSelectUniversity: (UniversityInfo) -> Unit,
    onSelectNonAccredited: () -> Unit,
    onDismiss: () -> Unit,
    universityTypeDisplayMap: Map<String, String>
) {
    val language by GlobalLanguageState.currentLanguage

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            color = Grey000,
            shadowElevation = 8.dp
        ) {
            Column {
                // 검색창
                HelpJobTextField(
                    value = filterQuery,
                    onValueChange = onFilterQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .height(46.dp),
                    placeholder = {
                        Text(
                            text = stringResource(Res.string.document_university_search_placeholder),
                            style = MaterialTheme.typography.titleSmall,
                            color = Grey300
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.search_normal),
                            contentDescription = null,
                            tint = Grey600,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )

                HorizontalDivider(color = Grey200, thickness = 1.dp)

                // 목록 or 로딩
                Box(modifier = Modifier.heightIn(max = 320.dp)) {
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(32.dp),
                                color = Primary500,
                                strokeWidth = 2.dp
                            )
                        }
                    } else {
                        LazyColumn {
                            items(filteredUniversities) { university ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(
                                                text = if (language == AppLanguage.KOREAN) university.nameKo else university.nameEn,
                                                style = MaterialTheme.typography.titleSmall,
                                                color = Grey600
                                            )
                                            Text(
                                                text = universityTypeDisplayMap[university.universityType]
                                                    ?: university.universityType,
                                                style = MaterialTheme.typography.labelMedium,
                                                color = Grey400
                                            )
                                        }
                                    },
                                    onClick = { onSelectUniversity(university) },
                                    contentPadding = PaddingValues(
                                        vertical = 11.5.dp,
                                        horizontal = 12.dp
                                    )
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(color = Grey200, thickness = 1.dp)

                // 비인증대로 진행 버튼
                TextButton(
                    onClick = onSelectNonAccredited,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.document_university_not_accredited_proceed),
                        color = Primary500,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}

// TODO[LEGACY]: 기존 MajorSearchDropdown — 대학 검색 API 재통합 시 해제
// @Composable
// private fun <T> MajorSearchDropdown(
//     modifier: Modifier = Modifier,
//     label: String = "",
//     selectedItem: T?,
//     items: List<T>,
//     onItemSelected: (T) -> Unit,
//     itemToString: (T) -> String,
//     placeholder: String = "",
// ) { ... }
