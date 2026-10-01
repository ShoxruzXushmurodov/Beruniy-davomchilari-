package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BeruniyRepository
import com.example.model.User
import com.example.ui.components.formatUzbekDate
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen() {
    val notifications by BeruniyRepository.notifications.collectAsState()

    LaunchedEffect(Unit) {
        BeruniyRepository.markNotificationsAsRead()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "🔔 Bildirishnomalar",
                        color = DarkText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Hozircha yangi bildirishnomalar yo'q.",
                    color = DarkTextMuted,
                    fontSize = 15.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notifications, key = { it.id }) { item ->
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkCard),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            if (!item.isRead) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(AccentOrange)
                                        .padding(top = 4.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.title,
                                        color = DarkText,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1f)
                                    )

                                    Text(
                                        text = formatUzbekDate(item.ts),
                                        color = DarkTextMuted,
                                        fontSize = 11.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = item.text,
                                    color = DarkTextMuted,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BroadcastScreen(
    currentUser: User?
) {
    val notifications by BeruniyRepository.notifications.collectAsState()
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "📣 Xabar tarqatish (Admin)",
                        color = DarkText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Barcha foydalanuvchilarga xabar yuborish",
                            color = AccentOrange,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        if (errorMessage != null) {
                            Text(errorMessage ?: "", color = SemanticError, fontSize = 12.sp)
                        }

                        OutlinedTextField(
                            value = title,
                            onValueChange = {
                                title = it
                                errorMessage = null
                            },
                            placeholder = { Text("Sarlavha", color = DarkTextMuted) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = DarkText,
                                unfocusedTextColor = DarkText,
                                focusedBorderColor = BrandPrimaryBlue
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().testTag("broadcast_title_input")
                        )

                        OutlinedTextField(
                            value = text,
                            onValueChange = {
                                text = it
                                errorMessage = null
                            },
                            placeholder = { Text("Bildirishnoma matni...", color = DarkTextMuted) },
                            minLines = 3,
                            maxLines = 6,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = DarkText,
                                unfocusedTextColor = DarkText,
                                focusedBorderColor = BrandPrimaryBlue
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().testTag("broadcast_text_input")
                        )

                        Button(
                            onClick = {
                                val res = BeruniyRepository.broadcastNotification(title, text)
                                if (res.isSuccess) {
                                    title = ""
                                    text = ""
                                    errorMessage = null
                                } else {
                                    errorMessage = res.exceptionOrNull()?.message
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("broadcast_submit_btn")
                        ) {
                            Icon(Icons.Default.Campaign, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "📣 Tarqatish",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Yuborilgan xabarlar tarixi",
                    color = DarkText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(notifications, key = { it.id }) { item ->
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCardSecondary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.title,
                                color = DarkText,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.text,
                                color = DarkTextMuted,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = formatUzbekDate(item.ts),
                                color = AccentGold,
                                fontSize = 10.sp
                            )
                        }

                        IconButton(onClick = { BeruniyRepository.deleteNotification(item.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "O'chirish", tint = SemanticError)
                        }
                    }
                }
            }
        }
    }
}
