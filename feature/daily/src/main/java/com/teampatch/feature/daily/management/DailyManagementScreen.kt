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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.em
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.teampatch.core.designsystem.R.drawable.ic_more_question
import com.teampatch.core.designsystem.component.BackButtonAppBar
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.DP12
import com.teampatch.core.designsystem.theme.DP16
import com.teampatch.core.designsystem.theme.DP26
import com.teampatch.core.designsystem.theme.DP4
import com.teampatch.core.designsystem.theme.DP8
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.G2
import com.teampatch.core.designsystem.theme.G4
import com.teampatch.core.designsystem.theme.G5
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.RoundedCornerShape10
import com.teampatch.core.designsystem.theme.SP20
import com.teampatch.core.designsystem.theme.SP21
import com.teampatch.core.designsystem.theme.SP24
import com.teampatch.core.designsystem.theme.WH
import com.teampatch.core.domain.fake.FakeRoutines
import java.time.DayOfWeek
import com.teampatch.core.domain.model.routine.Routine
import com.teampatch.feature.daily.R
import com.teampatch.feature.daily.management.component.DailyManagementDropDown
import com.teampatch.feature.daily.management.model.DailyManagementEvent
import com.teampatch.feature.daily.management.model.DropDownOption
import java.time.LocalTime

@Composable
internal fun DailyManagementScreenWithViewModel(
    onBackRequest: () -> Unit,
    onEditPageRequest: (dailyId: String) -> Unit,
    viewModel: DailyManagementViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val routines by viewModel.routines.collectAsStateWithLifecycle()

    DailyManagementScreen(
        onBackRequest = onBackRequest,
        onEditDailyRequest = onEditPageRequest,
        onDeleteDailyRequest = viewModel::deleteRoutine,
        todos = routines
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

                DailyManagementEvent.RoutineDeleteFailure -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_management_item_delete_error),
                        Toast.LENGTH_SHORT
                    ).show()
                    onBackRequest()
                }
            }
        }
    }
}

@Composable
internal fun DailyManagementScreen(
    onBackRequest: () -> Unit,
    onEditDailyRequest: (dailyId: String) -> Unit,
    onDeleteDailyRequest: (dailyId: String) -> Unit,
    todos: List<Routine>,
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
                }
                .also { isDropDownMenuShow = false }
                ?: showToastItemIndexOutOfBoundsError(context)
        },
        isDropDownMenuShow = isDropDownMenuShow,
        offset = dropDownOffset,
        modifier = Modifier.onSizeChanged { dropDownLayoutSize = it }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WH)
    ) {
        BackButtonAppBar(
            onBackRequest = { isDropDownMenuShow = false; onBackRequest() },
            title = { Text(stringResource(R.string.text_management_appbar)) }
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(DP12),
            modifier = Modifier
                .fillMaxSize()
                .background(G1)
                .padding(horizontal = dimensionResource(R.dimen.padding_root_20))
        ) {
            item { Box(modifier = Modifier.height(DP8)) }

            items(items = todos, key = { it.id }) { currentItem ->
                Box {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                dimensionResource(R.dimen.size_stroke_1),
                                G2,
                                RoundedCornerShape10
                            )
                            .background(WH, RoundedCornerShape10)
                            .clip(RoundedCornerShape10)
                            .padding(dimensionResource(R.dimen.padding_root_20))
                    ) {
                        Text(
                            text = context.periodStringFormat(
                                currentItem.daysOfWeekPeriod,
                                currentItem.periodTime
                            ),
                            fontWeight = FontWeight.Medium,
                            fontSize = SP20,
                            color = G4,
                            maxLines = 1,
                            lineHeight = SP21
                        )
                        Text(
                            text = currentItem.name,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = SP24,
                            color = BL,
                            lineHeight = 1.4.em,
                            maxLines = 2,
                            modifier = Modifier.padding(top = DP4)
                        )
                    }
                    IconButton(
                        onClick = {
                            with(density) { dropDownIndex = todos.indexOf(currentItem) }
                            isDropDownMenuShow = true
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(DP16)
                            .size(DP26)
                    ) {
                        Icon(
                            painter = painterResource(ic_more_question),
                            contentDescription = null,
                            tint = G5,
                            modifier = Modifier
                                .onGloballyPositioned { coordinates ->
                                    dropDownItemLayoutOffset.put(
                                        todos.indexOf(currentItem),
                                        coordinates.positionInRoot()
                                    )
                                }
                                .size(DP4, DP16)
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

private fun Context.periodStringFormat(
    daysOfWeek: Set<DayOfWeek>,
    time: LocalTime
): String {

    val daysOfWeek: String = if (daysOfWeek.size == 7) getString(R.string.everyday)
    else daysOfWeek.joinToString(", ") { it.toStringFormat(this) }

    val hour: String = if (time.hour <= 12) {
        getString(R.string.am)
    } else {
        getString(R.string.pm)
    }
        .let { it + " " + time.hour + getString(R.string.hour) }

    return getString(R.string.text_management_date_format, daysOfWeek, hour)
}

private fun DayOfWeek.toStringFormat(context: Context): String = when (this) {
    DayOfWeek.MONDAY -> context.getString(R.string.monday)
    DayOfWeek.TUESDAY -> context.getString(R.string.tuesday)
    DayOfWeek.WEDNESDAY -> context.getString(R.string.wednesday)
    DayOfWeek.THURSDAY -> context.getString(R.string.thursday)
    DayOfWeek.FRIDAY -> context.getString(R.string.friday)
    DayOfWeek.SATURDAY -> context.getString(R.string.saturday)
    DayOfWeek.SUNDAY -> context.getString(R.string.sunday)
}


@Preview
@Composable
private fun DailyManagementScreenPreview() {
    HarmonyTheme {
        DailyManagementScreen(
            onBackRequest = {},
            onEditDailyRequest = {},
            onDeleteDailyRequest = {},
            todos = FakeRoutines().get()
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