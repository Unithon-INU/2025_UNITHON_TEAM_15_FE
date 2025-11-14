package unithon.helpjob.ui.calculator.components

data class CalculationResult(
    val workHours: Int,
    val weeklyAllowanceHours: Int,
    val totalAmount: Int,
    val includesWeeklyAllowance: Boolean
)
