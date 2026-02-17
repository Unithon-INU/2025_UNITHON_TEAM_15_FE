package unithon.helpjob.ui.document.page

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.document_basic_info_1_major_label
import helpjob.composeapp.generated.resources.document_basic_info_2_major_dropdown_placeholder
import helpjob.composeapp.generated.resources.document_basic_info_2_semester_label
import helpjob.composeapp.generated.resources.document_basic_info_2_semester_placeholder
import helpjob.composeapp.generated.resources.document_basic_info_2_university_label
import helpjob.composeapp.generated.resources.document_basic_info_2_university_placeholder
import helpjob.composeapp.generated.resources.search_normal
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import unithon.helpjob.data.model.Semester
import unithon.helpjob.data.model.response.MajorInfo
import unithon.helpjob.data.model.response.UniversityResponse
import unithon.helpjob.ui.components.HelpJobDropdown
import unithon.helpjob.ui.components.HelpJobTextField
import unithon.helpjob.ui.theme.Grey000
import unithon.helpjob.ui.theme.Grey200
import unithon.helpjob.ui.theme.Grey300
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.Primary500
import unithon.helpjob.ui.theme.Warning
import unithon.helpjob.util.noRippleClickable

@Composable
fun BasicInfoStep2Screen(
    modifier: Modifier = Modifier,
    step: Int,
    title: String,
    // University
    universityQuery: String,
    onUniversityQueryChange: (String) -> Unit,
    onUniversitySearch: () -> Unit,
    isUniversitySearching: Boolean,
    universityName: String?,
    universitySearchResults: List<UniversityResponse> = emptyList(),
    onUniversitySelected: (UniversityResponse) -> Unit,
    onDismissUniversityResults: () -> Unit,
    universitySearchError: Boolean = false,
    universitySearchErrorMessage: StringResource? = null,
    // Major
    majorItems: List<MajorInfo>,
    selectedMajor: String?,
    onMajorSelected: (MajorInfo) -> Unit,
    // Semester
    semesterItems: List<Semester>,
    semesterValue: Semester?,
    onSemesterValueChange: (Semester) -> Unit,
    // Common
    enabled: Boolean,
    onNext: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val semesterDisplayMap = semesterItems.associateWith { it.getDisplayName() }

    DocumentInfoScreen(
        modifier = modifier,
        step = step,
        title = title,
        enabled = enabled,
        onNext = onNext
    ) {
        Column {
            // 1. 대학교 검색
            Column {
                Text(
                    text = stringResource(Res.string.document_basic_info_2_university_label),
                    style = MaterialTheme.typography.titleSmall,
                    color = Grey600,
                    modifier = Modifier.padding(bottom = 9.dp)
                )

                var textFieldSize by remember { mutableStateOf(Size.Zero) }

                Box {
                    HelpJobTextField(
                        value = universityQuery,
                        onValueChange = onUniversityQueryChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .onGloballyPositioned { layoutCoordinates ->
                                textFieldSize = layoutCoordinates.size.toSize()
                            },
                        placeholder = {
                            Text(
                                text = stringResource(Res.string.document_basic_info_2_university_placeholder),
                                style = MaterialTheme.typography.titleSmall,
                                color = Grey300
                            )
                        },
                        trailingIcon = {
                            if (isUniversitySearching) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                    color = Primary500
                                )
                            } else {
                                Icon(
                                    painter = painterResource(Res.drawable.search_normal),
                                    contentDescription = null,
                                    tint = if (universityName != null) Primary500 else Grey600,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .noRippleClickable {
                                            keyboardController?.hide()
                                            onUniversitySearch()
                                        }
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                keyboardController?.hide()
                                onUniversitySearch()
                            }
                        ),
                        isError = universitySearchError
                    )

                    // 검색 결과 드롭다운 (검색 필드 바로 아래 자동 펼침)
                    DropdownMenu(
                        expanded = universitySearchResults.isNotEmpty(),
                        onDismissRequest = { onDismissUniversityResults() },
                        modifier = Modifier
                            .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                            .heightIn(max = 240.dp),
                        border = BorderStroke(
                            width = 1.dp,
                            color = Grey200
                        ),
                        shape = RoundedCornerShape(10.dp),
                        containerColor = Grey000,
                        shadowElevation = 0.dp,
                        offset = DpOffset(x = 0.dp, y = 6.dp)
                    ) {
                        universitySearchResults.forEach { university ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = university.university,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = Grey600
                                    )
                                },
                                onClick = {
                                    focusManager.clearFocus()
                                    onUniversitySelected(university)
                                },
                                contentPadding = PaddingValues(
                                    vertical = 11.5.dp,
                                    horizontal = 12.dp
                                )
                            )
                        }
                    }
                }

                // 에러 메시지 (TextField 하단)
                if (universitySearchError && universitySearchErrorMessage != null) {
                    Text(
                        text = stringResource(universitySearchErrorMessage),
                        color = Warning,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(Modifier.height(27.dp))

            // 2. 학과/전공 선택 (드롭다운)
            HelpJobDropdown(
                selectedItem = majorItems.find { it.major == selectedMajor },
                items = majorItems,
                onItemSelected = onMajorSelected,
                label = stringResource(Res.string.document_basic_info_1_major_label),
                placeholder = stringResource(Res.string.document_basic_info_2_major_dropdown_placeholder),
                itemToString = { it.major }
            )

            Spacer(Modifier.height(27.dp))

            // 3. 이수학기 선택 (드롭다운)
            HelpJobDropdown(
                selectedItem = semesterValue,
                items = semesterItems,
                onItemSelected = onSemesterValueChange,
                label = stringResource(Res.string.document_basic_info_2_semester_label),
                placeholder = stringResource(Res.string.document_basic_info_2_semester_placeholder),
                itemToString = { semester ->
                    semesterDisplayMap[semester] ?: ""
                },
                isUpward = true
            )
        }
    }
}
