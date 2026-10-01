package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.User
import com.example.ui.theme.*

sealed class HomeDestination {
    object None : HomeDestination()
    object Courses : HomeDestination()
    object Tests : HomeDestination()
    object Contests : HomeDestination()
    object Stats : HomeDestination()
    object About : HomeDestination()
    object UsersList : HomeDestination()
}

data class GridItem(
    val title: String,
    val emoji: String,
    val destination: HomeDestination,
    val adminOnly: Boolean = false
)

@Composable
fun HomeScreen(
    currentUser: User?,
    onNavigate: (HomeDestination) -> Unit
) {
    val isAdmin = currentUser?.isAdmin == true

    val gridItems = listOf(
        GridItem("Kurslar", "📚", HomeDestination.Courses),
        GridItem("Testlar", "📝", HomeDestination.Tests),
        GridItem("Musobaqalar", "🏆", HomeDestination.Contests),
        GridItem("Statistika", "📊", HomeDestination.Stats),
        GridItem("Biz haqimizda", "ℹ️", HomeDestination.About),
        GridItem("Foydalanuvchilar", "👥", HomeDestination.UsersList, adminOnly = true)
    ).filter { !it.adminOnly || isAdmin }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Gradient Banner
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.beruniy_banner_1790856887942),
                    contentDescription = "Beruniy davomchilari banneri",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Gradient Overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0x770E121A),
                                    Color(0xEE0E121A)
                                )
                            )
                        )
                )

                // Text Content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Surface(
                        color = AccentOrange,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "YOSHLAR 2030",
                            color = Color.Black,
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Kelajak yoshlar qo'lida",
                        color = Color.White,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Bilim · Innovatsiya · Yangi avlod",
                        color = AccentGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Section Title
        Text(
            text = "Asosiy bo'limlar",
            color = DarkText,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )

        // 3-Column Grid Buttons
        val chunked = gridItems.chunked(3)
        chunked.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowItems.forEach { item ->
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkCard),
                        modifier = Modifier
                            .weight(1f)
                            .height(108.dp)
                            .clickable { onNavigate(item.destination) }
                            .testTag("home_btn_${item.title.lowercase()}")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(DarkCardSecondary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item.emoji,
                                    fontSize = 24.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = item.title,
                                color = DarkText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )
                        }
                    }
                }
                // Fill blank weights if row has less than 3
                if (rowItems.size < 3) {
                    repeat(3 - rowItems.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // Motivational Card from Abu Rayhan al-Biruni
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCardSecondary.copy(alpha = 0.7f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("💡", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Abu Rayhon Beruniy hikmati",
                        color = AccentGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "\"Haqiqatni bilish va izlash inson aql-idrokining eng oliy maqsadidir. Ilm egallashda hech qachon to'xtamang.\"",
                    color = DarkTextMuted,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
