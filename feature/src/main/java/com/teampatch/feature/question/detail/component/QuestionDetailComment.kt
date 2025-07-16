package com.teampatch.feature.question.detail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.teampatch.core.R.drawable.ic_more_question
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.DP12
import com.teampatch.core.designsystem.theme.DP16
import com.teampatch.core.designsystem.theme.DP20
import com.teampatch.core.designsystem.theme.DP200
import com.teampatch.core.designsystem.theme.DP24
import com.teampatch.core.designsystem.theme.DP4
import com.teampatch.core.designsystem.theme.DP8
import com.teampatch.core.designsystem.theme.G5
import com.teampatch.core.designsystem.theme.RoundedCornerShape10
import com.teampatch.core.designsystem.theme.SP18
import com.teampatch.core.designsystem.theme.SP20
import com.teampatch.core.designsystem.theme.SubRed
import com.teampatch.core.designsystem.theme.WH
import com.teampatch.core.designsystem.utils.noRippleClickable
import com.teampatch.feature.R

@Composable
internal fun QuestionDetailComment(
    onEditCommentRequest: () -> Unit,
    onDeleteCommentRequest: () -> Unit,
    comment: String,
    username: String,
    profileImage: Painter,
    hasWritePermission: Boolean,
    modifier: Modifier = Modifier,
) {
    var isDropDownMenuShow by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = DP20, end = DP20, top = DP8, bottom = DP8)
            .background(WH)
            .padding(all = DP20)
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = profileImage,
                    contentDescription = "profile image"
                )
                Text(
                    text = username,
                    fontWeight = FontWeight.Medium,
                    fontSize = SP18,
                    color = G5,
                    modifier = Modifier.padding(start = DP16)
                )
            }
            if (hasWritePermission) {
                Box(
                    modifier = Modifier
                        .size(DP24)
                        .align(Alignment.CenterEnd)
                        .noRippleClickable {
                            isDropDownMenuShow = true
                        }
                ) {
                    Icon(
                        painter = painterResource(ic_more_question),
                        contentDescription = "more",
                        tint = G5,
                        modifier = Modifier
                            .size(width = DP4, height = DP16)
                            .align(Alignment.CenterEnd)
                    )
                    DropdownMenu(
                        expanded = isDropDownMenuShow,
                        onDismissRequest = { isDropDownMenuShow = false },
                        shape = RoundedCornerShape10,
                        modifier = Modifier
                            .widthIn(min = DP200)
                    ) {
                        DropdownMenuItem(
                            text = {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text = stringResource(R.string.dropdown_edit_comment),
                                        fontWeight = FontWeight.Medium,
                                        fontSize = SP20,
                                        color = BL
                                    )
                                }
                            },
                            onClick = {
                                onEditCommentRequest()
                                isDropDownMenuShow = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text = stringResource(R.string.dropdown_delete_comment),

                                        fontWeight = FontWeight.Medium,
                                        fontSize = SP20,
                                        color = SubRed
                                    )
                                }
                            },
                            onClick = {
                                onDeleteCommentRequest()
                                isDropDownMenuShow = false
                            }
                        )
                    }
                }
            }
        }
        Text(
            text = comment,
            fontWeight = FontWeight.Medium,
            fontSize = SP20,
            color = BL,
            modifier = Modifier.padding(top = DP12)
        )
    }
}
