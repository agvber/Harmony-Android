package com.teampatch.feature.daily.management

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.teampatch.core.common.getOrNull
import com.teampatch.core.designsystem.R.drawable.ic_more_question
import com.teampatch.core.designsystem.component.BackButtonAppBar
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.G2
import com.teampatch.core.designsystem.theme.G4
import com.teampatch.core.designsystem.theme.G5
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.WH
import com.teampatch.core.domain.fake.FakeTodos
import com.teampatch.core.domain.model.Todo
import com.teampatch.feature.daily.R
import com.teampatch.feature.daily.management.component.DailyManagementDropDown
import com.teampatch.feature.daily.management.model.DailyManagementEvent
import com.teampatch.feature.daily.management.model.DropDownOption
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun DailyManagementScreenWithViewModel(
    onBackRequest: () -> Unit,
    onEditPageRequest: (dailyId: String) -> Unit,
    viewModel: DailyManagementViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val todos: LazyPagingItems<Todo> = viewModel.todos.collectAsLazyPagingItems()

    DailyManagementScreen(
        onBackRequest = onBackRequest,
        onEditDailyRequest = onEditPageRequest,
        onDeleteDailyRequest = viewModel::deleteTodo,
        todos = todos
    )

    LaunchedEffect(Unit) {
        viewModel.event.flowWithLifecycle(lifecycleOwner.lifecycle).collect {
            when (it) {
                is DailyManagementEvent.LoadError ->
                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_management_load_error),
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
    }
}

@Composable
internal fun DailyManagementScreen(
    onBackRequest: () -> Unit,
    onEditDailyRequest: (dailyId: String) -> Unit,
    onDeleteDailyRequest: (dailyId: String) -> Unit,
    todos: LazyPagingItems<Todo>,
) {
    val context: Context = LocalContext.current
    val density: Density = LocalDensity.current
    var isDropDownMenuShow: Boolean by remember { mutableStateOf(false) }
    var dropDownItemLayoutOffset = remember { mutableStateMapOf<Int, Offset>() }
    var dropDownLayoutSize: IntSize by remember { mutableStateOf(IntSize.Zero) }
    var dropDownIndex: Int? by remember { mutableStateOf<Int?>(null) }
    val dropDownOffset by remember(dropDownItemLayoutOffset, dropDownIndex, dropDownLayoutSize) {
        derivedStateOf {
            with(density) {
                val index = dropDownIndex ?: return@with DpOffset.Zero
                val x = dropDownItemLayoutOffset.getOrDefault(index, null)?.x?.toDp()
                    ?: return@with DpOffset.Zero
                val y = dropDownItemLayoutOffset.getOrDefault(index, null)?.y?.toDp()
                    ?: return@with DpOffset.Zero
                DpOffset(
                    x = x - dropDownLayoutSize.width.toDp(),
                    y = y + dropDownLayoutSize.height.toDp()
                )
            }
        }
    }

    DailyManagementDropDown(
        onDismissRequest = { isDropDownMenuShow = false },
        onSelectItem = { option ->
            dropDownIndex
                ?.let { todos.getOrNull(it) }
                ?.let {
                    when (option) {
                        DropDownOption.EDIT -> onEditDailyRequest(it.id)
                        DropDownOption.DELETE -> onDeleteDailyRequest(it.id)
                    }
                } ?: showToastItemIndexOutOfBoundsError(context)
        },
        isDropDownMenuShow = isDropDownMenuShow,
        offset = dropDownOffset,
        modifier = Modifier.onSizeChanged { dropDownLayoutSize = it }
    )

    Scaffold(
        topBar = {
            BackButtonAppBar(
                onBackRequest = onBackRequest,
                title = { Text(stringResource(R.string.text_management_appbar)) }
            )
        }
    ) { scaffoldPaddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPaddingValues)
                .background(G1)
                .padding(horizontal = dimensionResource(R.dimen.padding_root_20))
        ) {
            item { Box(modifier = Modifier.height(8.dp)) }

            items(count = todos.itemCount, key = todos.itemKey()) { index ->
                val currentItem = todos[index] ?: return@items
                Box {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, G2, RoundedCornerShape10)
                            .background(WH, RoundedCornerShape10)
                            .clip(RoundedCornerShape10)
                            .padding(dimensionResource(R.dimen.padding_root_20))
                    ) {
                        Text(
                            text = "월,수,금 / 오전 11시",
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp,
                            color = G4,
                            maxLines = 1,
                            lineHeight = 21.sp
                        )
                        Text(
                            text = currentItem.title,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 24.sp,
                            color = BL,
                            lineHeight = 1.4.em,
                            maxLines = 2,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    IconButton(
                        onClick = {
                            with(density) { dropDownIndex = index }
                            isDropDownMenuShow = true
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .size(26.dp)
                    ) {
                        Icon(
                            painter = painterResource(ic_more_question),
                            contentDescription = null,
                            tint = G5,
                            modifier = Modifier
                                .onGloballyPositioned { coordinates ->
                                    dropDownItemLayoutOffset.put(
                                        index,
                                        coordinates.positionInRoot()
                                    )
                                }
                                .size(4.dp, 16.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun showToastItemIndexOutOfBoundsError(context: Context) {
    Toast.makeText(
        context,
        context.getString(R.string.toast_management_item_select_error),
        Toast.LENGTH_SHORT
    )
        .show()
}

@Preview
@Composable
private fun DailyManagementScreenPreview() {
    HarmonyTheme {
        DailyManagementScreen(
            onBackRequest = {},
            onEditDailyRequest = {},
            onDeleteDailyRequest = {},
            todos = flowOf(PagingData.from(FakeTodos().get()))
                .collectAsLazyPagingItems()
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DailyManagementDropDownPreview() {
    HarmonyTheme {
        DailyManagementDropDown(
            onDismissRequest = {},
            onSelectItem = {},
            isDropDownMenuShow = true
        )
    }
}