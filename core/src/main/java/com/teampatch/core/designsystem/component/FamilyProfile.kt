package com.teampatch.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teampatch.core.R
import com.teampatch.core.designsystem.theme.BL
import com.teampatch.core.designsystem.theme.G1
import com.teampatch.core.designsystem.theme.G4
import com.teampatch.core.designsystem.theme.HarmonyTheme
import com.teampatch.core.designsystem.theme.MainGreen
import com.teampatch.core.designsystem.theme.PretendardFontFamily
import com.teampatch.core.designsystem.theme.WH

enum class FamilyRole {
    ADMIN,
    MANAGER,
}

@Composable
fun FamilyProfile(
    profileImage: Painter,
    title: String,
    name: String,
    modifier: Modifier = Modifier,
    role: FamilyRole? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 88.dp)
            .background(color = G1, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Image(
            painter = profileImage,
            contentDescription = stringResource(R.string.family_profile_image_content_description),
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
        )
        Text(
            text = title,
            color = G4,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(start = 16.dp, end = 8.dp)
        )
        Text(
            text = name,
            color = BL,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            modifier = Modifier
        )
        when (role) {
            FamilyRole.ADMIN -> {
                Image(
                    painter = painterResource(R.drawable.ic_crown_family_info),
                    contentDescription = stringResource(R.string.family_profile_admin_icon_content_description),
                    modifier = Modifier
                        .padding(start = 12.dp)
                )
            }

            FamilyRole.MANAGER -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .background(MainGreen, RoundedCornerShape(999.dp))
                        .padding(horizontal = 12.dp)
                        .widthIn(min = 36.dp)
                        .heightIn(min = 32.dp)
                ) {
                    Text(
                        text = stringResource(R.string.family_profile_manager),
                        color = WH,
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        maxLines = 1
                    )
                }
            }

            null -> {}
        }
    }
}

@Preview
@Composable
private fun FamilyProfilePreview() {
    HarmonyTheme {
        FamilyProfile(
            profileImage = painterResource(R.drawable.ic_my_appbar),
            title = "손녀",
            name = "조다은",
            role = FamilyRole.MANAGER
        )
    }
}