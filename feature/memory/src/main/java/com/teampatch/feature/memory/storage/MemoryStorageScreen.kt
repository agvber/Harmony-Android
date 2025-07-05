package com.teampatch.feature.memory.storage

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.compose.AsyncImage
import com.teampatch.core.designsystem.R.drawable.btn_search
import com.teampatch.core.designsystem.R.drawable.ic_chevron_memory_storage
import com.teampatch.core.designsystem.R.drawable.ic_fab_plus
import com.teampatch.core.designsystem.R.drawable.img_test_memory_card
import com.teampatch.core.designsystem.component.AppBar
import com.teampatch.core.designsystem.component.DefaultTextField
import com.teampatch.core.designsystem.component.ItemFloatingButton
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.DP0
import com.teampatch.core.designsystem.theme.DP1
import com.teampatch.core.designsystem.theme.DP10
import com.teampatch.core.designsystem.theme.DP114
import com.teampatch.core.designsystem.theme.DP12
import com.teampatch.core.designsystem.theme.DP14
import com.teampatch.core.designsystem.theme.DP16
import com.teampatch.core.designsystem.theme.DP20
import com.teampatch.core.designsystem.theme.DP4
import com.teampatch.core.designsystem.theme.DP8
import com.teampatch.core.designsystem.theme.FloatingButtonEnterVisibilityAnimation
import com.teampatch.core.designsystem.theme.FloatingButtonExitVisibilityAnimation
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.G2
import com.teampatch.core.designsystem.theme.G3
import com.teampatch.core.designsystem.theme.G5
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.PaddingContentHorizontal
import com.teampatch.core.designsystem.theme.SP15
import com.teampatch.core.designsystem.theme.SP16
import com.teampatch.core.designsystem.theme.SP22
import com.teampatch.core.designsystem.theme.SubRed
import com.teampatch.core.designsystem.theme.WH
import com.teampatch.core.designsystem.utils.noRippleClickable
import com.teampatch.core.designsystem.utils.previewPlaceholder
import com.teampatch.core.domain.fake.FakeMemoryCard
import com.teampatch.core.domain.model.memory.MemoryCard
import com.teampatch.feature.memory.R
import com.teampatch.feature.memory.storage.model.MemoryCardSort
import com.teampatch.feature.memory.storage.model.MemoryStorageUiState
import java.time.LocalDateTime

@Composable
internal fun MemoryStorageWithViewModel(
    onCreationPageRequest: () -> Unit,
    onDetailPageRequest: (memoryCardId: String) -> Unit,
    viewModel: MemoryStorageViewModel = hiltViewModel(),
) {
    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val memoryCards: List<MemoryCard> by viewModel.memoryCards.collectAsStateWithLifecycle()
    val uiState: MemoryStorageUiState by viewModel.uiState.collectAsStateWithLifecycle()

    MemoryStorageScreen(
        onCreationPageRequest = onCreationPageRequest,
        onDetailPageRequest = onDetailPageRequest,
        onSearchTextChange = viewModel::updateMemoryCardSearchText,
        onSortOptionChange = viewModel::updateMemoryCardSortOption,
        uiState = uiState,
        memoryCards = memoryCards
    )

    LaunchedEffect(Unit) {
        viewModel.memoryStorageEvent
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect {
                Toast.makeText(
                    context,
                    context.getString(R.string.toast_init_load_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}

@Composable
internal fun MemoryStorageScreen(
    onCreationPageRequest: () -> Unit,
    onDetailPageRequest: (memoryCardId: String) -> Unit,
    onSearchTextChange: (String) -> Unit,
    onSortOptionChange: (MemoryCardSort) -> Unit,
    uiState: MemoryStorageUiState,
    memoryCards: List<MemoryCard>,
) {
    val context: Context = LocalContext.current
    val lazyGridState: LazyGridState = rememberLazyGridState()
    var isSearchMode: Boolean by remember { mutableStateOf(false) }
    val isFABShow: Boolean by remember(lazyGridState) {
        derivedStateOf { lazyGridState.canScrollForward }
    }

    Column {
        AppBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PaddingContentHorizontal),
            navigation = {
                if (isSearchMode) {
                    MemoryStorageTextField(
                        onCancelRequest = { isSearchMode = false; onSearchTextChange("") },
                        value = uiState.searchText,
                        onValueChange = onSearchTextChange,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(MainGreen)) { append(uiState.userName) }
                            withStyle(SpanStyle(BL)) { append(stringResource(R.string.memory_storage_text_title)) }
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = SP22
                    )
                }
            },
            actions = {
                if (!isSearchMode) {
                    Image(
                        painter = painterResource(btn_search),
                        contentDescription = null,
                        modifier = Modifier.noRippleClickable { isSearchMode = true }
                    )
                }
            }
        )
        MemoryStorageFilterTab(
            onSortOptionChange = { onSortOptionChange(it); lazyGridState.requestScrollToItem(0) },
            sortOption = uiState.sortOption,
            modifier = Modifier.padding(bottom = DP20)
        )
        HorizontalDivider(thickness = DP1, color = G3)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = lazyGridState,
            verticalArrangement = Arrangement.spacedBy(DP12),
            horizontalArrangement = Arrangement.spacedBy(DP12),
            modifier = Modifier
                .fillMaxSize()
                .background(G1)
                .padding(top = DP16, bottom = DP16)
                .padding(horizontal = PaddingContentHorizontal)
        ) {
            items(
                items = memoryCards,
                key = { it.id },
            ) { currentItem ->
                Column(
                    modifier = Modifier
                        .background(WH)
                        .border(DP1, G2)
                        .noRippleClickable { onDetailPageRequest(currentItem.id) }
                ) {
                    AsyncImage(
                        model = currentItem.imageUrl ?: currentItem.imageUri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        placeholder = previewPlaceholder(img_test_memory_card),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(DP114)
                    )
                    Column(
                        modifier = Modifier.padding(vertical = DP10, horizontal = DP14)
                    ) {
                        Text(
                            text = currentItem.writerTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = SP16,
                            color = BL,
                            maxLines = 1
                        )
                        Text(
                            text = currentItem.dateTime.toStringResource(context),
                            fontWeight = FontWeight.Medium,
                            fontSize = SP15,
                            color = G5,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = isFABShow,
            enter = FloatingButtonEnterVisibilityAnimation,
            exit = FloatingButtonExitVisibilityAnimation,
            modifier = Modifier
                .align(Alignment.BottomEnd)
        ) {
            ItemFloatingButton(
                modifier = Modifier
                    .padding(DP20)
                    .noRippleClickable(onClick = onCreationPageRequest)
            ) {
                Image(
                    painter = painterResource(id = ic_fab_plus),
                    contentDescription = stringResource(R.string.fab_memory_add)
                )
            }
        }
    }
}

private fun LocalDateTime.toStringResource(context: Context): String {
    return context.getString(R.string.text_memory_card_date_format, year, monthValue, dayOfMonth)
}

private fun MemoryCardSort.toStringResource(): Int {
    return when (this) {
        MemoryCardSort.OLDEST -> R.string.text_item_sort_oldest
        MemoryCardSort.LATEST -> R.string.text_item_sort_latest
        MemoryCardSort.NAME -> R.string.text_item_sort_name
    }
}

@Composable
fun MemoryStorageTextField(
    onCancelRequest: () -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        DefaultTextField(
            value = value,
            onValueChange = onValueChange,
            hint = {
                Text(
                    text = stringResource(R.string.text_field_search_hint),
                    color = G3,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            },
            textStyle = TextStyle(
                color = BL,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            ),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = stringResource(R.string.button_search_cancel),
            color = SubRed,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(start = DP8)
                .noRippleClickable(onClick = onCancelRequest)
        )
    }
}

@Composable
private fun MemoryStorageFilterTab(
    onSortOptionChange: (MemoryCardSort) -> Unit,
    sortOption: MemoryCardSort,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var dropdownMenuFloatOffset by remember { mutableFloatStateOf(0f) }
    val dropdownMenuDpOffset by remember(dropdownMenuFloatOffset) {
        derivedStateOf {
            with(density) { dropdownMenuFloatOffset.toDp() }
        }
    }

    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = DP20),
    ) {
        Text(
            text = stringResource(sortOption.toStringResource()),
            color = MainGreen,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(end = DP4)
                .onGloballyPositioned {
                    dropdownMenuFloatOffset = it.parentCoordinates?.positionInRoot()?.x ?: 0f
                }
                .noRippleClickable {
                    isDropdownExpanded = true
                }
        )
        Image(
            painter = painterResource(ic_chevron_memory_storage),
            contentDescription = null,
            modifier = Modifier
                .size(DP14)
        )
        DropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false },
            offset = DpOffset(dropdownMenuDpOffset, DP0)
        ) {
            MemoryCardSort.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(stringResource(option.toStringResource())) },
                    onClick = {
                        onSortOptionChange(option)
                        isDropdownExpanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MemoryStorageFilterTabPreview() {
    HarmonyTheme {
        MemoryStorageFilterTab(
            onSortOptionChange = {},
            sortOption = MemoryCardSort.LATEST,
        )
    }
}

@Preview
@Composable
private fun MemoryStorageTextFieldPreview() {
    HarmonyTheme {
        MemoryStorageTextField({}, "", { _ -> })
    }
}

@Preview(showBackground = true)
@Composable
private fun MemoryStorageScreenPreview() {
    HarmonyTheme {
        MemoryStorageScreen(
            onCreationPageRequest = {},
            onDetailPageRequest = { },
            onSearchTextChange = {},
            onSortOptionChange = {},
            uiState = MemoryStorageUiState("여정"),
            memoryCards = FakeMemoryCard().get()
        )
    }
}