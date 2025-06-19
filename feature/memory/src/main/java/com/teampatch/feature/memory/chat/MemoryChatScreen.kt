package com.teampatch.feature.memory.chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teampatch.core.designsystem.R
import com.teampatch.core.designsystem.theme.HarmonyTheme

@Composable
internal fun MemoryChatScreenWithViewModel() {

}

@Composable
private fun MemoryChatScreen(
    onDismiss: () -> Unit,
    onRestartConversation: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
            .padding(horizontal = 24.dp)
    ) {
        // 상단 날짜 + 제목 + 닫기 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFECECEC))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text("2024년 5월 4일", fontSize = 12.sp)
                }
            }

            IconButton(onClick = onDismiss) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "닫기")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 프로필 이미지 + 질문 + 사진
        Row(verticalAlignment = Alignment.Top) {
            Image(
                painter = painterResource(id = R.drawable.ic_my_appbar), // 귀여운 캐릭터 아이콘 대체
                contentDescription = "캐릭터",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Image(
                    painter = painterResource(id = R.drawable.ic_my_appbar), // 병실 이미지 대체
                    contentDescription = "병실 사진",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(2.dp, Color(0xFF4A90E2), RoundedCornerShape(12.dp))
                )

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.LightGray),
                    modifier = Modifier.padding(end = 32.dp)
                ) {
                    Text(
                        text = "다은이를 분만실에서 처음 봤을 때 어떤 느낌이 들었나요?",
                        modifier = Modifier.padding(12.dp),
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 대답 말풍선
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFD9FDD3), // 연한 초록
                modifier = Modifier.padding(start = 64.dp)
            ) {
                Text(
                    text = "너무 사랑스러웠단다. 내 소중한 손녀 딸을 보고 싶었거든 어쩌구 저쩌구 그래서 울산 병원에서 어쩌구 저쩌구",
                    modifier = Modifier.padding(12.dp),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // 다시 대화하기 버튼
        Button(
            onClick = onRestartConversation,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2DC26B))
        ) {
            Text("다시 대화하기", color = Color.White)
        }
    }
}

@Preview
@Composable
private fun MemoryChatScreenPreview() {
    HarmonyTheme {
        MemoryChatScreen(onDismiss = {}, onRestartConversation = {})
    }
}