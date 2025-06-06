package unithon.helpjob.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Popup
import unithon.helpjob.ui.theme.Grey000
import unithon.helpjob.ui.theme.Grey200
import unithon.helpjob.ui.theme.Grey400
import unithon.helpjob.ui.theme.Grey500
import unithon.helpjob.ui.theme.Grey600
import unithon.helpjob.ui.theme.Primary500
import unithon.helpjob.util.noRippleClickable

@Composable
fun <T> HelpJobDropdown(
    modifier: Modifier = Modifier,
    label: String,
    selectedItem: T?,
    items: List<T>,
    onItemSelected: (T) -> Unit,
    itemToString: (T) -> String,
    placeholder: String = "",
    labelTextFieldSpace: Dp = 8.dp,
    isUpward: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }
    var rowSize by remember { mutableStateOf(Size.Zero) }
    val scrollState = rememberScrollState()

    Column(modifier = modifier) {
        // Label
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            color = Grey500
        )

        Spacer(modifier = Modifier.height(labelTextFieldSpace))

        // Dropdown Box
        Box {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .onGloballyPositioned { layoutCoordinates ->
                        rowSize = layoutCoordinates.size.toSize()
                    }
                    .border(
                        width = 1.dp,
                        color = if (expanded) Primary500 else Grey200,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .noRippleClickable { expanded = !expanded }
                    .padding(start = 18.dp, end = 10.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedItem?.let(itemToString) ?: placeholder,
                        style = MaterialTheme.typography.titleSmall,
                        color = Grey600,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Grey600,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Dropdown Menu
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { rowSize.width.toDp() })
                    .height(240.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = Grey200,
                ),
                shape = RoundedCornerShape(10.dp),
                containerColor = Grey000,
                shadowElevation = 0.dp,
                offset = DpOffset(
                    y = if (isUpward) (-(6)).dp else (6).dp,
                    x = 0.dp
                ),
                scrollState = scrollState
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = itemToString(item),
                                style = MaterialTheme.typography.titleSmall,
                                color = Grey600
                            )
                        },
                        onClick = {
                            onItemSelected(item)
                            expanded = false
                        },
                        contentPadding = PaddingValues(
                            vertical = (11.5).dp,
                            horizontal = 12.dp
                        ),
                    )
                }
            }

            // Custom Scrollbar
            if (expanded && items.size > 5) {
                val scrollbarOffset = with(LocalDensity.current) {
                    IntOffset(
                        x = (rowSize.width.toDp() + (-10.dp)).toPx().toInt(),
                        y = if (isUpward) (-(6 + 235)).dp.toPx().toInt() else (6 + 46 + 5).dp.toPx().toInt()
                    )
                }

                Popup(offset = scrollbarOffset) {
                    CustomScrollbar(
                        scrollState = scrollState,
                        dropdownHeight = 230.dp,
                        totalItems = items.size,
                        visibleItems = 5
                    )
                }
            }
        }
    }
}

@Composable
private fun CustomScrollbar(
    scrollState: androidx.compose.foundation.ScrollState,
    dropdownHeight: Dp,
    totalItems: Int,
    visibleItems: Int
) {
    val density = LocalDensity.current
    val scrollbarHeight = dropdownHeight * visibleItems / totalItems
    val maxScrollOffsetPx = with(density) { (48.dp * (totalItems - visibleItems)).toPx() }

    val scrollProgress = if (maxScrollOffsetPx > 0) {
        (scrollState.value / maxScrollOffsetPx).coerceIn(0f, 1f)
    } else 0f

    val scrollbarOffset = (dropdownHeight - scrollbarHeight) * scrollProgress

    // 외부 컨테이너 (DropdownMenu와 동일한 범위)
    Box(modifier = Modifier.width(5.dp).height(dropdownHeight)) {
        // 실제 스크롤바
        Box(
            modifier = Modifier
                .offset(y = scrollbarOffset)
                .width(5.dp)
                .height(scrollbarHeight)
                .background(
                    color = Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(21.dp)
                )
        )
    }
}