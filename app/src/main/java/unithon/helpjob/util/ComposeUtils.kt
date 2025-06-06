package unithon.helpjob.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//
///**
// * 화면의 로딩 상태와 빈 상태를 관리하는 유틸리티
// */
//@Composable
//fun LoadingContent(
//    loading: Boolean,
//    empty: Boolean,
//    emptyContent: @Composable () -> Unit,
//    modifier: Modifier = Modifier,
//    content: @Composable () -> Unit
//) {
//    when {
//        empty -> emptyContent()
//        else -> content()
//    }
//}


fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember {
            MutableInteractionSource()
        }) {
        onClick()
    }
}