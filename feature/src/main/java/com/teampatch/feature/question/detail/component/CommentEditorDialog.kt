package com.teampatch.feature.question.detail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import com.teampatch.core.designsystem.component.DefaultTextField
import com.teampatch.core.designsystem.dialog.InputLargeTextBottomSheetContent
import com.teampatch.core.designsystem.theme.DP12
import com.teampatch.core.designsystem.theme.DP16
import com.teampatch.core.designsystem.theme.DP20
import com.teampatch.core.designsystem.theme.DP204
import com.teampatch.core.designsystem.theme.DP24
import com.teampatch.core.designsystem.theme.G4
import com.teampatch.core.designsystem.theme.SP18
import com.teampatch.core.designsystem.theme.WH
import com.teampatch.feature.R

private const val TEXT_LENGTH_LIMIT = 100

enum class CommentEditorState {
    NONE, ADD, EDIT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CommentEditorDialog(
    onDismissRequest: () -> Unit,
    onCompleteRequest: (String) -> Unit,
    commentText: String,
    commentEditorState: CommentEditorState,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it != SheetValue.Hidden }
    ),
    textLimit: Int = TEXT_LENGTH_LIMIT,
) {
    var text: String by rememberSaveable { mutableStateOf(commentText) }
    val isCompleteButtonEnabled by remember { derivedStateOf { text.isNotBlank() } }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = WH,
        dragHandle = null,
        modifier = modifier
    ) {
        InputLargeTextBottomSheetContent(
            onDismissRequest = onDismissRequest,
            onCompleteRequest = { onCompleteRequest(text); onDismissRequest() },
            title = {
                Text(
                    text = stringResource(
                        when (commentEditorState) {
                            CommentEditorState.EDIT -> R.string.text_title_edit_comment
                            else -> R.string.text_title_add_comment
                        }
                    )
                )
            },
            buttonText = {
                Text(
                    text = stringResource(
                        when (commentEditorState) {
                            CommentEditorState.EDIT -> R.string.btn_complete_edit_comment
                            else -> R.string.btn_complete_add_comment
                        }
                    )
                )

            },
            buttonEnable = isCompleteButtonEnabled
        ) {
            CommentEditorContent(text = text) {
                if (it.length <= textLimit) {
                    text = it
                }
            }
        }
    }
}

@Composable
private fun CommentEditorContent(
    text: String,
    onTextChange: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(start = DP20, end = DP20, top = DP24, bottom = DP12)
            .height(DP204)
    ) {
        DefaultTextField(
            value = text,
            onValueChange = onTextChange,
            singleLine = false,
            maxLines = 6,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.None),
            modifier = Modifier
                .fillMaxSize()
        )
        Text(
            text = stringResource(
                R.string.question_detail_text_comment_limit,
                text.length,
                TEXT_LENGTH_LIMIT
            ),
            fontWeight = FontWeight.Medium,
            fontSize = SP18,
            color = G4,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(vertical = DP16, horizontal = DP20)
        )
    }
}