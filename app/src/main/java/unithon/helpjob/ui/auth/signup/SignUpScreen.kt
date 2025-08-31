package unithon.helpjob.ui.auth.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import unithon.helpjob.R
import unithon.helpjob.data.repository.LanguageAwareScreen
import unithon.helpjob.ui.auth.components.AuthEmailTextField
import unithon.helpjob.ui.auth.components.AuthPasswordTextField
import unithon.helpjob.ui.auth.components.AuthVerificationCodeTextField
import unithon.helpjob.ui.components.HelpJobButton
import unithon.helpjob.ui.components.HelpJobTopAppBar
import unithon.helpjob.ui.theme.Grey000
import unithon.helpjob.ui.theme.Grey200
import unithon.helpjob.ui.theme.Grey400
import unithon.helpjob.ui.theme.Grey700
import unithon.helpjob.ui.theme.HelpJobTheme
import unithon.helpjob.ui.theme.Primary500
import unithon.helpjob.ui.theme.Warning

@Composable
fun SignUpScreen(
    onNavigateToNicknameSetup: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.snackbarMessage.collect { messageRes ->
            snackbarHostState.showSnackbar(
                message = context.getString(messageRes)
            )
        }
    }

    // 회원가입 성공시 네비게이션
    LaunchedEffect(uiState.isSignUpSuccessful) {
        if (uiState.isSignUpSuccessful) {
            onNavigateToNicknameSetup()
        }
    }

    SignUpScreenContent(
        uiState = uiState,
        onUpdateEmail = viewModel::updateEmail,
        onUpdatePassword = viewModel::updatePassword,
        onUpdateConfirmPassword = viewModel::updateConfirmPassword,
        onUpdateVerificationCode = viewModel::updateVerificationCode,
        onSendEmailVerification = viewModel::sendEmailVerification,
        onVerifyEmailCode = viewModel::verifyEmailCode,
        onResendEmailVerification = viewModel::resendEmailVerification,
        onProceedToNickname = viewModel::proceedToNickname,
        onBack = onBack,
        modifier = modifier,
        snackbarHostState = snackbarHostState  // 🆕 추가
    )
}

@Composable
private fun SignUpScreenContent(
    uiState: SignUpViewModel.SignUpUiState,
    onUpdateEmail: (String) -> Unit,
    onUpdatePassword: (String) -> Unit,
    onUpdateConfirmPassword: (String) -> Unit,
    onUpdateVerificationCode: (String) -> Unit,
    onSendEmailVerification: () -> Unit,
    onVerifyEmailCode: () -> Unit,
    onResendEmailVerification: () -> Unit,
    onProceedToNickname: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }  // 🆕 파라미터 추가
) {
    // 레이블 텍스트 높이를 동적으로 계산 + 레이블과 텍스트필드 사이 간격
    val labelHeight = with(LocalDensity.current) {
        MaterialTheme.typography.titleSmall.lineHeight.toDp()
    }
    val labelSpacing = 9.dp // 레이블과 텍스트필드 사이 간격
    val totalOffset = labelHeight + labelSpacing

    LanguageAwareScreen {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                HelpJobTopAppBar(
                    title = R.string.sign_up_top_bar_title,
                    onBack = onBack
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }  // 🆕 추가
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(top = 19.dp, start = 20.dp, end = 20.dp),
            ) {
                // 제목
                Text(
                    text = stringResource(id = R.string.sign_up_subtitle),
                    style = MaterialTheme.typography.headlineLarge,
                    color = Grey700
                )

                Spacer(modifier = Modifier.height(40.dp))

                // 이메일 입력 필드 + send 버튼
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    AuthEmailTextField(
                        value = uiState.email,
                        onValueChange = onUpdateEmail,
                        labelText = stringResource(R.string.sign_up_email_label),
                        placeholderText = stringResource(R.string.sign_up_email_hint),
                        isError = uiState.emailError,
                        errorMessage = uiState.emailErrorMessage?.let { stringResource(id = it) },
                        modifier = Modifier.weight(1f),
                    )

                    // send 버튼 (레이블 높이 + 간격만큼 offset)
                    Button(
                        onClick = onSendEmailVerification,
                        enabled = uiState.isEmailValid && !uiState.isSendingEmail && !uiState.isEmailSent,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (uiState.isEmailValid && !uiState.isSendingEmail && !uiState.isEmailSent) Primary500 else Grey200,
                            contentColor = if (uiState.isEmailValid && !uiState.isSendingEmail && !uiState.isEmailSent) Grey000 else Grey400,
                            disabledContainerColor = Grey200,
                            disabledContentColor = Grey400
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp),
                        modifier = Modifier
                            .width(71.dp)
                            .height(48.dp)
                            .offset(y = totalOffset)
                    ) {
                        if (uiState.isSendingEmail) {
                            Text(
                                text = "...",
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1
                            )
                        } else {
                            Text(
                                text = if (uiState.isEmailSent) "sent" else "send",
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1
                            )
                        }
                    }
                }

                // 이메일 전송 후 인증코드 입력 섹션
                if (uiState.isEmailSent) {
                    Spacer(modifier = Modifier.height(20.dp))

                    // 인증코드 입력 필드 + verify 버튼
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        // 인증코드 텍스트필드 (내부에 Resend 포함)
                        Box(modifier = Modifier.weight(1f)) {
                            AuthVerificationCodeTextField(
                                value = uiState.verificationCode,
                                onValueChange = onUpdateVerificationCode,
                                placeholderText = stringResource(id = R.string.verification_code_hint),
                                isError = uiState.verificationCodeError,
                                errorMessage = uiState.verificationCodeErrorMessage?.let { stringResource(id = it) },
                                modifier = Modifier.fillMaxWidth(),
                            )

                            // Resend 텍스트를 텍스트필드 내부 오른쪽에 배치
                            if (uiState.verificationCodeError || uiState.verificationCodeErrorMessage == R.string.verification_code_expired) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(R.string.resend_button),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Warning,
                                        modifier = Modifier
                                            .padding(end = 18.dp)
                                            .clickable { onResendEmailVerification() }
                                    )
                                }
                            }
                        }

                        // verify 버튼 (인증코드 필드는 레이블이 없으므로 패딩 없음)
                        Button(
                            onClick = onVerifyEmailCode,
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
                                .height(48.dp)
                        ) {
                            if (uiState.isVerifyingCode) {
                                Text(
                                    text = "...",
                                    style = MaterialTheme.typography.titleMedium,
                                    maxLines = 1
                                )
                            } else {
                                Text(
                                    text = "verify",
                                    style = MaterialTheme.typography.titleMedium,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    // 인증번호 안내 메시지 (정상 상태일 때만)
                    if (!uiState.verificationCodeError) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.verification_code_instruction),
                            style = MaterialTheme.typography.labelMedium,
                            color = Primary500,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                // 비밀번호 입력 필드
                AuthPasswordTextField(
                    value = uiState.password,
                    onValueChange = onUpdatePassword,
                    labelText = stringResource(R.string.sign_up_password_label),
                    placeholderText = stringResource(R.string.sign_up_password_hint),
                    isError = uiState.passwordError,
                    errorMessage = uiState.passwordErrorMessage?.let { stringResource(it) }
                )

                Spacer(modifier = Modifier.height(15.dp))

                // 비밀번호 확인 입력 필드
                AuthPasswordTextField(
                    value = uiState.confirmPassword,
                    onValueChange = onUpdateConfirmPassword,
                    labelText = stringResource(R.string.sign_up_confirm_password_label),
                    placeholderText = stringResource(R.string.sign_up_confirm_password_hint),
                    isError = uiState.confirmPasswordError,
                    errorMessage = uiState.confirmPasswordErrorMessage?.let { stringResource(it) },
                    imeAction = ImeAction.Done
                )

                Spacer(modifier = Modifier.weight(1f))

                // 다음 버튼
                HelpJobButton(
                    text = stringResource(id = R.string.sign_up_next_button),
                    onClick = onProceedToNickname,
                    enabled = uiState.isInputValid,
                    isLoading = uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                )
            }
        }
    }
}

// =================================
// 프리뷰들
// =================================

// 기본 상태 프리뷰
@Preview(
    name = "기본 상태",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun SignUpScreenPreview() {
    HelpJobTheme {
        SignUpScreenContent(
            uiState = SignUpViewModel.SignUpUiState(),
            onUpdateEmail = {},
            onUpdatePassword = {},
            onUpdateConfirmPassword = {},
            onUpdateVerificationCode = {},
            onSendEmailVerification = {},
            onVerifyEmailCode = {},
            onResendEmailVerification = {},
            onProceedToNickname = {},
            onBack = {}
        )
    }
}

// 이메일 전송 후 상태 프리뷰
@Preview(
    name = "이메일 전송 후",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun SignUpScreenEmailSentPreview() {
    HelpJobTheme {
        SignUpScreenContent(
            uiState = SignUpViewModel.SignUpUiState(
                email = "test@example.com",
                isEmailSent = true,
                verificationCode = "123456"
            ),
            onUpdateEmail = {},
            onUpdatePassword = {},
            onUpdateConfirmPassword = {},
            onUpdateVerificationCode = {},
            onSendEmailVerification = {},
            onVerifyEmailCode = {},
            onResendEmailVerification = {},
            onProceedToNickname = {},
            onBack = {}
        )
    }
}

// 에러 상태 프리뷰
@Preview(
    name = "에러 상태",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun SignUpScreenErrorPreview() {
    HelpJobTheme {
        SignUpScreenContent(
            uiState = SignUpViewModel.SignUpUiState(
                email = "invalid-email",
                password = "123",
                confirmPassword = "456",
                verificationCode = "wrong",
                isEmailSent = true,
                emailError = true,
                emailErrorMessage = R.string.error_invalid_email,
                passwordError = true,
                passwordErrorMessage = R.string.error_short_password,
                confirmPasswordError = true,
                confirmPasswordErrorMessage = R.string.error_password_mismatch,
                verificationCodeError = true,
                verificationCodeErrorMessage = R.string.verification_code_invalid
            ),
            onUpdateEmail = {},
            onUpdatePassword = {},
            onUpdateConfirmPassword = {},
            onUpdateVerificationCode = {},
            onSendEmailVerification = {},
            onVerifyEmailCode = {},
            onResendEmailVerification = {},
            onProceedToNickname = {},
            onBack = {}
        )
    }
}

// 입력 완료 상태 프리뷰
@Preview(
    name = "입력 완료",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun SignUpScreenCompletedPreview() {
    HelpJobTheme {
        SignUpScreenContent(
            uiState = SignUpViewModel.SignUpUiState(
                email = "user@example.com",
                password = "password123",
                confirmPassword = "password123",
                verificationCode = "123456",
                isEmailSent = true,
                isCodeVerified = true
            ),
            onUpdateEmail = {},
            onUpdatePassword = {},
            onUpdateConfirmPassword = {},
            onUpdateVerificationCode = {},
            onSendEmailVerification = {},
            onVerifyEmailCode = {},
            onResendEmailVerification = {},
            onProceedToNickname = {},
            onBack = {}
        )
    }
}