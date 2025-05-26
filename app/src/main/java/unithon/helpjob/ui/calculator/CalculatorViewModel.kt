package unithon.helpjob.ui.calculator

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import unithon.helpjob.ui.auth.signin.SignInViewModel.SignInUiState
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(

) : ViewModel() {

    data class CalculatorUiState(
        val wage: String = "",
        val workTime: String = "",
        val workDayCount: String = "",
        val salary: String = ""
    ) {
        val isWageInputValid: Boolean
            get() = wage.isNotBlank() && wage.all { it.isDigit() }
        val isWorkTimeInputValid: Boolean
            get() = workTime.isNotBlank() && workTime.all { it.isDigit() }
        val isWorkDayCountInputValid: Boolean
            get() = workDayCount.isNotBlank() && workDayCount.all { it.isDigit() }
    }

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    fun updateWage(wage: String) {
        _uiState.update {
            it.copy(wage = wage)
        }
    }

    fun updateWorkTime(workTime: String) {
        _uiState.update {
            it.copy(workTime = workTime)
        }
    }

    fun updateWorkDayCount(workDayCount: String) {
        _uiState.update {
            it.copy(workDayCount = workDayCount)
        }
    }

    fun calculateSalary(){
        val wage = _uiState.value.wage.toIntOrNull() ?: 0
        val workTime = _uiState.value.workTime.toIntOrNull() ?: 0
        val workDayCount = _uiState.value.workDayCount.toIntOrNull() ?: 0
        val salary = wage * workTime * workDayCount
        _uiState.update {
            it.copy(salary = salary.toString())
        }
        Log.d("calculateSalary", salary.toString())
    }

}