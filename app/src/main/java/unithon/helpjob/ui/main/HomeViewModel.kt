package unithon.helpjob.ui.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

) : ViewModel() {

    data class HomeUiState(
        val steps : List<Step> = emptyList(),
        val selectedCategory: String = "제출 서류",
        val progressPercentage: Float? = null,
    )

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun getStepById(stepId: Int): Step? {
        return uiState.value.steps.find { it.step == stepId }
    }

    fun selectCategory(category: String){
        _uiState.update {
            it.copy(
                selectedCategory = category
            )
        }
    }

    init {
        getStepInfo()
    }

    fun getStepInfo(){
        val stepList = listOf(
            Step(
                step = 1,
                title = "근로계약서 작성",
                subTitle = "근무 조건, 시급, 시간 등 협의하는 단계예요.",
                submissionDocument = listOf(
                    Document(
                        title = "외국인등록증",
                        isSuccess = true
                    ),
                    Document(
                        title = "여권",
                        isSuccess = true
                    ),
                    Document(
                        title = "비자",
                        isSuccess = true
                    ),
                    Document(
                        title = "재학증명서",
                        isSuccess = false
                    )
                ),
                precautions = listOf(
                    "계약 전, 시급·근무시간 등 조건을 꼭 확인해요",
                    "주당 근무 가능 시간을 초과하면 불법이에요",
                    "계약서는 서명 전 사본을 꼭 보관하세요",
                    "이해되지 않는 내용은 번역 또는 도움을 받아 확인하세요"
                ),
                tips = listOf(
                    Tip(
                        title = "근무 시간과 휴게 시간",
                        content = listOf(
                            TipDetail(
                                "",
                                "",
                                null
                            )
                        )
                    ),
                    Tip(
                        title = "시급 외 급여 사항",
                        content = listOf(
                            TipDetail(
                                "주휴수당이란?",
                                "일주일에 15시간 이상, 정해진 요일에 근무하면\n" +
                                        "1일치 시급을 더 받아요.",
                                null
                            ),
                            TipDetail(
                                "휴일근로수당",
                                "휴일이나 법정공휴일에 근무하면 시급의 1.5배\n" +
                                        "받을 수 있어요.",
                                "※ 계약 내용과 업장 규모에 따라 달라질 수 있어요"
                            )
                        )
                    ),
                    Tip(
                        title = "이런 건 꼭 물어보세요!",
                        content = listOf(
                            TipDetail(
                                title = "사장님께 주휴수당, 휴게시간, 공휴일 수당 등은 꼭 물어보는 것이 좋아요.",
                                content = null,
                                warning = null
                            )
                        )
                    ),
                    Tip(
                        title = "기타사항",
                        content = listOf(
                            TipDetail(
                                title = "계약서는 2부 작성 필수!\n" +
                                        "나와 사장님이 각각 한 부씩 보관해요.",
                                content = null,
                                warning = null
                            )
                        )
                    )
                )
            ),
            Step(
                step = 2,
                title = "근로신고",
                subTitle = "고용 신고를 통해 법적 보호를 받는 단계예요.",
                submissionDocument = listOf(
                    Document(
                        title = "외국인등록증",
                        isSuccess = true
                    ),
                    Document(
                        title = "여권",
                        isSuccess = true
                    ),
                    Document(
                        title = "비자",
                        isSuccess = true
                    ),
                    Document(
                        title = "재학증명서",
                        isSuccess = false
                    )
                ),
                precautions = listOf(
                    "고용신고는 고용일로부터 14일 이내에 해야 해요",
                    "신고하지 않으면 법적 보호를 받기 어려워요",
                    "사업주가 신고하는 것이 원칙이에요",
                    "신고 완료 후 확인서를 받아 보관하세요"
                ),
                tips = listOf()
            ),
            Step(
                step = 3,
                title = "근무 시작",
                subTitle = "근무 시작 가능합니다.",
                submissionDocument = listOf(
                    Document(
                        title = "외국인등록증",
                        isSuccess = true
                    ),
                    Document(
                        title = "여권",
                        isSuccess = true
                    ),
                    Document(
                        title = "비자",
                        isSuccess = true
                    ),
                    Document(
                        title = "재학증명서",
                        isSuccess = false
                    )
                ),
                precautions = listOf(
                    "근무시간을 정확히 기록해두세요",
                    "급여 명세서를 꼼꼼히 확인하세요",
                    "문제가 생기면 즉시 상담받으세요",
                    "건강과 안전을 최우선으로 생각하세요"
                ),
                tips = listOf()
            )
        )

        _uiState.update {
            it.copy(
                steps = stepList
            )
        }
    }
}