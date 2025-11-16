package unithon.helpjob.ui.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import helpjob.composeapp.generated.resources.Res
import helpjob.composeapp.generated.resources.setting_withdrawal
import unithon.helpjob.data.network.ApiConstants
import unithon.helpjob.ui.components.HelpJobTopAppBar
import unithon.helpjob.ui.components.UrlWebView

@Composable
fun WithdrawalScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        HelpJobTopAppBar(
            title = Res.string.setting_withdrawal,  // "탈퇴" / "Account Deletion"
            onBack = onBack
        )

        UrlWebView(
            url = ApiConstants.WITHDRAWAL_FORM_URL,
            modifier = Modifier.fillMaxSize()
        )
    }
}
