package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BeruniyRepository
import com.example.model.Course
import com.example.model.SUBJECT_LIST
import com.example.model.TestItem
import com.example.model.User
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectsScreen(
    isCourses: Boolean, // true for Kurslar, false for Testlar
    currentUser: User?,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val isAdmin = currentUser?.isAdmin == true
    var selectedSubject by remember { mutableStateOf<String?>(null) }

    val courses by BeruniyRepository.courses.collectAsState()
    val tests by BeruniyRepository.tests.collectAsState()

    var showAddEditDialog by remember { mutableStateOf(false) }
    var editingItemId by remember { mutableStateOf<String?>(null) }
    var inputName by remember { mutableStateOf("") }
    var inputInfo by remember { mutableStateOf("") }
    var inputLink by remember { mutableStateOf("") }

    BackHandler {
        if (selectedSubject != null) {
            selectedSubject = null
        } else {
            onBack()
        }
    }

    if (selectedSubject == null) {
        // SUBJECT GRID VIEW (12 SUBJECTS)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("subjects_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Orqaga",
                        tint = DarkText
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isCourses) "📚 Kurslar" else "📝 Testlar",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(SUBJECT_LIST) { (subject, emoji) ->
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkCard),
                        modifier = Modifier
                            .height(110.dp)
                            .clickable { selectedSubject = subject }
                            .testTag("subject_card_${subject.lowercase()}")
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
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(DarkCardSecondary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(emoji, fontSize = 22.sp)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = subject,
                                color = DarkText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    } else {
        // MATERIAL LIST FOR CHOSEN SUBJECT
        val currentSubject = selectedSubject ?: ""
        val currentEmoji = SUBJECT_LIST.find { it.first == currentSubject }?.second ?: "📖"

        val subjectCourses = courses.filter { it.subject.equals(currentSubject, ignoreCase = true) }
        val subjectTests = tests.filter { it.subject.equals(currentSubject, ignoreCase = true) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "$currentSubject $currentEmoji",
                            color = DarkText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { selectedSubject = null }) {
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
                            editingItemId = null
                            inputName = ""
                            inputInfo = ""
                            inputLink = ""
                            showAddEditDialog = true
                        },
                        containerColor = BrandPrimaryBlue,
                        contentColor = Color.White,
                        modifier = Modifier.testTag("add_material_fab")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Yangi qo'shish")
                    }
                }
            },
            containerColor = DarkBackground
        ) { innerPadding ->
            val hasItems = if (isCourses) subjectCourses.isNotEmpty() else subjectTests.isNotEmpty()

            if (!hasItems) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Hozircha ma'lumot yo'q.",
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
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (isCourses) {
                        items(subjectCourses, key = { it.id }) { item ->
                            MaterialCard(
                                name = item.name,
                                info = item.info,
                                link = item.link,
                                isAdmin = isAdmin,
                                onOpenLink = { url ->
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    runCatching { context.startActivity(intent) }
                                },
                                onEdit = {
                                    editingItemId = item.id
                                    inputName = item.name
                                    inputInfo = item.info
                                    inputLink = item.link ?: ""
                                    showAddEditDialog = true
                                },
                                onDelete = { BeruniyRepository.deleteCourse(item.id) }
                            )
                        }
                    } else {
                        items(subjectTests, key = { it.id }) { item ->
                            MaterialCard(
                                name = item.name,
                                info = item.info,
                                link = item.link,
                                isAdmin = isAdmin,
                                onOpenLink = { url ->
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    runCatching { context.startActivity(intent) }
                                },
                                onEdit = {
                                    editingItemId = item.id
                                    inputName = item.name
                                    inputInfo = item.info
                                    inputLink = item.link ?: ""
                                    showAddEditDialog = true
                                },
                                onDelete = { BeruniyRepository.deleteTest(item.id) }
                            )
                        }
                    }
                }
            }

            // ADD / EDIT DIALOG
            if (showAddEditDialog) {
                AlertDialog(
                    onDismissRequest = { showAddEditDialog = false },
                    title = {
                        Text(
                            text = if (editingItemId == null) "Yangi qo'shish" else "O'zgartirish",
                            color = DarkText,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = inputName,
                                onValueChange = { inputName = it },
                                label = { Text("Nomi", color = DarkTextMuted) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = DarkText,
                                    unfocusedTextColor = DarkText,
                                    focusedBorderColor = BrandPrimaryBlue
                                ),
                                modifier = Modifier.fillMaxWidth().testTag("material_input_name")
                            )

                            OutlinedTextField(
                                value = inputInfo,
                                onValueChange = { inputInfo = it },
                                label = { Text("Tavsif (Info)", color = DarkTextMuted) },
                                minLines = 2,
                                maxLines = 4,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = DarkText,
                                    unfocusedTextColor = DarkText,
                                    focusedBorderColor = BrandPrimaryBlue
                                ),
                                modifier = Modifier.fillMaxWidth().testTag("material_input_info")
                            )

                            OutlinedTextField(
                                value = inputLink,
                                onValueChange = { inputLink = it },
                                label = { Text("Havola (ixtiyoriy)", color = DarkTextMuted) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = DarkText,
                                    unfocusedTextColor = DarkText,
                                    focusedBorderColor = BrandPrimaryBlue
                                ),
                                modifier = Modifier.fillMaxWidth().testTag("material_input_link")
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (inputName.isNotBlank() && inputInfo.isNotBlank()) {
                                    val linkVal = inputLink.trim().ifEmpty { null }
                                    if (isCourses) {
                                        if (editingItemId == null) {
                                            BeruniyRepository.addCourse(inputName, inputInfo, linkVal, currentSubject)
                                        } else {
                                            BeruniyRepository.updateCourse(editingItemId!!, inputName, inputInfo, linkVal, currentSubject)
                                        }
                                    } else {
                                        if (editingItemId == null) {
                                            BeruniyRepository.addTest(inputName, inputInfo, linkVal, currentSubject)
                                        } else {
                                            BeruniyRepository.updateTest(editingItemId!!, inputName, inputInfo, linkVal, currentSubject)
                                        }
                                    }
                                    showAddEditDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimaryBlue),
                            modifier = Modifier.testTag("material_save_btn")
                        ) {
                            Text("Saqlash", color = Color.White)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showAddEditDialog = false }) {
                            Text("Bekor qilish", color = DarkTextMuted)
                        }
                    },
                    containerColor = DarkCard
                )
            }
        }
    }
}

@Composable
fun MaterialCard(
    name: String,
    info: String,
    link: String?,
    isAdmin: Boolean,
    onOpenLink: (String) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = name,
                    color = DarkText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                if (isAdmin) {
                    Row {
                        IconButton(onClick = onEdit) {
                            Icon(Icons.Default.Edit, contentDescription = "O'zgartirish", tint = AccentOrange)
                        }
                        IconButton(onClick = onDelete) {
                            Icon(Icons.Default.Delete, contentDescription = "O'chirish", tint = SemanticError)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = info,
                color = DarkTextMuted,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            if (!link.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                FilledTonalButton(
                    onClick = { onOpenLink(link) },
                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = BrandPrimaryBlue.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Ochish ›", color = BrandPrimaryBlue, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
