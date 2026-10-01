package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.BeruniyRepository
import com.example.model.User
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    currentUser: User?,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val isAdmin = currentUser?.isAdmin == true
    val aboutInfo by BeruniyRepository.aboutInfo.collectAsState()

    var showEditTextDialog by remember { mutableStateOf(false) }
    var editTextValue by remember { mutableStateOf(aboutInfo.text) }

    var showAddLinkDialog by remember { mutableStateOf(false) }
    var linkTitle by remember { mutableStateOf("") }
    var linkUrl by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ℹ️ Biz haqimizda",
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Header Image
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.beruniy_banner_1790856887942),
                        contentDescription = "Biz haqimizda",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Description Card with Admin Edit
            item {
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
                            Text(
                                text = "Loyiha Maqsadi va Missiyasi",
                                color = AccentGold,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )

                            if (isAdmin) {
                                TextButton(
                                    onClick = {
                                        editTextValue = aboutInfo.text
                                        showEditTextDialog = true
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Matnni tahrirlash",
                                        tint = AccentOrange,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Tahrirlash", color = AccentOrange, fontSize = 12.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = aboutInfo.text,
                            color = DarkText,
                            fontSize = 14.sp,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            // Photo Showcase
            item {
                Text(
                    text = "🖼️ Fotolavhalar",
                    color = DarkText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(BrandPrimaryBlue.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🔬", fontSize = 28.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Ilm-fan", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(AccentOrange.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("💡", fontSize = 28.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Innovatsiya", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(AccentGold.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🚀", fontSize = 28.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Kelajak", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Useful Links
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "🔗 Foydali Havolalar",
                        color = DarkText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (isAdmin) {
                        TextButton(onClick = {
                            linkTitle = ""
                            linkUrl = "https://"
                            showAddLinkDialog = true
                        }) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = AccentOrange, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("➕ Havola qo'shish", color = AccentOrange, fontSize = 12.sp)
                        }
                    }
                }
            }

            items(aboutInfo.links, key = { it.id }) { linkItem ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCardSecondary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Link, contentDescription = null, tint = BrandPrimaryBlue)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = linkItem.title,
                                    color = DarkText,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = linkItem.url,
                                    color = DarkTextMuted,
                                    fontSize = 11.sp,
                                    maxLines = 1
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            FilledTonalButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkItem.url))
                                    runCatching { context.startActivity(intent) }
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.filledTonalButtonColors(containerColor = BrandPrimaryBlue.copy(alpha = 0.2f))
                            ) {
                                Text("Ochish ›", color = BrandPrimaryBlue, fontSize = 12.sp)
                            }

                            if (isAdmin) {
                                IconButton(onClick = { BeruniyRepository.deleteAboutLink(linkItem.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "O'chirish", tint = SemanticError)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Edit Text Dialog
        if (showEditTextDialog) {
            AlertDialog(
                onDismissRequest = { showEditTextDialog = false },
                title = { Text("Loyiha matnini tahrirlash", color = DarkText, fontWeight = FontWeight.Bold) },
                text = {
                    OutlinedTextField(
                        value = editTextValue,
                        onValueChange = { editTextValue = it },
                        label = { Text("Biz haqimizda matni", color = DarkTextMuted) },
                        minLines = 4,
                        maxLines = 8,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = DarkText,
                            unfocusedTextColor = DarkText,
                            focusedBorderColor = BrandPrimaryBlue
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("about_text_input")
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            BeruniyRepository.updateAboutText(editTextValue)
                            showEditTextDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimaryBlue),
                        modifier = Modifier.testTag("about_save_text_btn")
                    ) {
                        Text("Matnni saqlash", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEditTextDialog = false }) {
                        Text("Bekor qilish", color = DarkTextMuted)
                    }
                },
                containerColor = DarkCard
            )
        }

        // Add Link Dialog
        if (showAddLinkDialog) {
            AlertDialog(
                onDismissRequest = { showAddLinkDialog = false },
                title = { Text("➕ Havola qo'shish", color = DarkText, fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = linkTitle,
                            onValueChange = { linkTitle = it },
                            label = { Text("Havola nomi", color = DarkTextMuted) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = DarkText,
                                unfocusedTextColor = DarkText,
                                focusedBorderColor = BrandPrimaryBlue
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("about_link_title")
                        )

                        OutlinedTextField(
                            value = linkUrl,
                            onValueChange = { linkUrl = it },
                            label = { Text("Manzil (URL)", color = DarkTextMuted) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = DarkText,
                                unfocusedTextColor = DarkText,
                                focusedBorderColor = BrandPrimaryBlue
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("about_link_url")
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (linkTitle.isNotBlank() && linkUrl.isNotBlank()) {
                                BeruniyRepository.addAboutLink(linkTitle, linkUrl)
                                showAddLinkDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimaryBlue)
                    ) {
                        Text("Saqlash", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddLinkDialog = false }) {
                        Text("Bekor qilish", color = DarkTextMuted)
                    }
                },
                containerColor = DarkCard
            )
        }
    }
}
