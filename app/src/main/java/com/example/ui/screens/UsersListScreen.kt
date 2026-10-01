package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BeruniyRepository
import com.example.model.User
import com.example.ui.components.BeruniyAvatar
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersListScreen(
    currentUser: User?,
    onBack: () -> Unit,
    onSelectUser: (User) -> Unit
) {
    val users by BeruniyRepository.users.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filteredUsers = users.filter { user ->
        val query = searchQuery.trim().lowercase()
        if (query.isEmpty()) true
        else {
            user.fullName.lowercase().contains(query) ||
                    user.username.lowercase().contains(query) ||
                    user.region.lowercase().contains(query) ||
                    user.email.lowercase().contains(query)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "👥 Foydalanuvchilar",
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
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Ism, @nom yoki manzil bo'yicha qidiruv...", color = DarkTextMuted) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Qidirish", tint = DarkTextMuted)
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = DarkText,
                    unfocusedTextColor = DarkText,
                    focusedContainerColor = DarkCard,
                    unfocusedContainerColor = DarkCard,
                    focusedBorderColor = BrandPrimaryBlue,
                    unfocusedBorderColor = DarkCardSecondary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("users_search_input")
            )

            Text(
                text = "Jami foydalanuvchilar: ${filteredUsers.size}",
                color = DarkTextMuted,
                fontSize = 13.sp
            )

            if (filteredUsers.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Hech kim topilmadi.",
                        color = DarkTextMuted,
                        fontSize = 15.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredUsers, key = { it.uid }) { userItem ->
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkCard),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectUser(userItem) }
                                .testTag("user_item_${userItem.username}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                BeruniyAvatar(user = userItem, size = 50.dp)

                                Spacer(modifier = Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "#${userItem.order} ${userItem.fullName}",
                                            color = DarkText,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (userItem.isAdmin) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                color = AccentOrange.copy(alpha = 0.2f),
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = "Admin",
                                                    color = AccentOrange,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(3.dp))

                                    Text(
                                        text = "@${userItem.username} · ${userItem.age} yosh · ${userItem.region}",
                                        color = DarkTextMuted,
                                        fontSize = 12.sp
                                    )

                                    Text(
                                        text = "${userItem.phone} · ${userItem.email}",
                                        color = BrandPrimaryBlue,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
