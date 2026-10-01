package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BeruniyRepository
import com.example.model.ChatGroup
import com.example.model.ChatMessage
import com.example.model.User
import com.example.ui.components.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    currentUser: User?,
    onOpenChat: (ChatGroup) -> Unit,
    onViewProfile: (User) -> Unit
) {
    val groups by BeruniyRepository.groups.collectAsState()
    val users by BeruniyRepository.users.collectAsState()
    val currentUid = currentUser?.uid ?: ""

    var selectedTab by remember { mutableStateOf(0) } // 0: Barchasi, 1: Shaxsiy, 2: Guruhlar
    var searchQuery by remember { mutableStateOf("") }
    var showCreateDialog by remember { mutableStateOf(false) }

    // Dialog state for new chat
    var createMode by remember { mutableStateOf<String?>(null) } // "dm" or "group"
    var newGroupName by remember { mutableStateOf("") }
    var selectedGroupMembers by remember { mutableStateOf(setOf<String>()) }

    // Filtered chats
    val userChats = groups.filter { group ->
        group.isGeneral || group.members.contains(currentUid)
    }

    val filteredChats = userChats.filter { group ->
        val query = searchQuery.trim().lowercase()
        if (query.isEmpty()) true
        else {
            group.name.lowercase().contains(query) || group.lastMessage.lowercase().contains(query)
        }
    }.filter { group ->
        when (selectedTab) {
            1 -> !group.isGeneral && group.id.startsWith("dm_") // Shaxsiy
            2 -> group.isGeneral || group.id.startsWith("group_") // Guruhlar
            else -> true // Barchasi
        }
    }.sortedByDescending { it.isGeneral } // General always first

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "💬 Chat",
                        color = DarkText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                actions = {
                    IconButton(
                        onClick = { showCreateDialog = true },
                        modifier = Modifier.testTag("chat_add_button")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(BrandPrimaryBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Yangi chat",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Qidiruv (ism, familiya, @nom)...", color = DarkTextMuted) },
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
                modifier = Modifier.fillMaxWidth().testTag("chat_search_input")
            )

            // Tabs: Barchasi / Shaxsiy / Guruhlar
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = DarkCard,
                contentColor = AccentOrange,
                modifier = Modifier.clip(RoundedCornerShape(14.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            "Barchasi",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 0) AccentOrange else DarkTextMuted
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            "Shaxsiy",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 1) AccentOrange else DarkTextMuted
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = {
                        Text(
                            "Guruhlar",
                            fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 2) AccentOrange else DarkTextMuted
                        )
                    }
                )
            }

            // Chat List
            if (filteredChats.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Hech qanday chat topilmadi.",
                        color = DarkTextMuted,
                        fontSize = 15.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredChats, key = { it.id }) { chatItem ->
                        val isGeneral = chatItem.isGeneral
                        val isDm = chatItem.id.startsWith("dm_")

                        // For DM, find other user
                        val otherUid = if (isDm) chatItem.members.find { it != currentUid } else null
                        val otherUser = if (otherUid != null) users.find { it.uid == otherUid } else null

                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isGeneral) DarkCardSecondary else DarkCard
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOpenChat(chatItem) }
                                .testTag("chat_card_${chatItem.id}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (isGeneral) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(
                                                Brush.linearGradient(
                                                    listOf(BrandPrimaryBlue, AccentOrange)
                                                )
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("🏛️", fontSize = 22.sp)
                                    }
                                } else if (otherUser != null) {
                                    BeruniyAvatar(user = otherUser, size = 48.dp)
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(DarkCardSecondary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Group, contentDescription = null, tint = AccentGold)
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = if (isGeneral) "Beruniy X avlod" else (otherUser?.fullName ?: chatItem.name),
                                            color = DarkText,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f)
                                        )

                                        if (chatItem.lastMessageTs > 0) {
                                            Text(
                                                text = formatTimeOnly(chatItem.lastMessageTs),
                                                color = DarkTextMuted,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = if (isGeneral) "Umumiy guruh · ${chatItem.lastMessage.ifEmpty { "Xush kelibsiz!" }}"
                                        else if (isDm) (chatItem.lastMessage.ifEmpty { otherUser?.region ?: "" })
                                        else "${chatItem.members.size} a'zo · ${chatItem.lastMessage.ifEmpty { "Yangi guruh" }}",
                                        color = DarkTextMuted,
                                        fontSize = 13.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // CREATE NEW CHAT MODAL
        if (showCreateDialog) {
            AlertDialog(
                onDismissRequest = {
                    showCreateDialog = false
                    createMode = null
                },
                title = {
                    Text(
                        text = if (createMode == null) "Yangi suhbat" else if (createMode == "dm") "Shaxsiy chat" else "Yangi guruh",
                        color = DarkText,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    if (createMode == null) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = DarkCardSecondary),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { createMode = "dm" }
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = BrandPrimaryBlue)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("Shaxsiy chat (Odam tanlash)", color = DarkText, fontWeight = FontWeight.SemiBold)
                                }
                            }

                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = DarkCardSecondary),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { createMode = "group" }
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Group, contentDescription = null, tint = AccentOrange)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("Yangi guruh yaratish", color = DarkText, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    } else if (createMode == "dm") {
                        // Pick a user to chat
                        val otherUsers = users.filter { it.uid != currentUid }
                        LazyColumn(
                            modifier = Modifier.heightIn(max = 300.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(otherUsers) { u ->
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = DarkCardSecondary),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            val dmChat = BeruniyRepository.getOrCreateDirectChat(u)
                                            showCreateDialog = false
                                            createMode = null
                                            onOpenChat(dmChat)
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        BeruniyAvatar(user = u, size = 36.dp)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(u.fullName, color = DarkText, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                            Text("@${u.username} · ${u.region}", color = DarkTextMuted, fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }
                    } else if (createMode == "group") {
                        // Create group form
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = newGroupName,
                                onValueChange = { newGroupName = it },
                                label = { Text("Guruh nomi", color = DarkTextMuted) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = DarkText,
                                    unfocusedTextColor = DarkText,
                                    focusedBorderColor = BrandPrimaryBlue
                                ),
                                modifier = Modifier.fillMaxWidth().testTag("new_group_name_input")
                            )

                            Text(
                                "A'zolarni tanlang:",
                                color = DarkTextMuted,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            val candidates = users.filter { it.uid != currentUid }
                            LazyColumn(
                                modifier = Modifier.heightIn(max = 200.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                items(candidates) { candidate ->
                                    val isSelected = selectedGroupMembers.contains(candidate.uid)
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                selectedGroupMembers = if (isSelected) selectedGroupMembers - candidate.uid
                                                else selectedGroupMembers + candidate.uid
                                            }
                                    ) {
                                        Checkbox(
                                            checked = isSelected,
                                            onCheckedChange = { check ->
                                                selectedGroupMembers = if (check) selectedGroupMembers + candidate.uid
                                                else selectedGroupMembers - candidate.uid
                                            },
                                            colors = CheckboxDefaults.colors(checkedColor = BrandPrimaryBlue)
                                        )
                                        Text(
                                            text = "${candidate.fullName} (@${candidate.username})",
                                            color = DarkText,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    if (createMode == "group") {
                        Button(
                            onClick = {
                                if (newGroupName.isNotBlank()) {
                                    val group = BeruniyRepository.createGroup(newGroupName, selectedGroupMembers.toList())
                                    showCreateDialog = false
                                    createMode = null
                                    newGroupName = ""
                                    selectedGroupMembers = emptySet()
                                    onOpenChat(group)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimaryBlue),
                            modifier = Modifier.testTag("submit_create_group")
                        ) {
                            Text("Guruh yaratish", color = Color.White)
                        }
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showCreateDialog = false
                        createMode = null
                    }) {
                        Text("Bekor qilish", color = DarkTextMuted)
                    }
                },
                containerColor = DarkCard
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun ChatConversationScreen(
    chatGroup: ChatGroup,
    currentUser: User?,
    onBack: () -> Unit,
    onViewProfile: (User) -> Unit
) {
    val messages by BeruniyRepository.messages.collectAsState()
    val users by BeruniyRepository.users.collectAsState()
    val currentUid = currentUser?.uid ?: ""
    val isAdmin = currentUser?.isAdmin == true

    val chatMessages = messages.filter { it.chatId == chatGroup.id }
        .sortedBy { it.ts }

    var inputText by remember { mutableStateOf("") }
    var showEmojiPicker by remember { mutableStateOf(false) }
    var selectedMessageForAdmin by remember { mutableStateOf<ChatMessage?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val isDm = chatGroup.id.startsWith("dm_")
    val isGeneral = chatGroup.isGeneral
    val otherUid = if (isDm) chatGroup.members.find { it != currentUid } else null
    val otherUser = if (otherUid != null) users.find { it.uid == otherUid } else null

    BackHandler { onBack() }

    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            // Header: Two rounded capsules as specified
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkBackground)
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left round "‹" back button
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    modifier = Modifier
                        .size(42.dp)
                        .clickable(onClick = onBack)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "‹",
                            color = Color.Black,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Right capsule
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = Color.White,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clickable {
                            if (isDm && otherUser != null) {
                                onViewProfile(otherUser)
                            }
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isGeneral) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(BrandPrimaryBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🏛️", fontSize = 16.sp)
                            }
                        } else if (otherUser != null) {
                            BeruniyAvatar(user = otherUser, size = 32.dp)
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(AccentOrange),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Group, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Text(
                                text = if (isGeneral) "Beruniy X avlod" else (otherUser?.fullName ?: chatGroup.name),
                                color = Color.Black,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = if (isGeneral) "Umumiy guruh" else if (isDm) "@${otherUser?.username ?: ""}" else "Guruh · ${chatGroup.members.size} a'zo",
                                color = Color.Gray,
                                fontSize = 11.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            // Writing bar: White capsule + emoji picker + blue circle send button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkBackground)
                    .navigationBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                // Emoji quick bar if opened
                if (showEmojiPicker) {
                    Surface(
                        color = DarkCardSecondary,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            val quickEmojis = listOf("👍", "🔥", "🎉", "👏", "💡", "📚", "✨", "😊", "❤️")
                            quickEmojis.forEach { emoji ->
                                Text(
                                    text = emoji,
                                    fontSize = 24.sp,
                                    modifier = Modifier
                                        .clickable {
                                            inputText += emoji
                                            showEmojiPicker = false
                                        }
                                        .padding(4.dp)
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // White capsule for emoji & input
                    Surface(
                        shape = RoundedCornerShape(26.dp),
                        color = Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { showEmojiPicker = !showEmojiPicker },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Text("😊", fontSize = 20.sp)
                            }

                            TextField(
                                value = inputText,
                                onValueChange = { inputText = it },
                                placeholder = { Text("Xabar yozing...", color = Color.Gray, fontSize = 14.sp) },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black
                                ),
                                singleLine = false,
                                maxLines = 4,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("chat_input_text")
                            )
                        }
                    }

                    // Blue Round Send Button
                    Surface(
                        shape = CircleShape,
                        color = BrandPrimaryBlue,
                        modifier = Modifier
                            .size(50.dp)
                            .clickable {
                                if (inputText.isNotBlank()) {
                                    val r = BeruniyRepository.sendMessage(chatGroup.id, inputText)
                                    if (r.isSuccess) {
                                        inputText = ""
                                        coroutineScope.launch {
                                            if (chatMessages.isNotEmpty()) {
                                                listState.animateScrollToItem(chatMessages.size - 1)
                                            }
                                        }
                                    }
                                }
                            }
                            .testTag("chat_send_btn")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Yuborish",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        // Conversation background:
        // Gradient #9FD8C4 -> #B3A6E6 -> #86B2F2 + subtle doodle pattern
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF9FD8C4),
                            Color(0xFFB3A6E6),
                            Color(0xFF86B2F2)
                        )
                    )
                )
        ) {
            // Subtle Snowflake / Doodle Pattern overlay
            Canvas(modifier = Modifier.fillMaxSize()) {
                val step = 60f
                for (x in 0..size.width.toInt() step step.toInt()) {
                    for (y in 0..size.height.toInt() step step.toInt()) {
                        drawCircle(
                            color = Color.White.copy(alpha = 0.25f),
                            radius = 1.8f,
                            center = Offset(x.toFloat(), y.toFloat())
                        )
                    }
                }
            }

            if (chatMessages.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        color = Color.White.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Hozircha xabar yo'q. Birinchi bo'lib yozing!",
                            color = Color(0xFF1E293B),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    var lastDayHeader = ""

                    chatMessages.forEachIndexed { index, msg ->
                        val dayHeader = formatUzbekDayHeader(msg.ts)
                        if (dayHeader != lastDayHeader) {
                            lastDayHeader = dayHeader
                            item(key = "day_header_$index") {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color.Black.copy(alpha = 0.25f)
                                    ) {
                                        Text(
                                            text = dayHeader,
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }

                        val isMine = msg.fromUid == currentUid
                        val senderUser = users.find { it.uid == msg.fromUid }

                        item(key = msg.id) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .combinedClickable(
                                        onClick = {},
                                        onLongClick = {
                                            if (isAdmin) {
                                                selectedMessageForAdmin = msg
                                            }
                                        }
                                    ),
                                horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                if (!isMine) {
                                    BeruniyAvatar(
                                        user = senderUser,
                                        size = 32.dp,
                                        onClick = {
                                            if (senderUser != null) onViewProfile(senderUser)
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                }

                                // Message Bubble
                                Surface(
                                    shape = RoundedCornerShape(
                                        topStart = 18.dp,
                                        topEnd = 18.dp,
                                        bottomStart = if (isMine) 18.dp else 4.dp,
                                        bottomEnd = if (isMine) 4.dp else 18.dp
                                    ),
                                    color = if (isMine) ChatBubbleSelf else ChatBubbleOther,
                                    shadowElevation = 2.dp,
                                    modifier = Modifier.widthIn(max = 280.dp)
                                ) {
                                    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                                        if (!isMine) {
                                            Text(
                                                text = "${msg.senderName} (@${msg.senderUsername})",
                                                color = getUserColor(msg.fromUid),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                modifier = Modifier
                                                    .clickable {
                                                        if (senderUser != null) onViewProfile(senderUser)
                                                    }
                                                    .padding(bottom = 2.dp)
                                            )
                                        }

                                        Text(
                                            text = msg.text,
                                            color = Color(0xFF0F172A), // Dark text as required
                                            fontSize = 14.sp,
                                            lineHeight = 19.sp
                                        )

                                        Spacer(modifier = Modifier.height(2.dp))

                                        Row(
                                            modifier = Modifier.align(Alignment.End),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = formatTimeOnly(msg.ts),
                                                color = Color(0xFF64748B),
                                                fontSize = 10.sp
                                            )
                                            if (isMine) {
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "✓✓",
                                                    color = BrandPrimaryBlue,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }

                                if (isMine) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    BeruniyAvatar(user = currentUser, size = 32.dp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Admin Long Press Delete Dialog
        if (selectedMessageForAdmin != null) {
            AlertDialog(
                onDismissRequest = { selectedMessageForAdmin = null },
                title = { Text("Xabarni o'chirish (Admin)", color = DarkText, fontWeight = FontWeight.Bold) },
                text = {
                    Text(
                        "Haqiqatan ham ushbu xabarni o'chirmoqchimisiz?",
                        color = DarkTextMuted
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            selectedMessageForAdmin?.let { BeruniyRepository.deleteMessage(it.id) }
                            selectedMessageForAdmin = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SemanticError)
                    ) {
                        Text("O'chirish", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { selectedMessageForAdmin = null }) {
                        Text("Bekor qilish", color = DarkTextMuted)
                    }
                },
                containerColor = DarkCard
            )
        }
    }
}
