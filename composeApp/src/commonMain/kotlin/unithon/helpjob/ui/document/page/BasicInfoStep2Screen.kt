package unithon.helpjob.ui.document.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.document_basic_info_1_major_label
import helpjob.composeapp.generated.resources.document_basic_info_1_major_placeholder
import helpjob.composeapp.generated.resources.document_basic_info_2_semester_label
import helpjob.composeapp.generated.resources.document_basic_info_2_semester_placeholder
import org.jetbrains.compose.resources.stringResource
import unithon.helpjob.data.model.Semester
import unithon.helpjob.ui.components.HelpJobDropdown
import unithon.helpjob.ui.document.components.DocumentTextTextField

@Composable
fun BasicInfoStep2Screen(
    modifier: Modifier = Modifier,
    step: Int,
    title: String,
    majorValue: String,
    onMajorValueChange: (String) -> Unit,
    semesterValue: Semester?,
    onSemesterValueChange: (Semester) -> Unit,
    enabled: Boolean,
    onNext: () -> Unit
){
    val semesterList = Semester.entries
    val semesterDisplayMapUpdated = semesterList.associateWith { it.getDisplayName() }

    DocumentInfoScreen(
        modifier = modifier,
        step = step,
        title = title,
        enabled = enabled,
        onNext = onNext
    ) {
        Column {
            DocumentTextTextField(
                value = majorValue,
                onValueChange = onMajorValueChange,
                labelText = stringResource(Res.string.document_basic_info_1_major_label),
                placeholderText = stringResource(Res.string.document_basic_info_1_major_placeholder),
                imeAction = ImeAction.Done,
                onImeAction = if (enabled) onNext else null
            )
            Spacer(Modifier.height(27.dp))
            HelpJobDropdown(
                selectedItem = semesterValue,
                items = semesterList,
                onItemSelected = onSemesterValueChange,
                label = stringResource(Res.string.document_basic_info_2_semester_label),
                placeholder = stringResource(Res.string.document_basic_info_2_semester_placeholder),
                itemToString = { semester ->
                    semesterDisplayMapUpdated[semester] ?: ""
                }
            )
        }
    }
}