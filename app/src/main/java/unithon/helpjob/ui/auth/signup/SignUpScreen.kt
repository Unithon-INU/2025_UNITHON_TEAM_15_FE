package unithon.helpjob.ui.auth.signup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import unithon.helpjob.R
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.components.HelpJobTextField
import unithon.helpjob.ui.components.HelpJobTopAppBar
import unithon.helpjob.ui.theme.*

@Composable
fun SignUpScreen(
    onNavigateToNicknameSetup: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 회원가입 성공시 네비게이션
    LaunchedEffect(uiState.isSignUpSuccessful) {
        if (uiState.isSignUpSuccessful) {
            onNavigateToNicknameSetup()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            HelpJobTopAppBar(
                title = R.string.sign_up_top_bar_title,
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // 제목
            Text(
                text = stringResource(id = R.string.sign_up_title),
                style = MaterialTheme.typography.headlineLarge,
                color = Grey700
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 부제목
            Text(
                text = stringResource(id = R.string.sign_up_subtitle),
                style = MaterialTheme.typography.titleMedium,
                color = Grey500
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 이메일 레이블
            Text(
                text = stringResource(id = R.string.sign_up_email_label),
                style = MaterialTheme.typography.titleSmall,
                color = Grey500
            )

            Spacer(modifier = Modifier.height(9.dp))

            // 이메일 입력 필드 + send 버튼
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                HelpJobTextField(
                    value = uiState.email,
                    onValueChange = viewModel::updateEmail,
                    label = "",
                    placeholder = stringResource(id = R.string.sign_up_email_hint),
                    isError = uiState.emailError,
                    errorMessage = uiState.emailErrorMessage?.let { stringResource(id = it) },
                    modifier = Modifier.weight(1f)
                )

                // send 버튼
                Button(
                    onClick = viewModel::sendEmailVerification,
                    enabled = uiState.isEmailValid && !uiState.isSendingEmail,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (uiState.isEmailValid && !uiState.isSendingEmail) Primary500 else Grey200,
                        contentColor = if (uiState.isEmailValid && !uiState.isSendingEmail) Grey000 else Grey400,
                        disabledContainerColor = Grey200,
                        disabledContentColor = Grey400
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp),
                    modifier = Modifier
                        .width(71.dp)
                        .height(46.dp)  // 스펙에 맞게 46dp로 변경
                ) {
                    if (uiState.isSendingEmail) {
                        Text(
                            text = "...",
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 20.sp,
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.Bold,
                            ),
                            maxLines = 1
                        )
                    } else {
                        Text(
                            text = if (uiState.isEmailSent) "sent" else "send",
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 20.sp,
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.Bold,
                            ),
                            maxLines = 1
                        )
                    }
                }
            }

            // 이메일 전송 후 인증코드 입력 섹션
            if (uiState.isEmailSent) {
                Spacer(modifier = Modifier.height(20.dp))

                // 인증번호 안내 메시지
                if (!uiState.verificationCodeError) {
                    Text(
                        text = stringResource(R.string.verification_code_instruction),
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 15.sp,
                            fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                            fontWeight = FontWeight(500),
                            color = Primary500,
                        )
                    )
                    Spacer(modifier = Modifier.height(9.dp))
                }

                // 인증코드 입력 필드 + verify 버튼 + Resend
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        HelpJobTextField(
                            value = uiState.verificationCode,
                            onValueChange = viewModel::updateVerificationCode,
                            label = "",
                            placeholder = stringResource(id = R.string.verification_code_hint),
                            isError = uiState.verificationCodeError,
                            errorMessage = uiState.verificationCodeErrorMessage?.let { stringResource(id = it) },
                            modifier = Modifier.weight(1f)
                        )

                        // verify 버튼
                        Button(
                            onClick = viewModel::verifyEmailCode,
                            enabled = uiState.verificationCode.isNotBlank() && !uiState.isVerifyingCode && !uiState.isCodeVerified,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (uiState.verificationCode.isNotBlank() && !uiState.isCodeVerified) Primary500 else Grey200,
                                contentColor = if (uiState.verificationCode.isNotBlank() && !uiState.isCodeVerified) Grey000 else Grey400,
                                disabledContainerColor = Grey200,
                                disabledContentColor = Grey400
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp),
                            modifier = Modifier
                                .width(80.dp)
                                .height(46.dp)  // 스펙에 맞게 46dp로 변경
                        ) {
                            if (uiState.isVerifyingCode) {
                                Text(
                                    text = "...",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        lineHeight = 20.sp,
                                        fontFamily = PretendardFontFamily,
                                        fontWeight = FontWeight.Bold,
                                    ),
                                    maxLines = 1
                                )
                            } else {
                                Text(
                                    text = "verify",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        lineHeight = 20.sp,
                                        fontFamily = PretendardFontFamily,
                                        fontWeight = FontWeight.Bold,
                                    ),
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    // Resend 텍스트
                    if (uiState.verificationCodeError || uiState.verificationCodeErrorMessage == R.string.verification_code_expired) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = viewModel::resendEmailVerification,
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.resend_button),
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        lineHeight = 15.sp,
                                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                                        fontWeight = FontWeight(500),
                                        color = Warning,
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 비밀번호 레이블
            Text(
                text = stringResource(id = R.string.sign_up_password_label),
                style = MaterialTheme.typography.titleSmall,
                color = Grey500
            )

            Spacer(modifier = Modifier.height(9.dp))

            // 비밀번호 입력 필드
            HelpJobTextField(
                value = uiState.password,
                onValueChange = viewModel::updatePassword,
                label = "",
                placeholder = stringResource(id = R.string.sign_up_password_hint),
                isPassword = true,
                isError = uiState.passwordError,
                errorMessage = uiState.passwordErrorMessage?.let { stringResource(id = it) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 비밀번호 확인 레이블
            Text(
                text = stringResource(id = R.string.sign_up_confirm_password_label),
                style = MaterialTheme.typography.titleSmall,
                color = Grey500
            )

            Spacer(modifier = Modifier.height(9.dp))

            // 비밀번호 확인 입력 필드
            HelpJobTextField(
                value = uiState.confirmPassword,
                onValueChange = viewModel::updateConfirmPassword,
                label = "",
                placeholder = stringResource(id = R.string.sign_up_confirm_password_hint),
                isPassword = true,
                isError = uiState.confirmPasswordError,
                errorMessage = uiState.confirmPasswordErrorMessage?.let { stringResource(id = it) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            // 다음 버튼
            HelpJobButton(
                text = stringResource(id = R.string.sign_up_next_button),
                onClick = viewModel::signUp,
                enabled = uiState.isInputValid,
                isLoading = uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            )
        }
    }
}