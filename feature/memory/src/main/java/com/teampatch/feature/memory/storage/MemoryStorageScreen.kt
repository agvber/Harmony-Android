package com.teampatch.feature.memory.storage

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.teampatch.core.designsystem.R.drawable.btn_search
import com.teampatch.core.designsystem.R.drawable.ic_chevron_memory_storage
import com.teampatch.core.designsystem.R.drawable.ic_fab_plus
import com.teampatch.core.designsystem.R.drawable.img_test_memory_card
import com.teampatch.core.designsystem.component.AppBar
import com.teampatch.core.designsystem.component.DefaultTextField
import com.teampatch.core.designsystem.component.ItemFloatingButton
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.G2
import com.teampatch.core.designsystem.theme.G3
import com.teampatch.core.designsystem.theme.G5
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.SubRed
import com.teampatch.core.designsystem.theme.WH
import com.teampatch.core.designsystem.utils.noRippleClickable
import com.teampatch.core.designsystem.utils.previewPlaceholder
import com.teampatch.core.domain.fake.FakeMemoryCard
import com.teampatch.core.domain.model.MemoryCard
import com.teampatch.feature.memory.R
import com.teampatch.feature.memory.storage.model.MemoryCardSort
import com.teampatch.feature.memory.storage.model.MemoryStorageUiState
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDateTime

@Composable
internal fun MemoryStorageWithViewModel(
    onCreationPageRequest: () -> Unit,
    onDetailPageRequest: (memoryCardId: String) -> Unit,
    viewModel: MemoryStorageViewModel = hiltViewModel(),
) {
    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val memoryCards: LazyPagingItems<MemoryCard> = viewModel.memoryCards.collectAsLazyPagingItems()
    val uiState: MemoryStorageUiState by viewModel.uiState.collectAsStateWithLifecycle()

    MemoryStorageScreen(
        onCreationPageRequest = onCreationPageRequest,
        onDetailPageRequest = onDetailPageRequest,
        onSearchTextChange = viewModel::updateMemoryCardSearchText,
        onSortOptionChange = viewModel::updateMemoryCardSortOption,
        uiState = uiState,
        memoryCardsLazyItems = memoryCards
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
    memoryCardsLazyItems: LazyPagingItems<MemoryCard>,
) {
    val context: Context = LocalContext.current
    var isSearchMode by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.padding(bottom = 20.dp)) {
                AppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    navigation = {
                        if (isSearchMode) {
                            MemoryStorageTextField(
                                onCancelRequest = { isSearchMode = false },
                                value = uiState.searchText,
                                onValueChange = onSearchTextChange,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(SpanStyle(MainGreen)) { append(uiState.userName) }
                                    withStyle(SpanStyle(BL)) { append(stringResource(R.string.text_title_appbar)) }
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )
                        }
                    },
                    actions = {
                        if (!isSearchMode) {
                            Image(
                                painter = painterResource(btn_search),
                                contentDescription = null,
                                modifier = Modifier.noRippleClickable {
                                    isSearchMode = true
                                }
                            )
                        }
                    }
                )
                MemoryStorageFilterTab(
                    onSortOptionChange = onSortOptionChange,
                    sortOption = uiState.sortOption,
                )
            }
        },
        floatingActionButton = {
            ItemFloatingButton(
                modifier = Modifier.noRippleClickable(onClick = onCreationPageRequest)
            ) {
                Image(
                    painter = painterResource(id = ic_fab_plus),
                    contentDescription = stringResource(R.string.fab_memory_add)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { scaffoldPaddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPaddingValues)
                .background(G1)
                .padding(top = 16.dp, bottom = 16.dp)
                .padding(horizontal = 20.dp)
        ) {
            items(
                count = memoryCardsLazyItems.itemCount,
                key = memoryCardsLazyItems.itemKey(),
            ) { index ->
                val currentItem = memoryCardsLazyItems[index]
                Column(
                    modifier = Modifier
                        .background(WH)
                        .border(1.dp, G2)
                        .noRippleClickable {
                            currentItem?.let { onDetailPageRequest(it.id) }
                                ?: Toast.makeText(
                                    context,
                                    context.getString(R.string.toast_item_empty_error),
                                    Toast.LENGTH_SHORT
                                ).show()
                        }
                ) {
                    AsyncImage(
                        model = currentItem?.imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        placeholder = previewPlaceholder(img_test_memory_card),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(114.dp)
                    )
                    Column(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 14.dp)
                    ) {
                        Text(
                            text = currentItem?.text ?: "",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = BL,
                            maxLines = 1
                        )
                        Text(
                            text = currentItem?.dateTime?.toStringResource(context) ?: "",
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            color = G5,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
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
                .padding(start = 8.dp)
                .noRippleClickable(onClick = onCancelRequest)
        )
    }
}

@Composable
private fun MemoryStorageFilterTab(
    onSortOptionChange: (MemoryCardSort) -> Unit,
    sortOption: MemoryCardSort,
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        Text(
            text = stringResource(sortOption.toStringResource()),
            color = MainGreen,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(end = 4.dp)
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
                .size(14.dp)
        )
        DropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false },
            offset = DpOffset(dropdownMenuDpOffset, 0.dp)
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
            memoryCardsLazyItems = flowOf(
                PagingData.from(FakeMemoryCard().get())
            ).collectAsLazyPagingItems()
        )
    }
}