package unithon.helpjob.ui.document.page

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.document_basic_info_1_major_label
import helpjob.composeapp.generated.resources.document_basic_info_2_major_dropdown_placeholder
import helpjob.composeapp.generated.resources.document_basic_info_2_semester_label
import helpjob.composeapp.generated.resources.document_basic_info_2_semester_placeholder
import helpjob.composeapp.generated.resources.document_basic_info_2_university_label
import helpjob.composeapp.generated.resources.document_basic_info_2_university_placeholder
import helpjob.composeapp.generated.resources.search_normal
import helpjob.composeapp.generated.resources.searchable_dropdown_no_results
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import unithon.helpjob.data.model.Semester
import unithon.helpjob.data.model.response.MajorInfo
import unithon.helpjob.data.model.response.UniversityResponse
import unithon.helpjob.ui.components.CustomScrollbar
import unithon.helpjob.ui.components.HelpJobDropdown
import unithon.helpjob.ui.components.HelpJobTextField
import unithon.helpjob.ui.theme.Grey000
import unithon.helpjob.ui.theme.Grey200
import unithon.helpjob.ui.theme.Grey300
import unithon.helpjob.ui.theme.Grey400
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

            // 2. 학과/전공 선택 (검색 가능한 드롭다운)
            MajorSearchDropdown(
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

@Composable
private fun <T> MajorSearchDropdown(
    modifier: Modifier = Modifier,
    label: String = "",
    selectedItem: T?,
    items: List<T>,
    onItemSelected: (T) -> Unit,
    itemToString: (T) -> String,
    placeholder: String = "",
) {
    val focusManager = LocalFocusManager.current
    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var rowSize by remember { mutableStateOf(Size.Zero) }
    val scrollState = rememberScrollState()
    val isKeyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    val density = LocalDensity.current
    val popupPositionProvider = remember(isKeyboardVisible, density) {
        object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize
            ): IntOffset {
                val gap = with(density) { 6.dp.roundToPx() }
                return IntOffset(
                    x = anchorBounds.left,
                    y = if (isKeyboardVisible)
                        anchorBounds.top - popupContentSize.height - gap
                    else
                        anchorBounds.bottom + gap
                )
            }
        }
    }

    // 외부에서 selectedItem이 변경되면 (예: 대학 변경으로 학과 초기화) query 동기화
    LaunchedEffect(selectedItem) {
        if (!expanded) {
            query = selectedItem?.let { itemToString(it) } ?: ""
        }
    }

    val filteredItems = remember(items, query) {
        if (query.isBlank()) items
        else items.filter { itemToString(it).contains(query, ignoreCase = true) }
    }

    Column(modifier = modifier) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                color = Grey600
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Box {
            HelpJobTextField(
                value = query,
                onValueChange = { input ->
                    query = input
                    expanded = input.isNotEmpty()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .onGloballyPositioned { layoutCoordinates ->
                        rowSize = layoutCoordinates.size.toSize()
                    }
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused && !expanded) {
                            query = ""
                            // expanded = true 하지 않음: 타이핑해야 목록 표시
                        } else if (!focusState.isFocused) {
                            // 진짜 포커스 해제(외부 탭, 항목 선택): dropdown 닫고 선택 항목명 복원
                            expanded = false
                            query = selectedItem?.let { itemToString(it) } ?: ""
                        }
                    },
                placeholder = {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.titleSmall,
                        color = Grey300
                    )
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.search_normal),
                        contentDescription = null,
                        tint = if (selectedItem != null) Primary500 else Grey600,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )

            // PopupPositionProvider로 키보드 상태에 따라 정확히 6dp 간격 유지
            // (DropdownMenu 내부 DropdownMenuPositionProvider는 최소 gap 강제로 offset만으로는 제어 불가)
            if (expanded && items.isNotEmpty()) {
                Popup(
                    popupPositionProvider = popupPositionProvider,
                    onDismissRequest = {
                        expanded = false
                        // query 리셋 없음: 한글 IME가 onDismissRequest를 트리거해도 타이핑 중인 텍스트 보존
                        // query 복원은 onFocusChanged(unfocused)에서 처리
                    },
                    // focusable = false: TextField가 포커스를 유지하여 타이핑 가능
                    properties = PopupProperties(focusable = false)
                ) {
                    Surface(
                        modifier = Modifier
                            .width(with(LocalDensity.current) { rowSize.width.toDp() })
                            .heightIn(max = 240.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = Grey000,
                        border = BorderStroke(1.dp, Grey200),
                        shadowElevation = 0.dp,
                        tonalElevation = 0.dp
                    ) {
                        Column(modifier = Modifier.verticalScroll(scrollState)) {
                            if (filteredItems.isEmpty()) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = stringResource(Res.string.searchable_dropdown_no_results),
                                            style = MaterialTheme.typography.titleSmall,
                                            color = Grey400
                                        )
                                    },
                                    onClick = {},
                                    enabled = false,
                                    contentPadding = PaddingValues(
                                        vertical = 11.5.dp,
                                        horizontal = 12.dp
                                    )
                                )
                            } else {
                                filteredItems.forEach { item ->
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
                                            query = itemToString(item)
                                            expanded = false
                                            focusManager.clearFocus()
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
                }
            }

            // Custom Scrollbar
            if (expanded && filteredItems.size > 5) {
                val scrollbarOffset = with(LocalDensity.current) {
                    IntOffset(
                        x = (rowSize.width.toDp() + ((-10).dp)).toPx().toInt(),
                        y = if (isKeyboardVisible)
                            (-(6 + 235)).dp.toPx().toInt()   // upward: HelpJobDropDown isUpward 패턴과 동일
                        else
                            (6 + 46 + 5).dp.toPx().toInt()  // downward
                    )
                }

                Popup(offset = scrollbarOffset) {
                    CustomScrollbar(
                        scrollState = scrollState,
                        dropdownHeight = 230.dp,
                        totalItems = filteredItems.size,
                        visibleItems = 5
                    )
                }
            }
        }
    }
}
