package unithon.helpjob.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import unithon.helpjob.data.model.request.DocumentRequest
import unithon.helpjob.data.model.request.EmailSendReq
import unithon.helpjob.data.model.request.EmailVerifyCodeReq
import unithon.helpjob.data.model.request.MemberNicknameReq
import unithon.helpjob.data.model.request.MemberProfilePatchReq
import unithon.helpjob.data.model.request.MemberProfileSetReq
import unithon.helpjob.data.model.request.MemberSignInReq
import unithon.helpjob.data.model.request.MemberSignUpReq
import unithon.helpjob.data.model.request.UpdateEmploymentCheckRequest
import unithon.helpjob.data.model.response.HomeInfoResponse
import unithon.helpjob.data.model.response.MemberProfileGetRes
import unithon.helpjob.data.model.response.TipResponseItem
import unithon.helpjob.data.model.response.TokenResponse
import unithon.helpjob.data.model.response.AccreditedUniversityRes
import unithon.helpjob.data.model.response.UpdateEmploymentCheckResponse
import unithon.helpjob.data.model.response.WorkingTimeLimitResponse
// TODO[LEGACY]: import unithon.helpjob.data.model.response.UniversityResponse

class HelpJobApiService(private val client: HttpClient) {
    // 회원 관련 API
    suspend fun signIn(request: MemberSignInReq): TokenResponse {
        return client.post(ApiConstants.SIGN_IN) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun signUp(request: MemberSignUpReq): TokenResponse {
        return client.post(ApiConstants.SIGN_UP) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun setNickname(request: MemberNicknameReq) {
        client.post(ApiConstants.SET_NICKNAME) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    suspend fun setProfile(request: MemberProfileSetReq): TokenResponse {
        return client.post(ApiConstants.SET_PROFILE) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun getMemberProfile(): MemberProfileGetRes {
        return client.get(ApiConstants.GET_PROFILE).body()
    }

    suspend fun patchProfile(profileField: String, request: MemberProfilePatchReq) {
        client.patch("${ApiConstants.PATCH_PROFILE}/$profileField") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    // 🆕 이메일 인증 관련 API
    suspend fun sendEmailVerification(request: EmailSendReq) {
        client.post(ApiConstants.EMAIL_SEND) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    suspend fun verifyEmailCode(request: EmailVerifyCodeReq) {
        client.post(ApiConstants.EMAIL_VERIFY) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    // 시간제 취업 확인 관련 API (서버가 배열 형식만 받도록 변경됨)
    suspend fun updateChecklist(requests: List<UpdateEmploymentCheckRequest>): UpdateEmploymentCheckResponse {
        return client.patch(ApiConstants.UPDATE_CHECKLIST) {
            contentType(ContentType.Application.Json)
            setBody(requests)
        }.body()
    }

    suspend fun getHomeInfo(): HomeInfoResponse {
        return client.get(ApiConstants.GET_HOME_INFO).body()
    }

    suspend fun getTips(checkStep: String): List<TipResponseItem> {
        return client.get(ApiConstants.GET_TIPS) {
            parameter("checkStep", checkStep)
        }.body()
    }

    suspend fun resetProgress() {
        client.put(ApiConstants.RESET_PROGRESS)
    }

    suspend fun postCertification(documentRequest: DocumentRequest) {
        client.post(ApiConstants.POST_CERTIFICATION) {
            contentType(ContentType.Application.Json)
            setBody(documentRequest)
        }
    }

    // TODO[LEGACY]: 대학 검색 API 재통합 시 해제
    // suspend fun searchUniversity(university: String): List<UniversityResponse> {
    //     return client.get(ApiConstants.SEARCH_UNIVERSITY) {
    //         parameter("university", university)
    //     }.body()
    // }

    // 인증대학 목록 조회 API
    suspend fun getAccreditedUniversities(): AccreditedUniversityRes {
        return client.get(ApiConstants.GET_ACCREDITED_UNIVERSITIES).body()
    }

    // 시간제취업 근무 가능 시간 조회 API
    suspend fun getWorkingTimeLimit(
        isAccredited: Boolean,
        year: String
    ): WorkingTimeLimitResponse {
        return client.get(ApiConstants.GET_WORKING_TIME_LIMIT) {
            parameter("isAccredited", isAccredited)
            parameter("year", year)
        }.body()
    }

    // 정책 및 약관 관련 API
    suspend fun getPrivacyPolicy(): String {
        return client.get(ApiConstants.PRIVACY_POLICY).body()
    }

    suspend fun getTermsOfService(): String {
        return client.get(ApiConstants.TERMS_OF_SERVICE).body()
    }
}
