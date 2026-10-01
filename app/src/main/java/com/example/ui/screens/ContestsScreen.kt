package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BeruniyRepository
import com.example.model.User
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContestsScreen(
    currentUser: User?,
    onBack: () -> Unit
) {
    val isAdmin = currentUser?.isAdmin == true
    val contests by BeruniyRepository.contests.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var editingId by remember { mutableStateOf<String?>(null) }
    var inputName by remember { mutableStateOf("") }
    var inputDate by remember { mutableStateOf("") }
    var inputInfo by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "🏆 Musobaqalar",
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
        floatingActionButton = {
            if (isAdmin) {
                FloatingActionButton(
                    onClick = {
                        editingId = null
                        inputName = ""
                        inputDate = "2026-11-20"
                        inputInfo = ""
                        showDialog = true
                    },
                    containerColor = AccentOrange,
                    contentColor = Color.Black,
                    modifier = Modifier.testTag("add_contest_fab")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Musobaqa qo'shish")
                }
            }
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        if (contests.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Hozircha musobaqalar yo'q.",
                    color = DarkTextMuted,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(contests, key = { it.id }) { contest ->
                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkCard),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Surface(
                                    color = AccentOrange.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Event,
                                            contentDescription = null,
                                            tint = AccentOrange,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = contest.date,
                                            color = AccentOrange,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                if (isAdmin) {
                                    Row {
                                        IconButton(onClick = {
                                            editingId = contest.id
                                            inputName = contest.name
                                            inputDate = contest.date
                                            inputInfo = contest.info
                                            showDialog = true
                                        }) {
                                            Icon(Icons.Default.Edit, contentDescription = "O'zgartirish", tint = AccentOrange)
                                        }
                                        IconButton(onClick = { BeruniyRepository.deleteContest(contest.id) }) {
                                            Icon(Icons.Default.Delete, contentDescription = "O'chirish", tint = SemanticError)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = contest.name,
                                color = DarkText,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = contest.info,
                                color = DarkTextMuted,
                                fontSize = 13.sp,
                                lineHeight = 19.sp
                            )
                        }
                    }
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(
                        text = if (editingId == null) "Musobaqa qo'shish" else "Musobaqani o'zgartirish",
                        color = DarkText,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = inputName,
                            onValueChange = { inputName = it },
                            label = { Text("Musobaqa nomi", color = DarkTextMuted) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = DarkText,
                                unfocusedTextColor = DarkText,
                                focusedBorderColor = AccentOrange
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("contest_input_name")
                        )

                        OutlinedTextField(
                            value = inputDate,
                            onValueChange = { inputDate = it },
                            label = { Text("Sana (YYYY-MM-DD)", color = DarkTextMuted) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = DarkText,
                                unfocusedTextColor = DarkText,
                                focusedBorderColor = AccentOrange
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("contest_input_date")
                        )

                        OutlinedTextField(
                            value = inputInfo,
                            onValueChange = { inputInfo = it },
                            label = { Text("Batafsil ma'lumot", color = DarkTextMuted) },
                            minLines = 3,
                            maxLines = 5,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = DarkText,
                                unfocusedTextColor = DarkText,
                                focusedBorderColor = AccentOrange
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("contest_input_info")
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (inputName.isNotBlank() && inputDate.isNotBlank()) {
                                if (editingId == null) {
                                    BeruniyRepository.addContest(inputName, inputDate, inputInfo)
                                } else {
                                    BeruniyRepository.updateContest(editingId!!, inputName, inputDate, inputInfo)
                                }
                                showDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
                        modifier = Modifier.testTag("contest_save_btn")
                    ) {
                        Text("Saqlash", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Bekor qilish", color = DarkTextMuted)
                    }
                },
                containerColor = DarkCard
            )
        }
    }
}
