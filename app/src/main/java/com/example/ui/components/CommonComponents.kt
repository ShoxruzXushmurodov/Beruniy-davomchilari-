package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.User
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

// Deterministic User Avatar Color Generator
fun getUserColor(uid: String): Color {
    val colors = listOf(
        Color(0xFF3D6DF2),
        Color(0xFF8B5CF6),
        Color(0xFFEC4899),
        Color(0xFF10B981),
        Color(0xFFF59E0B),
        Color(0xFF06B6D4),
        Color(0xFF6366F1),
        Color(0xFF14B8A6)
    )
    val hash = abs(uid.hashCode())
    return colors[hash % colors.size]
}

@Composable
fun BeruniyAvatar(
    user: User?,
    size: Dp = 40.dp,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val initials = user?.initials ?: "BD"
    val userColor = if (user != null) getUserColor(user.uid) else BrandPrimaryBlue
    val gradient = Brush.linearGradient(
        listOf(userColor, Color(0xFF8B5CF6))
    )

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(gradient)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = (size.value * 0.38f).sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BeruniyToast(
    message: String?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = message != null,
        enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
        modifier = modifier
    ) {
        if (message != null) {
            LaunchedEffect(message) {
                kotlinx.coroutines.delay(5000)
                onDismiss()
            }

            Surface(
                color = DarkCardSecondary,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable { onDismiss() }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(AccentOrange)
                    )
                    Text(
                        text = message,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

// Uzbek Date Formatter
fun formatUzbekDate(timestamp: Long): String {
    val now = Calendar.getInstance()
    val msgCal = Calendar.getInstance().apply { timeInMillis = timestamp }

    val uzbekMonths = listOf(
        "yanvar", "fevral", "mart", "aprel", "may", "iyun",
        "iyul", "avgust", "sentabr", "oktabr", "noyabr", "dekabr"
    )

    val isToday = now.get(Calendar.YEAR) == msgCal.get(Calendar.YEAR) &&
            now.get(Calendar.DAY_OF_YEAR) == msgCal.get(Calendar.DAY_OF_YEAR)

    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val timeStr = timeFormat.format(Date(timestamp))

    return if (isToday) {
        "Bugun $timeStr"
    } else {
        val day = msgCal.get(Calendar.DAY_OF_MONTH)
        val month = uzbekMonths[msgCal.get(Calendar.MONTH)]
        "$day-$month $timeStr"
    }
}

fun formatUzbekDayHeader(timestamp: Long): String {
    val now = Calendar.getInstance()
    val msgCal = Calendar.getInstance().apply { timeInMillis = timestamp }

    val uzbekMonths = listOf(
        "yanvar", "fevral", "mart", "aprel", "may", "iyun",
        "iyul", "avgust", "sentabr", "oktabr", "noyabr", "dekabr"
    )

    val isToday = now.get(Calendar.YEAR) == msgCal.get(Calendar.YEAR) &&
            now.get(Calendar.DAY_OF_YEAR) == msgCal.get(Calendar.DAY_OF_YEAR)

    return if (isToday) {
        "Bugun"
    } else {
        val day = msgCal.get(Calendar.DAY_OF_MONTH)
        val month = uzbekMonths[msgCal.get(Calendar.MONTH)]
        "$day-$month"
    }
}

fun formatTimeOnly(timestamp: Long): String {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(Date(timestamp))
}
