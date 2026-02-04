package unithon.helpjob.ui.document

import androidx.lifecycle.viewModelScope
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.document_submit_success
import helpjob.composeapp.generated.resources.error_document_submit_failed
import helpjob.composeapp.generated.resources.error_fill_all_fields
import helpjob.composeapp.generated.resources.error_invalid_email
import helpjob.composeapp.generated.resources.error_invalid_foreigner_number
import helpjob.composeapp.generated.resources.error_invalid_work_end_date
import helpjob.composeapp.generated.resources.error_invalid_work_start_date
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import unithon.helpjob.util.Logger
import unithon.helpjob.data.model.Semester
import unithon.helpjob.data.model.WorkDay
import unithon.helpjob.data.model.request.DocumentRequest
import unithon.helpjob.data.model.request.WeekdayWorkTime
import unithon.helpjob.data.model.request.WeekendWorkTime
import unithon.helpjob.data.repository.AuthRepository
import unithon.helpjob.data.repository.DocumentRepository
import unithon.helpjob.data.repository.HomeStateRepository
import unithon.helpjob.ui.base.BaseViewModel
import unithon.helpjob.util.Analytics
import unithon.helpjob.util.EmailValidator
import unithon.helpjob.util.NumberFormatter

class DocumentViewModel(
    private val documentRepository: DocumentRepository,
    private val authRepository: AuthRepository,
    private val homeStateRepository: HomeStateRepository
): BaseViewModel() {

    private val _uiState = MutableStateFlow(DocumentUiState())
    val uiState: StateFlow<DocumentUiState> = _uiState.asStateFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    // 🆕 Snackbar용 에러 이벤트 - SharedFlow 사용
    private val _snackbarMessage = MutableSharedFlow<StringResource>()
    val snackbarMessage: SharedFlow<StringResource> = _snackbarMessage.asSharedFlow()

    init {
        // Guest Mode 실시간 구독 (로그인/로그아웃 시 자동 갱신)
        viewModelScope.launch {
            authRepository.observeGuestMode().collect { isGuest ->
                _uiState.update { it.copy(isGuest = isGuest) }
            }
        }

        // 가입 이메일(=로그인 ID)로 EmailCheck pre-fill
        viewModelScope.launch {
            val email = homeStateRepository.homeState.value.email
            if (email.isNotBlank()) {
                _uiState.update { it.copy(emailAddress = email) }
            }
        }
    }


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
        _uiState.update { currentState ->
            currentState.copy(
                emailAddress = input,
                emailError = false,
                emailErrorMessage = null
            )
        }

        // 실시간 이메일 형식 검증
        if (input.isNotBlank() && !EmailValidator.isValid(input)) {
            _uiState.update {
                it.copy(
                    emailError = true,
                    emailErrorMessage = Res.string.error_invalid_email
                )
            }
        }
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
                    workDayTimes = newTimes
                )
            } else {
                currentState.copy(
                    workDays = currentState.workDays + selectedDay
                )
            }
        }
    }

    fun toggleVacation() {
        _uiState.update { currentState ->
            currentState.copy(isVacation = !currentState.isVacation)
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

    // 서류 제출 함수 구현
    fun submitDocument() {
        val currentState = _uiState.value

        // 기본 유효성 검사
        if (!currentState.isAllValid) {
            viewModelScope.launch {
                _snackbarMessage.emit(Res.string.error_fill_all_fields)
            }
            return
        }

        // 추가 검사: 외국인등록번호 길이
        if (currentState.foreignerNumber.filter { it.isDigit() }.length != 13) {
            viewModelScope.launch {
                _snackbarMessage.emit(Res.string.error_invalid_foreigner_number)
            }
            return
        }

        // 추가 검사: 날짜 유효성
        if (!isValidDate(currentState.workStartYear, currentState.workStartMonth, currentState.workStartDay)) {
            viewModelScope.launch {
                _snackbarMessage.emit(Res.string.error_invalid_work_start_date)
            }
            return
        }

        if (!isValidDate(currentState.workEndYear, currentState.workEndMonth, currentState.workEndDay)) {
            viewModelScope.launch {
                _snackbarMessage.emit(Res.string.error_invalid_work_end_date)
            }
            return
        }

        viewModelScope.launch(crashPreventionHandler) {
            try {
                _isSubmitting.value = true

                val documentRequest = createDocumentRequest(currentState)
                Logger.d("Document data : $documentRequest")

                documentRepository.postCertification(documentRequest)

                Logger.d("Document submitted successfully")
                _snackbarMessage.emit(Res.string.document_submit_success) // 성공 이벤트 발생

                Analytics.logEvent("certificate_sent")
            } catch (e: Exception) {
                Logger.e(e, "Failed to submit document")

//                val errorMessage = when {
//                    e.message?.contains("400") == true -> "입력 정보가 올바르지 않습니다."
//                    e.message?.contains("401") == true -> "인증이 필요합니다."
//                    e.message?.contains("403") == true -> "권한이 없습니다."
//                    e.message?.contains("500") == true -> "서버 오류가 발생했습니다."
//                    e.message?.contains("network") == true -> "네트워크 연결을 확인해주세요."
//                    else -> "서류 제출 중 오류가 발생했습니다. 다시 시도해주세요."
//                }

                _snackbarMessage.emit(Res.string.error_document_submit_failed) // 에러 이벤트 발생

            } finally {
                _isSubmitting.value = false
            }
        }
    }

    // 날짜 유효성 검사 함수들
    private fun isValidDate(year: String, month: String, day: String): Boolean {
        return try {
            val yearInt = year.toInt()
            val monthInt = month.toInt()
            val dayInt = day.toInt()

            if (monthInt !in 1..12) return false

            val daysInMonth = when (monthInt) {
                1, 3, 5, 7, 8, 10, 12 -> 31
                4, 6, 9, 11 -> 30
                2 -> if (isLeapYear(yearInt)) 29 else 28
                else -> return false
            }

            dayInt in 1..daysInMonth
        } catch (e: Exception) {
            false
        }
    }

    private fun isLeapYear(year: Int): Boolean {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
    }

    // DocumentRequest 생성 함수
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

    // 포맷팅 헬퍼 함수들
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
        return NumberFormatter.formatCurrency(number)
    }

    private fun formatTime(time: String): String {
        // 18:00 -> 18:00:00
        return if (time.contains(":") && time.split(":").size == 2) {
            "$time:00"
        } else time
    }

    // 평일 근무시간 생성
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

    // 주말 근무시간 생성
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
        val emailError: Boolean = false,
        val emailErrorMessage: StringResource? = null,
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
        val isVacation: Boolean = false,
        val isSameTimeForAll: Boolean = false,
        val isGuest: Boolean = false,  // 🆕 Guest Mode 여부
        @Deprecated("Use workDayTimes instead")
        val workStartTime: String = "",
        @Deprecated("Use workDayTimes instead")
        val workEndTime: String = "",
    ) {
        // ... 기존 유효성 검사 함수들은 그대로 유지 ...

        private val isNameValid: Boolean
            get() = name.isNotBlank()

        private val isForeignerNumberValid: Boolean
            get() = foreignerNumber.matches(Regex("^\\d{13}$"))

        private val isMajorValid: Boolean
            get() = major.isNotBlank()

        private val isSemesterValid: Boolean
            get() = semester != null

        private val isPhoneNumberValid: Boolean
            get() = phoneNumber.matches(Regex("^010\\d{8}$"))

        private val isEmailAddressValid: Boolean
            get() = emailAddress.isNotBlank() &&
                    EmailValidator.isValid(emailAddress) &&
                    !emailError

        private val isCompanyNameValid: Boolean
            get() = companyName.isNotBlank()

        private val isBusinessRegisterNumberValid: Boolean
            get() = businessRegisterNumber.matches(Regex("^\\d{10}$"))

        private val isCategoryOfBusinessValid: Boolean
            get() = categoryOfBusiness.isNotBlank()

        private val isAddressOfCompanyValid: Boolean
            get() = addressOfCompany.isNotBlank()

        private val isEmployerNameValid: Boolean
            get() = employerName.isNotBlank()

        private val isEmployerPhoneNumberValid: Boolean
            get() = employerPhoneNumber.matches(Regex("^0\\d{9,10}$"))

        private val isHourlyWageValid: Boolean
            get() = hourlyWage.matches(Regex("^\\d+$")) && hourlyWage.isNotBlank()

        private val isWorkStartYearValid: Boolean
            get() = workStartYear.matches(Regex("^\\d{4}$"))

        private val isWorkStartMonthValid: Boolean
            get() = workStartMonth.matches(Regex("^\\d{1,2}$")) &&
                    workStartMonth.toIntOrNull()?.let { it in 1..12 } == true

        private val isWorkStartDayValid: Boolean
            get() = workStartDay.matches(Regex("^\\d{1,2}$")) &&
                    workStartDay.toIntOrNull()?.let { it in 1..31 } == true

        private val isWorkEndYearValid: Boolean
            get() = workEndYear.matches(Regex("^\\d{4}$"))

        private val isWorkEndMonthValid: Boolean
            get() = workEndMonth.matches(Regex("^\\d{1,2}$")) &&
                    workEndMonth.toIntOrNull()?.let { it in 1..12 } == true

        private val isWorkEndDayValid: Boolean
            get() = workEndDay.matches(Regex("^\\d{1,2}$")) &&
                    workEndDay.toIntOrNull()?.let { it in 1..31 } == true

        private val isWorkDayValid: Boolean
            get() = workDays.isNotEmpty()

        private val isWorkTimeValid: Boolean
            get() = workDays.all { workDay ->
                val dayTime = workDayTimes[workDay]
                dayTime != null && dayTime.startTime.isNotBlank() && dayTime.endTime.isNotBlank()
            }

        // 최대 허용 시간 상수 (추후 서버/함수로 교체 가능)
        companion object {
            const val MAX_WEEKDAY_HOURS = 20f  // 임시 고정값
            const val MAX_WEEKEND_HOURS = 10f  // 임시 고정값
        }

        // 시간 문자열을 분 단위로 변환
        private fun parseTimeToMinutes(time: String): Int {
            if (time.isBlank()) return 0
            val parts = time.split(":")
            return if (parts.size == 2) {
                (parts[0].toIntOrNull() ?: 0) * 60 + (parts[1].toIntOrNull() ?: 0)
            } else 0
        }

        // 요일별 근무 시간 계산 (분 단위)
        private fun calculateDayMinutes(workDay: WorkDay): Int {
            val dayTime = workDayTimes[workDay] ?: return 0
            if (dayTime.startTime.isBlank() || dayTime.endTime.isBlank()) return 0
            val start = parseTimeToMinutes(dayTime.startTime)
            val end = parseTimeToMinutes(dayTime.endTime)
            return if (end > start) end - start else 0
        }

        // 주중 총 근무 시간 (시간 단위, Float)
        val weekdayTotalHours: Float
            get() {
                val weekdays = listOf(WorkDay.MONDAY, WorkDay.TUESDAY, WorkDay.WEDNESDAY,
                    WorkDay.THURSDAY, WorkDay.FRIDAY)
                val totalMinutes = workDays.filter { it in weekdays }
                    .sumOf { calculateDayMinutes(it) }
                return totalMinutes / 60f
            }

        // 주말 총 근무 시간 (시간 단위, Float)
        val weekendTotalHours: Float
            get() {
                val weekends = listOf(WorkDay.SATURDAY, WorkDay.SUNDAY)
                val totalMinutes = workDays.filter { it in weekends }
                    .sumOf { calculateDayMinutes(it) }
                return totalMinutes / 60f
            }

        // 주중 초과 여부
        val isWeekdayOvertime: Boolean
            get() = weekdayTotalHours > MAX_WEEKDAY_HOURS

        // 주말 초과 여부
        val isWeekendOvertime: Boolean
            get() = weekendTotalHours > MAX_WEEKEND_HOURS

        val isBasicInfo1Valid: Boolean
            get() = isNameValid && isForeignerNumberValid && isPhoneNumberValid

        val isBasicInfo2Valid: Boolean
            get() = isMajorValid && isSemesterValid

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
            get() = isWorkDayValid && isWorkTimeValid && (isVacation || (!isWeekdayOvertime && !isWeekendOvertime))

        val isAllValid: Boolean
            get() = isBasicInfo1Valid && isBasicInfo2Valid && isWorkplaceInfo1Valid &&
                    isWorkplaceInfo2Valid && isWorkplaceInfo3Valid && isWorkplaceInfo4Valid &&
                    isEmailAddressValid
    }
}