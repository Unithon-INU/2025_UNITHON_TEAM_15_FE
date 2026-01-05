package unithon.helpjob.data.repository

import kotlinx.coroutines.runBlocking
import unithon.helpjob.data.model.GuestChecklist
import unithon.helpjob.data.model.request.UpdateEmploymentCheckRequest
import unithon.helpjob.data.model.response.DocumentInfoRes
import unithon.helpjob.data.model.response.EmploymentCheckRes
import unithon.helpjob.data.model.response.HomeInfoResponse
import unithon.helpjob.data.model.response.StepInfoRes
import unithon.helpjob.data.model.response.UpdateEmploymentCheckResponse

/**
 * Guest Mode용 Mock Data 제공
 *
 * ⚠️ 주의: Mock Data는 반드시 서버 응답과 100% 일치해야 합니다.
 * 백엔드 개발자에게 "최신 기준 체크리스트 JSON"을 요청하여 업데이트하세요.
 */
class GuestMockDataSource(
    private val authRepository: AuthRepository
) {

    /**
     * Guest 홈 정보 조회
     * @param language 언어 코드 ("ko", "en")
     */
    suspend fun getGuestHomeInfo(language: String): HomeInfoResponse {
        val guestChecklist = authRepository.getGuestChecklist()

        return HomeInfoResponse(
            nickname = "게스트",  // Guest 사용자 닉네임
            email = "",           // Guest는 이메일 없음
            progress = calculateProgress(guestChecklist),
            memberCheckStep = "STEP1",  // Guest는 항상 STEP1부터 시작
            employmentCheckRes = buildMockSteps(language, guestChecklist)
        )
    }

    /**
     * Guest 체크리스트 업데이트
     */
    suspend fun updateGuestChecklist(
        request: UpdateEmploymentCheckRequest
    ): UpdateEmploymentCheckResponse {
        val current = authRepository.getGuestChecklist() ?: GuestChecklist()

        // 현재 단계의 체크 항목 가져오기
        val stepItems = current.checkedItems[request.checkStep]?.toMutableList() ?: mutableListOf()

        // 토글 처리
        if (request.submissionIdx in stepItems) {
            stepItems.remove(request.submissionIdx)
        } else {
            stepItems.add(request.submissionIdx)
        }

        // 업데이트된 체크리스트 저장
        val updated = GuestChecklist(
            checkedItems = current.checkedItems.toMutableMap().apply {
                this[request.checkStep] = stepItems
            }
        )

        authRepository.saveGuestChecklist(updated)

        return UpdateEmploymentCheckResponse(
            progress = calculateProgress(updated)
        )
    }

    /**
     * 진행률 계산
     * 전체 문서 중 체크된 문서의 비율
     */
    private fun calculateProgress(checklist: GuestChecklist?): Int {
        if (checklist == null) return 0

        val guestProfile = runBlocking { authRepository.getGuestProfile() }
        val needsHealthCert = guestProfile?.industry?.contains("음식점") == true ||
                              guestProfile?.industry?.contains("카페") == true

        // 실제 서버 응답 기준: STEP1(1개) + STEP2(5~6개) + STEP3(2개) = 8~9개
        val totalItems = if (needsHealthCert) 9 else 8
        val checkedCount = checklist.checkedItems.values.sumOf { it.size }

        return ((checkedCount * 100) / totalItems).coerceIn(0, 100)
    }

    /**
     * 언어별 Mock Data 생성
     *
     * ⚠️ TODO: 백엔드 개발자에게 최신 서버 응답 JSON을 요청하여 업데이트 필요
     * 현재는 간단한 예시 데이터만 제공
     */
    private fun buildMockSteps(
        language: String,
        checklist: GuestChecklist?
    ): List<EmploymentCheckRes> {
        return when (language) {
            "ko" -> buildMockStepsKo(checklist)
            "en" -> buildMockStepsEn(checklist)
            else -> buildMockStepsKo(checklist)  // 기본값 한국어
        }
    }

    /**
     * 한국어 Mock Data
     *
     * ⚠️ 실제 서버 응답 기준 (2025-01-XX):
     * - STEP1: 시간제취업확인서 작성
     * - STEP2: 아르바이트 서류 준비 (업종에 따라 보건증 추가)
     * - STEP3: 아르바이트 신청
     */
    private fun buildMockStepsKo(checklist: GuestChecklist?): List<EmploymentCheckRes> {
        val checkedItems = checklist?.checkedItems ?: emptyMap()
        val guestProfile = runBlocking { authRepository.getGuestProfile() }
        val needsHealthCert = guestProfile?.industry?.contains("음식점") == true ||
                              guestProfile?.industry?.contains("카페") == true

        return listOf(
            // STEP1: 시간제취업확인서 작성
            EmploymentCheckRes(
                checkStep = "STEP1",
                stepInfoRes = StepInfoRes(
                    title = "시간제취업확인서 작성",
                    subtitle = "하단 서류작성 탭에서 시간제취업확인서를 작성할 수 있습니다.",
                    precautions = listOf(
                        "최대 1년동안 근무할 수 있어요.",
                        "비자 만료 전까지 근무할 수 있어요.",
                        "일주일에 10시간을 초과해 근무하고 싶다면 언어 자격을 준비해주세요 (TOPIK, IELTS etc.)."
                    )
                ),
                documentInfoRes = listOf(
                    DocumentInfoRes(
                        submissionIdx = 0,
                        title = "시간제취업확인서 작성",
                        isChecked = 0 in (checkedItems["STEP1"] ?: emptyList())
                    )
                )
            ),
            // STEP2: 아르바이트 서류 준비
            EmploymentCheckRes(
                checkStep = "STEP2",
                stepInfoRes = StepInfoRes(
                    title = "아르바이트 서류 준비",
                    subtitle = "아르바이트 신청을 위한 서류를 준비하는 단계예요.",
                    precautions = listOf(
                        "계약 전, 시급·근무시간 등 조건을 꼭 확인해요.",
                        "주당 근무 가능 시간을 초과하면 불법이에요.",
                        "계약서는 서명 전 사본을 꼭 보관하세요.",
                        "이해되지 않는 내용은 번역 또는 도움을 받아 확인하세요.",
                        "음식을 다루는 업장에서 근무할 경우, 보건증이 필요해요 (유효기간 1년)."
                    )
                ),
                documentInfoRes = buildList {
                    add(DocumentInfoRes(
                        submissionIdx = 0,
                        title = "근로계약서",
                        isChecked = 0 in (checkedItems["STEP2"] ?: emptyList())
                    ))
                    add(DocumentInfoRes(
                        submissionIdx = 1,
                        title = "시간제취업확인서",
                        isChecked = 1 in (checkedItems["STEP2"] ?: emptyList())
                    ))
                    add(DocumentInfoRes(
                        submissionIdx = 2,
                        title = "성적증명서",
                        isChecked = 2 in (checkedItems["STEP2"] ?: emptyList())
                    ))
                    add(DocumentInfoRes(
                        submissionIdx = 3,
                        title = "사업자등록증",
                        isChecked = 3 in (checkedItems["STEP2"] ?: emptyList())
                    ))
                    add(DocumentInfoRes(
                        submissionIdx = 4,
                        title = "사장님 신분증",
                        isChecked = 4 in (checkedItems["STEP2"] ?: emptyList())
                    ))
                    // 🆕 업종에 따라 보건증 추가
                    if (needsHealthCert) {
                        add(DocumentInfoRes(
                            submissionIdx = 5,
                            title = "보건증",
                            isChecked = 5 in (checkedItems["STEP2"] ?: emptyList())
                        ))
                    }
                }
            ),
            // STEP3: 아르바이트 신청
            EmploymentCheckRes(
                checkStep = "STEP3",
                stepInfoRes = StepInfoRes(
                    title = "아르바이트 신청",
                    subtitle = "준비한 서류를 가지고 아르바이트 신청을 하는 단계입니다.",
                    precautions = listOf(
                        "사무실을 방문해서 시간제취업확인서에 유학생담당자의 서명을 받아야 해요.",
                        "유학생담당자의 서명을 받은 시간제취업확인서를 제출해야 신청이 가능해요.",
                        "시간제취업허가 승인 전에는 절대 근무하면 안 돼요. 불법 취업으로 간주될 수 있어요."
                    )
                ),
                documentInfoRes = listOf(
                    DocumentInfoRes(
                        submissionIdx = 0,
                        title = "교내 유학생 사무실 방문",
                        isChecked = 0 in (checkedItems["STEP3"] ?: emptyList())
                    ),
                    DocumentInfoRes(
                        submissionIdx = 1,
                        title = "출입국에서 아르바이트 신청",
                        isChecked = 1 in (checkedItems["STEP3"] ?: emptyList())
                    )
                )
            )
        )
    }

    /**
     * 영어 Mock Data
     *
     * ⚠️ 실제 서버 응답 기준 영어 번역본
     */
    private fun buildMockStepsEn(checklist: GuestChecklist?): List<EmploymentCheckRes> {
        val checkedItems = checklist?.checkedItems ?: emptyMap()
        val guestProfile = runBlocking { authRepository.getGuestProfile() }
        val needsHealthCert = guestProfile?.industry?.contains("Restaurant") == true ||
                              guestProfile?.industry?.contains("Cafe") == true ||
                              guestProfile?.industry?.contains("음식점") == true ||
                              guestProfile?.industry?.contains("카페") == true

        return listOf(
            // STEP1: Writing Part-time Work Confirmation
            EmploymentCheckRes(
                checkStep = "STEP1",
                stepInfoRes = StepInfoRes(
                    title = "Writing Part-time Work Confirmation",
                    subtitle = "You can write the part-time work confirmation in the Document Writing tab below.",
                    precautions = listOf(
                        "You can work for up to 1 year.",
                        "You can work until your visa expires.",
                        "If you want to work more than 10 hours per week, prepare language qualifications (TOPIK, IELTS, etc.)."
                    )
                ),
                documentInfoRes = listOf(
                    DocumentInfoRes(
                        submissionIdx = 0,
                        title = "Writing Part-time Work Confirmation",
                        isChecked = 0 in (checkedItems["STEP1"] ?: emptyList())
                    )
                )
            ),
            // STEP2: Preparing Part-time Job Documents
            EmploymentCheckRes(
                checkStep = "STEP2",
                stepInfoRes = StepInfoRes(
                    title = "Preparing Part-time Job Documents",
                    subtitle = "This is the stage of preparing documents for applying for a part-time job.",
                    precautions = listOf(
                        "Be sure to check conditions such as hourly wage and working hours before the contract.",
                        "Exceeding the allowable working hours per week is illegal.",
                        "Be sure to keep a copy of the contract before signing.",
                        "Get a translation or help to confirm anything you don't understand.",
                        "If you work at a food establishment, you need a health certificate (valid for 1 year)."
                    )
                ),
                documentInfoRes = buildList {
                    add(DocumentInfoRes(
                        submissionIdx = 0,
                        title = "Employment Contract",
                        isChecked = 0 in (checkedItems["STEP2"] ?: emptyList())
                    ))
                    add(DocumentInfoRes(
                        submissionIdx = 1,
                        title = "Part-time Work Confirmation",
                        isChecked = 1 in (checkedItems["STEP2"] ?: emptyList())
                    ))
                    add(DocumentInfoRes(
                        submissionIdx = 2,
                        title = "Transcript",
                        isChecked = 2 in (checkedItems["STEP2"] ?: emptyList())
                    ))
                    add(DocumentInfoRes(
                        submissionIdx = 3,
                        title = "Business Registration Certificate",
                        isChecked = 3 in (checkedItems["STEP2"] ?: emptyList())
                    ))
                    add(DocumentInfoRes(
                        submissionIdx = 4,
                        title = "Employer's ID",
                        isChecked = 4 in (checkedItems["STEP2"] ?: emptyList())
                    ))
                    // 🆕 업종에 따라 보건증 추가
                    if (needsHealthCert) {
                        add(DocumentInfoRes(
                            submissionIdx = 5,
                            title = "Health Certificate",
                            isChecked = 5 in (checkedItems["STEP2"] ?: emptyList())
                        ))
                    }
                }
            ),
            // STEP3: Applying for Part-time Job
            EmploymentCheckRes(
                checkStep = "STEP3",
                stepInfoRes = StepInfoRes(
                    title = "Applying for Part-time Job",
                    subtitle = "This is the stage of applying for a part-time job with the prepared documents.",
                    precautions = listOf(
                        "You must visit the office and get the signature of the international student staff on the part-time work confirmation.",
                        "You can only apply by submitting the part-time work confirmation signed by the international student staff.",
                        "You must never work before the part-time work permit is approved. It may be considered illegal employment."
                    )
                ),
                documentInfoRes = listOf(
                    DocumentInfoRes(
                        submissionIdx = 0,
                        title = "Visit International Student Office on Campus",
                        isChecked = 0 in (checkedItems["STEP3"] ?: emptyList())
                    ),
                    DocumentInfoRes(
                        submissionIdx = 1,
                        title = "Apply for Part-time Job at Immigration",
                        isChecked = 1 in (checkedItems["STEP3"] ?: emptyList())
                    )
                )
            )
        )
    }
}
