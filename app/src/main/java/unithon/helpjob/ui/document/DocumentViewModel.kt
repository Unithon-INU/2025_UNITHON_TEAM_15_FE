package unithon.helpjob.ui.document

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import unithon.helpjob.data.model.Semester
import unithon.helpjob.data.model.WorkDay
import unithon.helpjob.data.model.request.DocumentRequest
import unithon.helpjob.data.model.request.WeekdayWorkTime
import unithon.helpjob.data.model.request.WeekendWorkTime
import unithon.helpjob.data.repository.DocumentRepository
import javax.inject.Inject

@HiltViewModel
class DocumentViewModel @Inject constructor(
    private val documentRepository: DocumentRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(DocumentUiState())
    val uiState: StateFlow<DocumentUiState> = _uiState.asStateFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    // ... 기존 업데이트 함수들은 그대로 유지 ...

    // 기본 정보 입력 함수들 (VisualTransformation 사용으로 숫자만 저장)
    fun updateName(input: String) {
        _uiState.value = _uiState.value.copy(name = input)
    }

    fun updateForeignerNumber(input: String) {
        val numbersOnly = input.filter { it.isDigit() }.take(13)
        _uiState.value = _uiState.value.copy(foreignerNumber = numbersOnly)
    }

    fun updateMajor(input: String) {
        _uiState.value = _uiState.value.copy(major = input)
    }

    fun updateSemester(semester: Semester) {
        _uiState.value = _uiState.value.copy(semester = semester)
    }

    fun updatePhoneNumber(input: String) {
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

    fun updateWorkDay(selectedDay: WorkDay) {
        _uiState.update { currentState ->
            if (currentState.workDays.contains(selectedDay)) {
                val newTimes = currentState.workDayTimes.toMutableMap()
                newTimes.remove(selectedDay)
                currentState.copy(
                    workDays = currentState.workDays - selectedDay,
                    workDayTimes = newTimes,
                    isAllDaysSelected = false
                )
            } else {
                currentState.copy(
                    workDays = currentState.workDays + selectedDay,
                    isAllDaysSelected = false
                )
            }
        }
    }

    fun toggleAllDays() {
        _uiState.update { currentState ->
            if (currentState.isAllDaysSelected) {
                currentState.copy(
                    isAllDaysSelected = false,
                    workDays = emptyList(),
                    workDayTimes = emptyMap()
                )
            } else {
                currentState.copy(
                    isAllDaysSelected = true,
                    workDays = WorkDay.entries
                )
            }
        }
    }

    fun updateWorkDayStartTime(workDay: WorkDay, startTime: String) {
        _uiState.update { currentState ->
            val currentTimes = currentState.workDayTimes[workDay] ?: WorkDayTime()
            val newTimes = currentState.workDayTimes.toMutableMap()
            newTimes[workDay] = currentTimes.copy(startTime = startTime)

            if (currentState.isSameTimeForAll) {
                currentState.workDays.forEach { day ->
                    val dayTimes = newTimes[day] ?: WorkDayTime()
                    newTimes[day] = dayTimes.copy(startTime = startTime)
                }
            }

            currentState.copy(workDayTimes = newTimes)
        }
    }

    fun updateWorkDayEndTime(workDay: WorkDay, endTime: String) {
        _uiState.update { currentState ->
            val currentTimes = currentState.workDayTimes[workDay] ?: WorkDayTime()
            val newTimes = currentState.workDayTimes.toMutableMap()
            newTimes[workDay] = currentTimes.copy(endTime = endTime)

            if (currentState.isSameTimeForAll) {
                currentState.workDays.forEach { day ->
                    val dayTimes = newTimes[day] ?: WorkDayTime()
                    newTimes[day] = dayTimes.copy(endTime = endTime)
                }
            }

            currentState.copy(workDayTimes = newTimes)
        }
    }

    fun toggleSameTimeForAll() {
        _uiState.update { currentState ->
            val newIsSameTimeForAll = !currentState.isSameTimeForAll

            if (newIsSameTimeForAll && currentState.workDays.isNotEmpty()) {
                val firstDay = currentState.workDays.first()
                val firstDayTime = currentState.workDayTimes[firstDay] ?: WorkDayTime()
                val newTimes = currentState.workDayTimes.toMutableMap()

                currentState.workDays.forEach { day ->
                    newTimes[day] = firstDayTime.copy()
                }

                currentState.copy(
                    isSameTimeForAll = newIsSameTimeForAll,
                    workDayTimes = newTimes
                )
            } else {
                currentState.copy(isSameTimeForAll = newIsSameTimeForAll)
            }
        }
    }

    fun resetUiState(){
        _uiState.update {
            DocumentUiState()
        }
    }

    // 🆕 서류 제출 함수 구현
    fun submitDocument() {
        val currentState = _uiState.value

        if (!currentState.isAllValid) {
            Timber.w("Document validation failed")
            return
        }

        viewModelScope.launch {
            try {
                _isSubmitting.value = true

                val documentRequest = createDocumentRequest(currentState)
                Timber.d("Document data : ${documentRequest}")

                documentRepository.postCertification(documentRequest)

                Timber.d("Document submitted successfully")
                // 성공 시 UI 상태 초기화 또는 성공 화면으로 이동

            } catch (e: Exception) {
                Timber.e(e, "Failed to submit document")
                // 에러 처리 - 사용자에게 에러 메시지 표시
            } finally {
                _isSubmitting.value = false
            }
        }
    }

    // 🆕 DocumentRequest 생성 함수
    private fun createDocumentRequest(state: DocumentUiState): DocumentRequest {
        return DocumentRequest(
            name = state.name,
            regNum = formatForeignerNumber(state.foreignerNumber),
            major = state.major,
            phoneNum = formatPhoneNumber(state.phoneNumber),
            email = state.emailAddress,
            semester = state.semester?.apiValue ?: "",
            companyName = state.companyName,
            bizRegNum = formatBusinessNumber(state.businessRegisterNumber),
            industry = state.categoryOfBusiness,
            address = state.addressOfCompany,
            companyPhoneNum = formatPhoneNumber(state.employerPhoneNumber),
            workingStartDate = formatDate(state.workStartYear, state.workStartMonth, state.workStartDay),
            workingEndDate = formatDate(state.workEndYear, state.workEndMonth, state.workEndDay),
            hourlyWage = formatHourlyWage(state.hourlyWage),
            weekdayWorkTimes = createWeekdayWorkTimes(state),
            weekendWorkTimes = createWeekendWorkTimes(state)
        )
    }

    // 🆕 포맷팅 헬퍼 함수들
    private fun formatForeignerNumber(number: String): String {
        // 1234567890123 -> 123456-1234567
        return if (number.length == 13) {
            "${number.substring(0, 6)}-${number.substring(6)}"
        } else number
    }

    private fun formatPhoneNumber(number: String): String {
        // 01012345678 -> 010-1234-5678
        return when {
            number.startsWith("010") && number.length == 11 -> {
                "${number.substring(0, 3)}-${number.substring(3, 7)}-${number.substring(7)}"
            }
            number.startsWith("0") && number.length == 10 -> {
                "${number.substring(0, 3)}-${number.substring(3, 6)}-${number.substring(6)}"
            }
            number.startsWith("0") && number.length == 11 -> {
                "${number.substring(0, 3)}-${number.substring(3, 7)}-${number.substring(7)}"
            }
            else -> number
        }
    }

    private fun formatBusinessNumber(number: String): String {
        // 1234567890 -> 123-12-12345
        return if (number.length == 10) {
            "${number.substring(0, 3)}-${number.substring(3, 5)}-${number.substring(5)}"
        } else number
    }

    private fun formatDate(year: String, month: String, day: String): String {
        // 2025, 6, 21 -> 2025-06-21
        val paddedMonth = month.padStart(2, '0')
        val paddedDay = day.padStart(2, '0')
        return "$year-$paddedMonth-$paddedDay"
    }

    private fun formatHourlyWage(wage: String): String {
        // 10030 -> 10,030원
        val number = wage.toLongOrNull() ?: 0L
        return "${String.format("%,d", number)}원"
    }

    private fun formatTime(time: String): String {
        // 18:00 -> 18:00:00
        return if (time.contains(":") && time.split(":").size == 2) {
            "$time:00"
        } else time
    }

    // 🆕 평일 근무시간 생성
    private fun createWeekdayWorkTimes(state: DocumentUiState): List<WeekdayWorkTime> {
        val weekdays = listOf(WorkDay.MONDAY, WorkDay.TUESDAY, WorkDay.WEDNESDAY, WorkDay.THURSDAY, WorkDay.FRIDAY)
        val selectedWeekdays = state.workDays.filter { it in weekdays }

        if (selectedWeekdays.isEmpty()) return emptyList()

        // 같은 시간대별로 그룹화
        val timeGroups = selectedWeekdays.groupBy { workDay ->
            val dayTime = state.workDayTimes[workDay] ?: WorkDayTime()
            "${dayTime.startTime}_${dayTime.endTime}"
        }

        return timeGroups.map { (_, workDaysGroup) ->
            val firstDay = workDaysGroup.first()
            val dayTime = state.workDayTimes[firstDay] ?: WorkDayTime()

            WeekdayWorkTime(
                workingStartTime = formatTime(dayTime.startTime),
                workingEndTime = formatTime(dayTime.endTime),
                day = workDaysGroup.map { it.apiValue }
            )
        }
    }

    // 🆕 주말 근무시간 생성
    private fun createWeekendWorkTimes(state: DocumentUiState): List<WeekendWorkTime> {
        val weekends = listOf(WorkDay.SATURDAY, WorkDay.SUNDAY)
        val selectedWeekends = state.workDays.filter { it in weekends }

        if (selectedWeekends.isEmpty()) return emptyList()

        // 같은 시간대별로 그룹화
        val timeGroups = selectedWeekends.groupBy { workDay ->
            val dayTime = state.workDayTimes[workDay] ?: WorkDayTime()
            "${dayTime.startTime}_${dayTime.endTime}"
        }

        return timeGroups.map { (_, workDaysGroup) ->
            val firstDay = workDaysGroup.first()
            val dayTime = state.workDayTimes[firstDay] ?: WorkDayTime()

            WeekendWorkTime(
                workingStartTime = formatTime(dayTime.startTime),
                workingEndTime = formatTime(dayTime.endTime),
                day = workDaysGroup.map { it.apiValue }
            )
        }
    }

    // 요일별 시간 정보를 저장하는 데이터 클래스
    data class WorkDayTime(
        val startTime: String = "",
        val endTime: String = ""
    )

    data class DocumentUiState(
        val name: String = "",
        val foreignerNumber: String = "",
        val major: String = "",
        val semester: Semester? = null,
        val phoneNumber: String = "",
        val emailAddress: String = "",
        val companyName: String = "",
        val businessRegisterNumber: String = "",
        val categoryOfBusiness: String = "",
        val addressOfCompany: String = "",
        val employerName: String = "",
        val employerPhoneNumber: String = "",
        val hourlyWage: String = "",
        val workStartYear: String = "",
        val workStartMonth: String = "",
        val workStartDay: String = "",
        val workEndYear: String = "",
        val workEndMonth: String = "",
        val workEndDay: String = "",
        val workDays: List<WorkDay> = emptyList(),
        val workDayTimes: Map<WorkDay, WorkDayTime> = emptyMap(),
        val isAllDaysSelected: Boolean = false,
        val isSameTimeForAll: Boolean = false,
        @Deprecated("Use workDayTimes instead")
        val workStartTime: String = "",
        @Deprecated("Use workDayTimes instead")
        val workEndTime: String = "",
    ) {
        // ... 기존 유효성 검사 함수들은 그대로 유지 ...

        val isNameValid: Boolean
            get() = name.isNotBlank()

        val isForeignerNumberValid: Boolean
            get() = foreignerNumber.matches(Regex("^\\d{13}$"))

        val isMajorValid: Boolean
            get() = major.isNotBlank()

        val isSemesterValid: Boolean
            get() = semester != null

        val isPhoneNumberValid: Boolean
            get() = phoneNumber.matches(Regex("^010\\d{8}$"))

        val isEmailAddressValid: Boolean
            get() = emailAddress.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))

        val isCompanyNameValid: Boolean
            get() = companyName.isNotBlank()

        val isBusinessRegisterNumberValid: Boolean
            get() = businessRegisterNumber.matches(Regex("^\\d{10}$"))

        val isCategoryOfBusinessValid: Boolean
            get() = categoryOfBusiness.isNotBlank()

        val isAddressOfCompanyValid: Boolean
            get() = addressOfCompany.isNotBlank()

        val isEmployerNameValid: Boolean
            get() = employerName.isNotBlank()

        val isEmployerPhoneNumberValid: Boolean
            get() = employerPhoneNumber.matches(Regex("^0\\d{9,10}$"))

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
            get() = workDays.isNotEmpty()

        val isWorkTimeValid: Boolean
            get() = workDays.all { workDay ->
                val dayTime = workDayTimes[workDay]
                dayTime != null && dayTime.startTime.isNotBlank() && dayTime.endTime.isNotBlank()
            }

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
            get() = isWorkDayValid && isWorkTimeValid

        val isAllValid: Boolean
            get() = isBasicInfo1Valid && isBasicInfo2Valid && isWorkplaceInfo1Valid &&
                    isWorkplaceInfo2Valid && isWorkplaceInfo3Valid && isWorkplaceInfo4Valid
    }
}