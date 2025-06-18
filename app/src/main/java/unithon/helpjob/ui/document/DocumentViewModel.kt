package unithon.helpjob.ui.document

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import unithon.helpjob.data.model.Semester
import unithon.helpjob.data.model.WorkDay
import javax.inject.Inject

@HiltViewModel
class DocumentViewModel @Inject constructor(

): ViewModel() {

    private val _uiState = MutableStateFlow(DocumentUiState())
    val uiState: StateFlow<DocumentUiState> = _uiState.asStateFlow()

    // 기본 정보 입력 함수들 (VisualTransformation 사용으로 숫자만 저장)
    fun updateName(input: String) {
        _uiState.value = _uiState.value.copy(name = input)
    }

    fun updateForeignerNumber(input: String) {
        // 숫자만 추출해서 저장 (VisualTransformation이 포맷팅 처리)
        val numbersOnly = input.filter { it.isDigit() }.take(13)
        _uiState.value = _uiState.value.copy(foreignerNumber = numbersOnly)
    }

    fun updateMajor(input: String) {
        _uiState.value = _uiState.value.copy(major = input)
    }

    // Semester enum 사용
    fun updateSemester(semester: Semester) {
        _uiState.value = _uiState.value.copy(semester = semester)
    }

    fun updatePhoneNumber(input: String) {
        // 숫자만 추출해서 저장 (VisualTransformation이 포맷팅 처리)
        val numbersOnly = input.filter { it.isDigit() }.take(11)
        _uiState.value = _uiState.value.copy(phoneNumber = numbersOnly)
    }

    fun updateEmailAddress(input: String) {
        _uiState.value = _uiState.value.copy(emailAddress = input)
    }

    // 회사 정보 입력 함수들
    fun updateCompanyName(input: String) {
        _uiState.value = _uiState.value.copy(companyName = input)
    }

    fun updateBusinessRegisterNumber(input: String) {
        // 숫자만 추출해서 저장 (VisualTransformation이 포맷팅 처리)
        val numbersOnly = input.filter { it.isDigit() }.take(10)
        _uiState.value = _uiState.value.copy(businessRegisterNumber = numbersOnly)
    }

    fun updateCategoryOfBusiness(input: String) {
        _uiState.value = _uiState.value.copy(categoryOfBusiness = input)
    }

    fun updateAddressOfCompany(input: String) {
        _uiState.value = _uiState.value.copy(addressOfCompany = input)
    }

    fun updateEmployerName(input: String) {
        _uiState.value = _uiState.value.copy(employerName = input)
    }

    fun updateEmployerPhoneNumber(input: String) {
        // 숫자만 추출해서 저장 (VisualTransformation이 포맷팅 처리)
        val numbersOnly = input.filter { it.isDigit() }.take(11)
        _uiState.value = _uiState.value.copy(employerPhoneNumber = numbersOnly)
    }

    // 근무 조건 입력 함수들
    fun updateHourlyWage(input: String) {
        val numbersOnly = input.filter { it.isDigit() }
        _uiState.value = _uiState.value.copy(hourlyWage = numbersOnly)
    }

    fun updateWorkStartYear(input: String) {
        val numbersOnly = input.filter { it.isDigit() }.take(4)
        _uiState.value = _uiState.value.copy(workStartYear = numbersOnly)
    }

    fun updateWorkStartMonth(input: String) {
        val numbersOnly = input.filter { it.isDigit() }.take(2)
        _uiState.value = _uiState.value.copy(workStartMonth = numbersOnly)
    }

    fun updateWorkStartDay(input: String) {
        val numbersOnly = input.filter { it.isDigit() }.take(2)
        _uiState.value = _uiState.value.copy(workStartDay = numbersOnly)
    }

    fun updateWorkEndYear(input: String) {
        val numbersOnly = input.filter { it.isDigit() }.take(4)
        _uiState.value = _uiState.value.copy(workEndYear = numbersOnly)
    }

    fun updateWorkEndMonth(input: String) {
        val numbersOnly = input.filter { it.isDigit() }.take(2)
        _uiState.value = _uiState.value.copy(workEndMonth = numbersOnly)
    }

    fun updateWorkEndDay(input: String) {
        val numbersOnly = input.filter { it.isDigit() }.take(2)
        _uiState.value = _uiState.value.copy(workEndDay = numbersOnly)
    }

    // 🆕 WorkDay enum 사용
    fun updateWorkDay(selectedDay: WorkDay) {
        _uiState.update { currentState ->
            if (currentState.workDay.contains(selectedDay)) {
                // 이미 있으면 제거
                currentState.copy(workDay = currentState.workDay - selectedDay)
            } else {
                // 없으면 추가
                currentState.copy(workDay = currentState.workDay + selectedDay)
            }
        }
    }

    fun updateWorkStartTime(input: String) {
        _uiState.value = _uiState.value.copy(workStartTime = input)
    }

    fun updateWorkEndTime(input: String) {
        _uiState.value = _uiState.value.copy(workEndTime = input)
    }

    fun resetUiState(){
        _uiState.update {
            DocumentUiState()
        }
    }

    fun submitDocument() {
        // 서류 제출 로직
        if (_uiState.value.isAllValid) {
            // 서버 전송 시 API 값 사용
            val semesterApiValue = _uiState.value.semester?.apiValue ?: ""
            val workDayApiValue = WorkDay.toApiValues(_uiState.value.workDay)

            // TODO: 서버 API 호출
            // MemberDocumentRequest(
            //     semester = semesterApiValue,
            //     workDays = workDayApiValue,
            //     ...
            // )
        }
    }

    data class DocumentUiState(
        val name: String = "",
        val foreignerNumber: String = "", // 숫자만 저장 (예: "1234567890123")
        val major: String = "",
        val semester: Semester? = null, // 🆕 Semester enum 사용
        val phoneNumber: String = "", // 숫자만 저장 (예: "01012345678")
        val emailAddress: String = "",
        val companyName: String = "",
        val businessRegisterNumber: String = "", // 숫자만 저장 (예: "1234567890")
        val categoryOfBusiness: String = "",
        val addressOfCompany: String = "",
        val employerName: String = "",
        val employerPhoneNumber: String = "", // 숫자만 저장 (예: "01012345678")
        val hourlyWage: String = "",
        val workStartYear: String = "",
        val workStartMonth: String = "",
        val workStartDay: String = "",
        val workEndYear: String = "",
        val workEndMonth: String = "",
        val workEndDay: String = "",
        val workDay: List<WorkDay> = emptyList(), // 🆕 WorkDay enum 리스트 사용
        val workStartTime: String = "",
        val workEndTime: String = "",
    ) {
        // 기본 정보 유효성 검사 (숫자만 체크하도록 수정)
        val isNameValid: Boolean
            get() = name.isNotBlank()

        val isForeignerNumberValid: Boolean
            get() = foreignerNumber.matches(Regex("^\\d{13}$")) // 숫자 13자리

        val isMajorValid: Boolean
            get() = major.isNotBlank()

        val isSemesterValid: Boolean
            get() = semester != null // 🆕 enum이 선택되었는지 확인

        val isPhoneNumberValid: Boolean
            get() = phoneNumber.matches(Regex("^010\\d{8}$")) // 010으로 시작하는 11자리

        val isEmailAddressValid: Boolean
            get() = emailAddress.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))

        // 회사 정보 유효성 검사 (숫자만 체크하도록 수정)
        val isCompanyNameValid: Boolean
            get() = companyName.isNotBlank()

        val isBusinessRegisterNumberValid: Boolean
            get() = businessRegisterNumber.matches(Regex("^\\d{10}$")) // 숫자 10자리

        val isCategoryOfBusinessValid: Boolean
            get() = categoryOfBusiness.isNotBlank()

        val isAddressOfCompanyValid: Boolean
            get() = addressOfCompany.isNotBlank()

        val isEmployerNameValid: Boolean
            get() = employerName.isNotBlank()

        val isEmployerPhoneNumberValid: Boolean
            get() = employerPhoneNumber.matches(Regex("^0\\d{9,10}$")) // 0으로 시작하는 10-11자리

        // 근무 조건 유효성 검사
        val isHourlyWageValid: Boolean
            get() = hourlyWage.matches(Regex("^\\d+$")) && hourlyWage.isNotBlank()

        val isWorkStartYearValid: Boolean
            get() = workStartYear.matches(Regex("^\\d{4}$"))

        val isWorkStartMonthValid: Boolean
            get() = workStartMonth.matches(Regex("^\\d{1,2}$")) &&
                    workStartMonth.toIntOrNull()?.let { it in 1..12 } == true

        val isWorkStartDayValid: Boolean
            get() = workStartDay.matches(Regex("^\\d{1,2}$")) &&
                    workStartDay.toIntOrNull()?.let { it in 1..31 } == true

        val isWorkEndYearValid: Boolean
            get() = workEndYear.matches(Regex("^\\d{4}$"))

        val isWorkEndMonthValid: Boolean
            get() = workEndMonth.matches(Regex("^\\d{1,2}$")) &&
                    workEndMonth.toIntOrNull()?.let { it in 1..12 } == true

        val isWorkEndDayValid: Boolean
            get() = workEndDay.matches(Regex("^\\d{1,2}$")) &&
                    workEndDay.toIntOrNull()?.let { it in 1..31 } == true

        val isWorkDayValid: Boolean
            get() = workDay.isNotEmpty() // 🆕 enum 리스트가 비어있지 않은지 확인

        val isWorkStartTimeValid: Boolean
            get() = workStartTime.isNotBlank()

        val isWorkEndTimeValid: Boolean
            get() = workEndTime.isNotBlank()

        // 단계별 유효성 검사
        val isBasicInfo1Valid: Boolean
            get() = isNameValid && isForeignerNumberValid && isMajorValid

        val isBasicInfo2Valid: Boolean
            get() = isSemesterValid && isPhoneNumberValid && isEmailAddressValid

        val isWorkplaceInfo1Valid: Boolean
            get() = isCompanyNameValid && isBusinessRegisterNumberValid &&
                    isCategoryOfBusinessValid

        val isWorkplaceInfo2Valid: Boolean
            get() = isAddressOfCompanyValid && isEmployerNameValid && isEmployerPhoneNumberValid

        val isWorkplaceInfo3Valid: Boolean
            get() = isHourlyWageValid && isWorkStartYearValid && isWorkStartMonthValid &&
                    isWorkStartDayValid && isWorkEndYearValid && isWorkEndMonthValid &&
                    isWorkEndDayValid

        val isWorkplaceInfo4Valid: Boolean
            get() = isWorkDayValid && isWorkStartTimeValid && isWorkEndTimeValid

        val isAllValid: Boolean
            get() = isBasicInfo1Valid && isBasicInfo2Valid && isWorkplaceInfo1Valid &&
                    isWorkplaceInfo2Valid && isWorkplaceInfo3Valid && isWorkplaceInfo4Valid
    }
}