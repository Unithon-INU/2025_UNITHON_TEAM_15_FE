package unithon.helpjob.data.repository

import javax.inject.Inject
import javax.inject.Singleton

/**
 * 회원가입 임시 데이터 모델
 */
data class SignUpData(
    val email: String,
    val password: String
)

/**
 * 회원가입 과정에서 임시로 데이터를 저장하는 Repository
 */
@Singleton
class SignUpDataRepository @Inject constructor() {
    private var pendingSignUpData: SignUpData? = null

    fun saveSignUpData(data: SignUpData) {
        pendingSignUpData = data
    }

    fun getSignUpData(): SignUpData? = pendingSignUpData

    fun clearSignUpData() {
        pendingSignUpData = null
    }
}