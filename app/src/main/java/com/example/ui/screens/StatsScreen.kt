package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BeruniyRepository
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    onBack: () -> Unit
) {
    val courses by BeruniyRepository.courses.collectAsState()
    val tests by BeruniyRepository.tests.collectAsState()
    val contests by BeruniyRepository.contests.collectAsState()
    val users by BeruniyRepository.users.collectAsState()

    // Non-admin users count as required: "adminlar hisobga kirmaydi"
    val studentUsersCount = users.count { !it.isAdmin }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "📊 Statistika",
                        color = DarkText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Orqaga",
                            tint = DarkText
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Platforma ko'rsatkichlari (Real vaqtda)",
                color = DarkTextMuted,
                fontSize = 14.sp
            )

            // 2x2 CARDS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                StatCard(
                    title = "Kurslar",
                    count = courses.size,
                    emoji = "📚",
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    title = "Testlar",
                    count = tests.size,
                    emoji = "📝",
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                StatCard(
                    title = "Musobaqalar",
                    count = contests.size,
                    emoji = "🏆",
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    title = "Foydalanuvchilar",
                    count = studentUsersCount,
                    emoji = "👥",
                    subtitle = "(adminlarsiz)",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🎯 Yoshlar 2030 maqsadi",
                        color = AccentGold,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "O'zbekiston yoshlarining 100 000 dan ortig'ini xalqaro sertifikatlar (IELTS, SAT, IT) bilan ta'minlash va jahon darajasidagi mutaxassis qilib tarbiyalash.",
                        color = DarkTextMuted,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    count: Int,
    emoji: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        modifier = modifier.height(150.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    color = DarkText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(DarkCardSecondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(emoji, fontSize = 16.sp)
                }
            }

            Column {
                Text(
                    text = "$count",
                    color = AccentOrange,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        color = DarkTextMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
