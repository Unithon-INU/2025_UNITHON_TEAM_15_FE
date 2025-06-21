package unithon.helpjob.ui.calculator

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import unithon.helpjob.ui.calculator.components.CalculationResult
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor() : ViewModel() {

    data class CalculatorUiState(
        val wage: String = "",
        val selectedWorkTime: Float? = null, // 선택된 일일 근무시간 (시간 단위)
        val selectedWorkDayCount: Int? = null, // 선택된 주간 근무일수
        val salary: String = "",
        val calculationResult: CalculationResult? = null,
        val showResultDialog: Boolean = false
    ) {
        val isWageInputValid: Boolean
            get() = wage.isNotBlank() && wage.all { it.isDigit() }
        val isWorkTimeInputValid: Boolean
            get() = selectedWorkTime != null
        val isWorkDayCountInputValid: Boolean
            get() = selectedWorkDayCount != null
        val isLowerThanMinimumWage: Boolean
            get() {
                // 입력이 유효하고 숫자로 변환 가능할 때만 비교
                val wageInt = wage.toIntOrNull()
                return wageInt != null && wageInt < 10030
            }
    }

    // 일일 근무시간 옵션들 (1시간부터 16시간까지 30분 단위)
    val workTimeOptions = buildList {
        for (i in 2..32) { // 2 * 0.5 = 1시간부터 32 * 0.5 = 16시간까지
            add(i * 0.5f)
        }
    }

    // 주간 근무일수 옵션들 (1일부터 7일까지)
    val workDayOptions = (1..7).toList()

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    fun updateWage(wage: String) {
        _uiState.update {
            it.copy(wage = wage)
        }
    }

    fun updateSelectedWorkTime(workTime: Float) {
        _uiState.update {
            it.copy(selectedWorkTime = workTime)
        }
    }

    fun updateSelectedWorkDayCount(workDayCount: Int) {
        _uiState.update {
            it.copy(selectedWorkDayCount = workDayCount)
        }
    }

    fun calculateSalary() {
        val wage = _uiState.value.wage.toIntOrNull() ?: 0
        val workTime = _uiState.value.selectedWorkTime ?: 0f
        val workDayCount = _uiState.value.selectedWorkDayCount ?: 0

        val weeklyWorkHours = workTime * workDayCount
        val includesWeeklyAllowance = weeklyWorkHours >= 15f

        // 주휴수당 계산 로직
        val (monthlySalary, weeklyAllowanceAmount) = if (includesWeeklyAllowance) {
            // 15시간 이상: 기본급(10시간×4주) + 주휴수당(10시간분)
            val basicSalary = wage * 10 * 4
            val allowance = wage * 10
            Pair(basicSalary + allowance, allowance)
        } else {
            // 15시간 미만: 실제근무시간 × 4주
            val salary = (wage * weeklyWorkHours * 4).toInt()
            Pair(salary, 0)
        }

        // CalculationResult 객체 생성
        val calculationResult = CalculationResult(
            workHours = weeklyWorkHours.toInt(),
            weeklyAllowanceHours = weeklyAllowanceAmount,
            totalAmount = monthlySalary,
            includesWeeklyAllowance = includesWeeklyAllowance
        )

        _uiState.update {
            it.copy(
                salary = monthlySalary.toString(),
                calculationResult = calculationResult,
                showResultDialog = true
            )
        }
    }

    // 시간을 문자열로 변환하는 함수
    fun workTimeToString(time: Float): String {
        val hours = time.toInt()
        val minutes = ((time - hours) * 60).toInt()
        return if (minutes == 0) {
            "${hours}시간"
        } else {
            "${hours}시간 ${minutes}분"
        }
    }

    // 일수를 문자열로 변환하는 함수
    fun workDayToString(days: Int): String {
        return "${days}일"
    }

    fun dismissResultDialog() {
        _uiState.update {
            it.copy(showResultDialog = false)
        }
    }
}