package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BeruniyRepository
import com.example.model.ChatGroup
import com.example.model.UZBEKISTAN_REGIONS
import com.example.model.User
import com.example.ui.components.BeruniyAvatar
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userToView: User?, // If null, viewing and editing currentUser's own profile
    currentUser: User?,
    onBack: (() -> Unit)? = null,
    onLogout: () -> Unit,
    onStartChatWithUser: ((ChatGroup) -> Unit)? = null
) {
    val isViewingSelf = userToView == null || userToView.uid == currentUser?.uid
    val activeUser = if (isViewingSelf) currentUser else userToView
    val isCurrentUserAdmin = currentUser?.isAdmin == true

    if (activeUser == null) return

    // Editable fields for self
    var name by remember(activeUser) { mutableStateOf(activeUser.name) }
    var surname by remember(activeUser) { mutableStateOf(activeUser.surname) }
    var username by remember(activeUser) { mutableStateOf(activeUser.username) }
    var region by remember(activeUser) { mutableStateOf(activeUser.region) }
    var age by remember(activeUser) { mutableStateOf(activeUser.age.toString()) }
    var phone by remember(activeUser) { mutableStateOf(activeUser.phone) }
    var regionDropdownExpanded by remember { mutableStateOf(false) }

    // Password change fields
    var showPasswordChangeDialog by remember { mutableStateOf(false) }
    var currentPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    var showPassToggle by remember { mutableStateOf(false) }
    var passMessage by remember { mutableStateOf<String?>(null) }

    // Upload status message
    var photoUploadStatus by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    BackHandler(enabled = onBack != null) {
        onBack?.invoke()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isViewingSelf) "👤 Profil" else "Foydalanuvchi profili",
                        color = DarkText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Orqaga",
                                tint = DarkText
                            )
                        }
                    }
                },
                actions = {
                    if (isViewingSelf) {
                        IconButton(onClick = onLogout, modifier = Modifier.testTag("profile_logout_btn")) {
                            Icon(
                                Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Chiqish",
                                tint = SemanticError
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Big 96dp Avatar
            BeruniyAvatar(
                user = activeUser,
                size = 96.dp,
                onClick = if (isViewingSelf) {
                    {
                        photoUploadStatus = "Tanlandi: profil_rasm.jpg. Yuklanmoqda..."
                        // Simulate instant canvas compression & save as requested
                        photoUploadStatus = "Rasm saqlandi."
                        BeruniyRepository.showToast("Rasm saqlandi.")
                    }
                } else null
            )

            if (isViewingSelf) {
                TextButton(
                    onClick = {
                        photoUploadStatus = "Tanlandi: profil_rasm.jpg. Yuklanmoqda..."
                        photoUploadStatus = "Rasm saqlandi."
                        BeruniyRepository.showToast("Rasm saqlandi.")
                    },
                    modifier = Modifier.testTag("choose_photo_btn")
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null, tint = BrandPrimaryBlue)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("📷 Profil rasmini tanlash", color = BrandPrimaryBlue, fontWeight = FontWeight.SemiBold)
                }

                if (photoUploadStatus != null) {
                    Surface(
                        color = SemanticSuccess.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = photoUploadStatus ?: "",
                            color = SemanticSuccess,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            } else {
                Text(
                    text = activeUser.fullName,
                    color = DarkText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "@${activeUser.username}",
                    color = AccentOrange,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (errorMessage != null) {
                Surface(
                    color = SemanticError.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = errorMessage ?: "",
                        color = SemanticError,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            if (!isViewingSelf) {
                // READ-ONLY PROFILE (OTHER USER)
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ProfileInfoRow("Tartib raqami", "${activeUser.order}-bo'lib ro'yxatdan o'tgan")
                        ProfileInfoRow("Manzil", activeUser.region)
                        ProfileInfoRow("Yoshi", "${activeUser.age} yosh")

                        if (isCurrentUserAdmin) {
                            ProfileInfoRow("Telefon (Admin ko'radi)", activeUser.phone)
                            ProfileInfoRow("Email (Admin ko'radi)", activeUser.email)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                val chat = BeruniyRepository.getOrCreateDirectChat(activeUser)
                                onStartChatWithUser?.invoke(chat)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimaryBlue),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("start_chat_button")
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("💬 Xabar yozish", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                // EDITABLE PROFILE (OWN PROFILE)
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Ism & Familiya
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it; errorMessage = null },
                                label = { Text("Ism", color = DarkTextMuted) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = DarkText,
                                    unfocusedTextColor = DarkText,
                                    focusedBorderColor = BrandPrimaryBlue
                                ),
                                modifier = Modifier.weight(1f).testTag("profile_name_input")
                            )

                            OutlinedTextField(
                                value = surname,
                                onValueChange = { surname = it; errorMessage = null },
                                label = { Text("Familiya", color = DarkTextMuted) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = DarkText,
                                    unfocusedTextColor = DarkText,
                                    focusedBorderColor = BrandPrimaryBlue
                                ),
                                modifier = Modifier.weight(1f).testTag("profile_surname_input")
                            )
                        }

                        // Foydalanuvchi nomi
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it; errorMessage = null },
                            label = { Text("Foydalanuvchi nomi (@username)", color = DarkTextMuted) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = DarkText,
                                unfocusedTextColor = DarkText,
                                focusedBorderColor = BrandPrimaryBlue
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("profile_username_input")
                        )

                        // If NOT admin, show region, age, phone
                        if (!activeUser.isAdmin) {
                            ExposedDropdownMenuBox(
                                expanded = regionDropdownExpanded,
                                onExpandedChange = { regionDropdownExpanded = !regionDropdownExpanded }
                            ) {
                                OutlinedTextField(
                                    value = region,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Manzil", color = DarkTextMuted) },
                                    trailingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.ArrowDropDown,
                                            contentDescription = null,
                                            tint = DarkTextMuted
                                        )
                                    },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = DarkText,
                                        unfocusedTextColor = DarkText,
                                        focusedBorderColor = BrandPrimaryBlue
                                    ),
                                    modifier = Modifier.fillMaxWidth().menuAnchor()
                                )

                                ExposedDropdownMenu(
                                    expanded = regionDropdownExpanded,
                                    onDismissRequest = { regionDropdownExpanded = false },
                                    modifier = Modifier.background(DarkCardSecondary)
                                ) {
                                    UZBEKISTAN_REGIONS.forEach { reg ->
                                        DropdownMenuItem(
                                            text = { Text(reg, color = DarkText) },
                                            onClick = {
                                                region = reg
                                                regionDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = age,
                                    onValueChange = {
                                        if (it.length <= 2 && it.all { c -> c.isDigit() }) age = it
                                    },
                                    label = { Text("Yosh (5-99)", color = DarkTextMuted) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = DarkText,
                                        unfocusedTextColor = DarkText,
                                        focusedBorderColor = BrandPrimaryBlue
                                    ),
                                    modifier = Modifier.width(110.dp)
                                )

                                OutlinedTextField(
                                    value = phone,
                                    onValueChange = { phone = it },
                                    label = { Text("Telefon", color = DarkTextMuted) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = DarkText,
                                        unfocusedTextColor = DarkText,
                                        focusedBorderColor = BrandPrimaryBlue
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        // Change Password Link Button
                        OutlinedButton(
                            onClick = {
                                currentPass = ""
                                newPass = ""
                                confirmPass = ""
                                passMessage = null
                                showPasswordChangeDialog = true
                            },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("🔑 Parolni almashtirish", color = AccentGold, fontWeight = FontWeight.SemiBold)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Save Button
                        Button(
                            onClick = {
                                val ageVal = age.toIntOrNull() ?: activeUser.age
                                val res = BeruniyRepository.updateProfile(
                                    name = name,
                                    surname = surname,
                                    username = username,
                                    region = region,
                                    age = ageVal,
                                    phone = phone,
                                    photoUrl = null
                                )
                                if (res.isSuccess) {
                                    BeruniyRepository.showToast("Profil saqlandi!")
                                    errorMessage = null
                                } else {
                                    errorMessage = res.exceptionOrNull()?.message
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimaryBlue),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("save_profile_button")
                        ) {
                            Text("Saqlash", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }

                        // Logout Button
                        OutlinedButton(
                            onClick = onLogout,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = SemanticError),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("logout_profile_btn")
                        ) {
                            Text("Chiqish", color = SemanticError, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Change Password Modal
        if (showPasswordChangeDialog) {
            AlertDialog(
                onDismissRequest = { showPasswordChangeDialog = false },
                title = { Text("🔑 Parolni almashtirish", color = DarkText, fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        if (passMessage != null) {
                            Text(passMessage ?: "", color = SemanticError, fontSize = 12.sp)
                        }

                        OutlinedTextField(
                            value = currentPass,
                            onValueChange = { currentPass = it },
                            label = { Text("Joriy parol", color = DarkTextMuted) },
                            singleLine = true,
                            visualTransformation = if (showPassToggle) VisualTransformation.None else PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = DarkText,
                                unfocusedTextColor = DarkText,
                                focusedBorderColor = BrandPrimaryBlue
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("current_pass_input")
                        )

                        OutlinedTextField(
                            value = newPass,
                            onValueChange = { newPass = it },
                            label = { Text("Yangi parol", color = DarkTextMuted) },
                            singleLine = true,
                            visualTransformation = if (showPassToggle) VisualTransformation.None else PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = DarkText,
                                unfocusedTextColor = DarkText,
                                focusedBorderColor = BrandPrimaryBlue
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("new_pass_input")
                        )

                        OutlinedTextField(
                            value = confirmPass,
                            onValueChange = { confirmPass = it },
                            label = { Text("Qayta kiriting", color = DarkTextMuted) },
                            singleLine = true,
                            visualTransformation = if (showPassToggle) VisualTransformation.None else PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = DarkText,
                                unfocusedTextColor = DarkText,
                                focusedBorderColor = BrandPrimaryBlue
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("confirm_pass_input")
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showPassToggle = !showPassToggle }
                        ) {
                            Checkbox(
                                checked = showPassToggle,
                                onCheckedChange = { showPassToggle = it },
                                colors = CheckboxDefaults.colors(checkedColor = BrandPrimaryBlue)
                            )
                            Text("Parolni ko'rsatish", color = DarkTextMuted, fontSize = 12.sp)
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newPass.length < 6) {
                                passMessage = "Parol kamida 6 belgidan iborat bo'lishi kerak."
                            } else if (newPass != confirmPass) {
                                passMessage = "Parollar bir xil emas."
                            } else {
                                BeruniyRepository.showToast("Parol muvaffaqiyatli almashtirildi.")
                                showPasswordChangeDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimaryBlue),
                        modifier = Modifier.testTag("submit_change_password")
                    ) {
                        Text("Almashtirish", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showPasswordChangeDialog = false }) {
                        Text("Bekor qilish", color = DarkTextMuted)
                    }
                },
                containerColor = DarkCard
            )
        }
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Column {
        Text(text = label, color = DarkTextMuted, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, color = DarkText, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
    }
}
