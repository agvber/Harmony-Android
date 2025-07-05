package com.teampatch.feature.question.detail

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.teampatch.core.designsystem.R.drawable.ic_more_question
import com.teampatch.core.designsystem.R.drawable.ic_my_appbar
import com.teampatch.core.designsystem.component.BackButtonAppBar
import com.teampatch.core.designsystem.component.RoundButton
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.DP12
import com.teampatch.core.designsystem.theme.DP16
import com.teampatch.core.designsystem.theme.DP20
import com.teampatch.core.designsystem.theme.DP200
import com.teampatch.core.designsystem.theme.DP24
import com.teampatch.core.designsystem.theme.DP32
import com.teampatch.core.designsystem.theme.DP36
import com.teampatch.core.designsystem.theme.DP40
import com.teampatch.core.designsystem.theme.DP68
import com.teampatch.core.designsystem.theme.DP8
import com.teampatch.core.designsystem.theme.EnterVisibilityAnimation
import com.teampatch.core.designsystem.theme.ExitVisibilityAnimation
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.G3
import com.teampatch.core.designsystem.theme.G5
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.RoundedCornerShape10
import com.teampatch.core.designsystem.theme.SP18
import com.teampatch.core.designsystem.theme.SP20
import com.teampatch.core.designsystem.theme.SP22
import com.teampatch.core.designsystem.theme.SP24
import com.teampatch.core.designsystem.theme.WH
import com.teampatch.core.designsystem.utils.noRippleClickable
import com.teampatch.core.domain.fake.FakeQuestionComments
import com.teampatch.core.domain.fake.FakeQuestionDetail
import com.teampatch.core.domain.model.question.QuestionComment
import com.teampatch.feature.question.R
import com.teampatch.feature.question.detail.component.CommentEditorDialog
import com.teampatch.feature.question.detail.component.CommentEditorState
import com.teampatch.feature.question.detail.component.QuestionDetailComment
import com.teampatch.feature.question.detail.mapper.toPresentationModel
import com.teampatch.feature.question.detail.model.CommentEvent
import com.teampatch.feature.question.detail.model.PostEvent
import com.teampatch.feature.question.detail.model.QuestionDetailEvent
import com.teampatch.feature.question.detail.model.QuestionDetailUiState

@Composable
internal fun QuestionDetailRoute(
    onBackRequest: () -> Unit,
    answerEditPageRequest: (questionId: String) -> Unit,
    viewModel: QuestionDetailViewModel = hiltViewModel(),
) {
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val context: Context = LocalContext.current
    val uiState: QuestionDetailUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val comments: List<QuestionComment> by viewModel.comments.collectAsStateWithLifecycle()

    if (!uiState.isLoading) {
        QuestionDetailScreen(
            onBackRequest = onBackRequest,
            postEventListener = { answerEditPageRequest(viewModel.questionId) },
            commentEventListener = { event ->
                when (event) {
                    is CommentEvent.Add -> viewModel.addComment(event.commentText)
                    is CommentEvent.Delete -> viewModel.deleteComment(event.commentId)
                    is CommentEvent.Edit -> viewModel.editComment(
                        event.commentId,
                        event.commentText
                    )
                }
            },
            uiState = uiState,
            comments = comments
        )
    }

    LaunchedEffect(Unit) {
        viewModel.event
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { event ->
                when (event) {
                    is QuestionDetailEvent.AddCommentError ->
                        Toast.makeText(
                            context,
                            R.string.question_detail_toast_comment_add_error,
                            Toast.LENGTH_SHORT
                        ).show()

                    is QuestionDetailEvent.DeleteCommentError ->
                        Toast.makeText(
                            context,
                            R.string.question_detail_toast_comment_delete_error,
                            Toast.LENGTH_SHORT
                        ).show()

                    is QuestionDetailEvent.EditCommentError ->
                        Toast.makeText(
                            context,
                            R.string.question_detail_toast_comment_edit_error,
                            Toast.LENGTH_SHORT
                        ).show()

                    is QuestionDetailEvent.LoadError -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.question_detail_toast_init_data_load_error),
                            Toast.LENGTH_SHORT
                        ).show()
                        onBackRequest()
                    }
                }
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QuestionDetailScreen(
    onBackRequest: () -> Unit,
    postEventListener: (PostEvent) -> Unit,
    commentEventListener: (CommentEvent) -> Unit,
    uiState: QuestionDetailUiState,
    comments: List<QuestionComment>
) {
    val lazyColumnState = rememberLazyListState()
    val isFabShow: Boolean by remember(lazyColumnState) {
        derivedStateOf { !lazyColumnState.isScrollInProgress && !lazyColumnState.canScrollBackward }
    }

    var selectedCommentId by rememberSaveable { mutableStateOf("") }
    var selectedCommentText by rememberSaveable { mutableStateOf("") }
    var commentEditorState: CommentEditorState by rememberSaveable {
        mutableStateOf(CommentEditorState.NONE)
    }

    if (commentEditorState != CommentEditorState.NONE) {
        CommentEditorDialog(
            onDismissRequest = {
                commentEditorState = CommentEditorState.NONE
                selectedCommentId = ""
                selectedCommentText = ""
            },
            onCompleteRequest = {
                when (commentEditorState) {
                    CommentEditorState.ADD -> commentEventListener(CommentEvent.Add(it))
                    CommentEditorState.EDIT -> commentEventListener(
                        CommentEvent.Edit(
                            commentId = selectedCommentId,
                            commentText = it
                        )
                    )

                    else -> {}
                }
            },
            commentText = selectedCommentText,
            commentEditorState = CommentEditorState.NONE,
        )
    }

    Column {
        QuestionDetailAppbar(
            onBackRequest = onBackRequest,
            postEventListener = postEventListener,
            isPostWritable = uiState.postWritable
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(G1),
            state = lazyColumnState
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(WH)
                        .padding(top = DP40)
                ) {
                    Text(
                        text = stringResource(
                            R.string.question_detail_text_line_count,
                            uiState.post.number
                        ),
                        fontWeight = FontWeight.Medium,
                        fontSize = SP18,
                        color = MainGreen,
                        modifier = Modifier.padding(horizontal = DP20)
                    )
                    Text(
                        text = uiState.post.title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = SP24,
                        color = BL,
                        modifier = Modifier
                            .padding(top = DP12, bottom = DP8, start = DP20, end = DP20)
                    )
                    Text(
                        text = with(uiState.post.dateTime) {
                            stringResource(
                                R.string.question_main_text_post_date_time,
                                year, monthValue, dayOfMonth
                            )
                        },
                        fontWeight = FontWeight.Medium,
                        fontSize = SP18,
                        color = G3,
                        modifier = Modifier.padding(horizontal = DP20)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = DP20, end = DP20, bottom = DP32, top = DP24)
                            .background(G1, RoundedCornerShape10)
                            .padding(DP24)
                    ) {
                        Text(
                            text = uiState.post.content,
                            fontWeight = FontWeight.Medium,
                            fontSize = SP20,
                            color = G5
                        )
                    }
                }
            }

            item {
                Text(
                    text = stringResource(
                        R.string.question_detail_text_comment_count,
                        comments.size
                    ),
                    fontWeight = FontWeight.Medium,
                    fontSize = SP18,
                    color = G5,
                    modifier = Modifier
                        .padding(start = DP20, top = DP16)
                )
            }

            items(
                items = comments,
                key = { it.commentId }
            ) { currentItem ->
                QuestionDetailComment(
                    onEditCommentRequest = {
                        commentEditorState = CommentEditorState.EDIT
                        selectedCommentId = currentItem.commentId
                        selectedCommentText = currentItem.content
                    },
                    onDeleteCommentRequest = {
                        commentEventListener(CommentEvent.Delete(currentItem.commentId))
                    },
                    comment = currentItem.content,
                    username = currentItem.writerName,
                    profileImage = painterResource(ic_my_appbar),
                    hasWritePermission = uiState.uid == currentItem.writerUid,
                )
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = isFabShow,
            enter = EnterVisibilityAnimation,
            exit = ExitVisibilityAnimation,
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            RoundButton(
                onClick = { commentEditorState = CommentEditorState.ADD },
                modifier = Modifier
                    .padding(bottom = DP24)
                    .size(DP200, DP68)
            ) {
                Text(
                    text = stringResource(R.string.float_add_comment),
                    fontSize = SP22
                )
            }
        }
    }
}

@Composable
private fun QuestionDetailAppbar(
    onBackRequest: () -> Unit,
    postEventListener: (PostEvent) -> Unit,
    isPostWritable: Boolean,
) {
    var answerEventMenuExpanded by remember { mutableStateOf(false) }

    BackButtonAppBar(
        onBackRequest = onBackRequest,
        actions = {
            if (isPostWritable) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(end = DP20)
                        .size(DP36)
                        .noRippleClickable {
                            answerEventMenuExpanded = true
                        }
                ) {
                    Image(
                        painter = painterResource(ic_more_question),
                        contentDescription = "more"
                    )
                }
                DropdownMenu(
                    expanded = answerEventMenuExpanded,
                    onDismissRequest = { answerEventMenuExpanded = false },
                    shape = RoundedCornerShape10,
                    modifier = Modifier
                        .widthIn(min = DP200)
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(R.string.dropdown_edit_answer),
                                fontWeight = FontWeight.Medium,
                                fontSize = SP20,
                                color = BL,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxSize()
                            )
                        },
                        onClick = {
                            postEventListener(PostEvent.EDIT)
                            answerEventMenuExpanded = false
                        }
                    )
                }
            }
        },
        modifier = Modifier
            .background(WH)
    )
}

@Preview
@Composable
private fun QuestionDetailScreenPreview() {
    HarmonyTheme {
        QuestionDetailScreen(
            onBackRequest = {},
            postEventListener = {},
            commentEventListener = {},
            uiState = QuestionDetailUiState(
                post = FakeQuestionDetail().get().toPresentationModel(true),
                isLoading = false
            ),
            comments = FakeQuestionComments().get()
        )
    }
}