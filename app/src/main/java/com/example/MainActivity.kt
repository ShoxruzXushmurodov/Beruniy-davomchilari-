package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BeruniyRepository
import com.example.model.ChatGroup
import com.example.model.User
import com.example.ui.components.BeruniyAvatar
import com.example.ui.components.BeruniyToast
import com.example.ui.screens.*
import com.example.ui.theme.*

enum class BottomTab {
    HOME,
    CHAT,
    NOTIFICATIONS,
    BROADCAST,
    PROFILE
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BeruniyTheme(darkTheme = true) {
                MainAppRoot()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppRoot() {
    val currentUser by BeruniyRepository.currentUser.collectAsState()
    val toastMessage by BeruniyRepository.toastEvent.collectAsState()
    val notifications by BeruniyRepository.notifications.collectAsState()

    val unreadNotifCount = notifications.count { !it.isRead }

    // If not logged in, show AuthScreen
    if (currentUser == null) {
        AuthScreen(
            onAuthSuccess = {
                // currentUser will automatically update
            }
        )
        return
    }

    val user = currentUser!!
    val isAdmin = user.isAdmin

    var currentTab by remember { mutableStateOf(BottomTab.HOME) }
    var homeDestination by remember { mutableStateOf<HomeDestination>(HomeDestination.None) }
    var activeChatGroup by remember { mutableStateOf<ChatGroup?>(null) }
    var inspectedUser by remember { mutableStateOf<User?>(null) }

    // Back handling for nested screens
    BackHandler(enabled = activeChatGroup != null || inspectedUser != null || homeDestination != HomeDestination.None) {
        when {
            activeChatGroup != null -> activeChatGroup = null
            inspectedUser != null -> inspectedUser = null
            homeDestination != HomeDestination.None -> homeDestination = HomeDestination.None
        }
    }

    Scaffold(
        topBar = {
            // Only show main top bar when not inside a full-screen sub-view that has its own top bar
            val isInsideSubScreen = activeChatGroup != null ||
                    inspectedUser != null ||
                    homeDestination != HomeDestination.None ||
                    currentTab == BottomTab.CHAT ||
                    currentTab == BottomTab.NOTIFICATIONS ||
                    currentTab == BottomTab.BROADCAST ||
                    currentTab == BottomTab.PROFILE

            if (!isInsideSubScreen) {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Logo (42px)
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(DarkCardSecondary),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.app_icon_beruniy_1790856810601),
                                    contentDescription = "Logo",
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = user.fullName,
                                        color = DarkText,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    if (isAdmin) {
                                        Text(
                                            text = " · Admin",
                                            color = AccentOrange,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Text(
                                    text = "@${user.username}",
                                    color = DarkTextMuted,
                                    fontSize = 12.sp
                                )
                            }

                            // Right Avatar (clicking switches to Profile tab)
                            BeruniyAvatar(
                                user = user,
                                size = 38.dp,
                                onClick = { currentTab = BottomTab.PROFILE }
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
                )
            }
        },
        bottomBar = {
            // Hide bottom bar when inside chat conversation
            if (activeChatGroup == null) {
                NavigationBar(
                    containerColor = DarkCard,
                    contentColor = DarkText
                ) {
                    // 1. Asosiy
                    NavigationBarItem(
                        selected = currentTab == BottomTab.HOME && homeDestination == HomeDestination.None && inspectedUser == null,
                        onClick = {
                            currentTab = BottomTab.HOME
                            homeDestination = HomeDestination.None
                            inspectedUser = null
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Asosiy") },
                        label = { Text("Asosiy", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AccentOrange,
                            selectedTextColor = AccentOrange,
                            unselectedIconColor = DarkTextMuted,
                            unselectedTextColor = DarkTextMuted,
                            indicatorColor = DarkCardSecondary
                        ),
                        modifier = Modifier.testTag("nav_home")
                    )

                    // 2. Chat
                    NavigationBarItem(
                        selected = currentTab == BottomTab.CHAT && inspectedUser == null,
                        onClick = {
                            currentTab = BottomTab.CHAT
                            inspectedUser = null
                        },
                        icon = { Icon(Icons.Default.Chat, contentDescription = "Chat") },
                        label = { Text("Chat", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AccentOrange,
                            selectedTextColor = AccentOrange,
                            unselectedIconColor = DarkTextMuted,
                            unselectedTextColor = DarkTextMuted,
                            indicatorColor = DarkCardSecondary
                        ),
                        modifier = Modifier.testTag("nav_chat")
                    )

                    // 3. Bildirishnoma (with Badge)
                    NavigationBarItem(
                        selected = currentTab == BottomTab.NOTIFICATIONS && inspectedUser == null,
                        onClick = {
                            currentTab = BottomTab.NOTIFICATIONS
                            inspectedUser = null
                        },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (unreadNotifCount > 0) {
                                        Badge(
                                            containerColor = SemanticError,
                                            contentColor = Color.White
                                        ) {
                                            Text(
                                                text = if (unreadNotifCount > 99) "99+" else "$unreadNotifCount",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Notifications, contentDescription = "Bildirishnoma")
                            }
                        },
                        label = { Text("Bildirishnoma", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AccentOrange,
                            selectedTextColor = AccentOrange,
                            unselectedIconColor = DarkTextMuted,
                            unselectedTextColor = DarkTextMuted,
                            indicatorColor = DarkCardSecondary
                        ),
                        modifier = Modifier.testTag("nav_notifications")
                    )

                    // 4. Tarqatish (FAQAT Admin)
                    if (isAdmin) {
                        NavigationBarItem(
                            selected = currentTab == BottomTab.BROADCAST && inspectedUser == null,
                            onClick = {
                                currentTab = BottomTab.BROADCAST
                                inspectedUser = null
                            },
                            icon = { Icon(Icons.Default.Campaign, contentDescription = "Tarqatish") },
                            label = { Text("Tarqatish", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = AccentOrange,
                                selectedTextColor = AccentOrange,
                                unselectedIconColor = DarkTextMuted,
                                unselectedTextColor = DarkTextMuted,
                                indicatorColor = DarkCardSecondary
                            ),
                            modifier = Modifier.testTag("nav_broadcast")
                        )
                    }

                    // 5. Profil
                    NavigationBarItem(
                        selected = currentTab == BottomTab.PROFILE && inspectedUser == null,
                        onClick = {
                            currentTab = BottomTab.PROFILE
                            inspectedUser = null
                        },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
                        label = { Text("Profil", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AccentOrange,
                            selectedTextColor = AccentOrange,
                            unselectedIconColor = DarkTextMuted,
                            unselectedTextColor = DarkTextMuted,
                            indicatorColor = DarkCardSecondary
                        ),
                        modifier = Modifier.testTag("nav_profile")
                    )
                }
            }
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                // If inspecting another user's profile
                inspectedUser != null -> {
                    ProfileScreen(
                        userToView = inspectedUser,
                        currentUser = user,
                        onBack = { inspectedUser = null },
                        onLogout = { BeruniyRepository.logout() },
                        onStartChatWithUser = { directChatGroup ->
                            inspectedUser = null
                            activeChatGroup = directChatGroup
                            currentTab = BottomTab.CHAT
                        }
                    )
                }

                // If inside active chat conversation
                activeChatGroup != null -> {
                    ChatConversationScreen(
                        chatGroup = activeChatGroup!!,
                        currentUser = user,
                        onBack = { activeChatGroup = null },
                        onViewProfile = { targetUser -> inspectedUser = targetUser }
                    )
                }

                // Otherwise render according to current Tab & Home Destinations
                currentTab == BottomTab.HOME -> {
                    when (homeDestination) {
                        HomeDestination.Courses -> {
                            SubjectsScreen(
                                isCourses = true,
                                currentUser = user,
                                onBack = { homeDestination = HomeDestination.None }
                            )
                        }
                        HomeDestination.Tests -> {
                            SubjectsScreen(
                                isCourses = false,
                                currentUser = user,
                                onBack = { homeDestination = HomeDestination.None }
                            )
                        }
                        HomeDestination.Contests -> {
                            ContestsScreen(
                                currentUser = user,
                                onBack = { homeDestination = HomeDestination.None }
                            )
                        }
                        HomeDestination.Stats -> {
                            StatsScreen(
                                onBack = { homeDestination = HomeDestination.None }
                            )
                        }
                        HomeDestination.About -> {
                            AboutScreen(
                                currentUser = user,
                                onBack = { homeDestination = HomeDestination.None }
                            )
                        }
                        HomeDestination.UsersList -> {
                            UsersListScreen(
                                currentUser = user,
                                onBack = { homeDestination = HomeDestination.None },
                                onSelectUser = { targetUser -> inspectedUser = targetUser }
                            )
                        }
                        HomeDestination.None -> {
                            HomeScreen(
                                currentUser = user,
                                onNavigate = { destination -> homeDestination = destination }
                            )
                        }
                    }
                }

                currentTab == BottomTab.CHAT -> {
                    ChatListScreen(
                        currentUser = user,
                        onOpenChat = { chat -> activeChatGroup = chat },
                        onViewProfile = { targetUser -> inspectedUser = targetUser }
                    )
                }

                currentTab == BottomTab.NOTIFICATIONS -> {
                    NotificationScreen()
                }

                currentTab == BottomTab.BROADCAST -> {
                    BroadcastScreen(currentUser = user)
                }

                currentTab == BottomTab.PROFILE -> {
                    ProfileScreen(
                        userToView = null, // Own profile
                        currentUser = user,
                        onBack = null,
                        onLogout = { BeruniyRepository.logout() }
                    )
                }
            }

            // In-app Toast Banner
            BeruniyToast(
                message = toastMessage,
                onDismiss = { BeruniyRepository.clearToast() },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
            )
        }
    }
}
