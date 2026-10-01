package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.BeruniyRepository
import com.example.model.UZBEKISTAN_REGIONS
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit
) {
    var isRegisterMode by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Login Fields
    var loginEmail by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    // Register Fields
    var regName by remember { mutableStateOf("") }
    var regSurname by remember { mutableStateOf("") }
    var regUsername by remember { mutableStateOf("") }
    var regRegion by remember { mutableStateOf(UZBEKISTAN_REGIONS[0]) }
    var regAge by remember { mutableStateOf("") }
    var regPhone by remember { mutableStateOf("+998") }
    var regEmail by remember { mutableStateOf("") }
    var regPass1 by remember { mutableStateOf("") }
    var regPass2 by remember { mutableStateOf("") }
    var regionDropdownExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo / Header
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(DarkCardSecondary),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_icon_beruniy_1790856810601),
                    contentDescription = "Beruniy davomchilari logotipi",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Beruniy davomchilari",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Yoshlar 2030 — Kelajak yoshlar qo'lida",
                fontSize = 14.sp,
                color = AccentOrange,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Bilim · Innovatsiya · Yangi avlod",
                fontSize = 12.sp,
                color = DarkTextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            // Error display
            if (errorMessage != null) {
                Surface(
                    color = SemanticError.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = errorMessage ?: "",
                        color = SemanticError,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            if (!isRegisterMode) {
                // LOGIN FORM
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = loginEmail,
                            onValueChange = {
                                loginEmail = it
                                errorMessage = null
                            },
                            placeholder = { Text("Email", color = DarkTextMuted) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandPrimaryBlue,
                                unfocusedBorderColor = DarkCardSecondary,
                                focusedTextColor = DarkText,
                                unfocusedTextColor = DarkText,
                                focusedContainerColor = DarkCardSecondary,
                                unfocusedContainerColor = DarkCardSecondary
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_email_input")
                        )

                        OutlinedTextField(
                            value = loginPassword,
                            onValueChange = {
                                loginPassword = it
                                errorMessage = null
                            },
                            placeholder = { Text("Parol", color = DarkTextMuted) },
                            singleLine = true,
                            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            trailingIcon = {
                                IconButton(onClick = { showPassword = !showPassword }) {
                                    Icon(
                                        imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Parolni ko'rsatish",
                                        tint = DarkTextMuted
                                    )
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandPrimaryBlue,
                                unfocusedBorderColor = DarkCardSecondary,
                                focusedTextColor = DarkText,
                                unfocusedTextColor = DarkText,
                                focusedContainerColor = DarkCardSecondary,
                                unfocusedContainerColor = DarkCardSecondary
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_password_input")
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showPassword = !showPassword }
                        ) {
                            Checkbox(
                                checked = showPassword,
                                onCheckedChange = { showPassword = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = BrandPrimaryBlue,
                                    uncheckedColor = DarkTextMuted
                                )
                            )
                            Text(
                                text = "Parolni ko'rsatish",
                                color = DarkTextMuted,
                                fontSize = 13.sp
                            )
                        }

                        Button(
                            onClick = {
                                val result = BeruniyRepository.login(loginEmail, loginPassword)
                                if (result.isSuccess) {
                                    onAuthSuccess()
                                } else {
                                    errorMessage = result.exceptionOrNull()?.message
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimaryBlue),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("login_button")
                        ) {
                            Text(
                                text = "Kirish",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ro'yxatdan o'tmaganmisiz?",
                        color = DarkTextMuted,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Ro'yxatdan o'tish",
                        color = AccentOrange,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .clickable {
                                isRegisterMode = true
                                errorMessage = null
                            }
                            .testTag("goto_register_button")
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Quick Login Helper for Testing
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCardSecondary.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "⚡ Tezkor kirish (Sinov uchun):",
                            fontSize = 12.sp,
                            color = DarkTextMuted,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    loginEmail = "pirnazarovshoxruz567@gmail.com"
                                    loginPassword = "adminpassword"
                                    val r = BeruniyRepository.login(loginEmail, loginPassword)
                                    if (r.isSuccess) onAuthSuccess()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentOrange.copy(alpha = 0.2f)),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("👑 Admin", color = AccentOrange, fontSize = 12.sp)
                            }

                            Button(
                                onClick = {
                                    loginEmail = "madina@example.com"
                                    loginPassword = "userpassword"
                                    val r = BeruniyRepository.login(loginEmail, loginPassword)
                                    if (r.isSuccess) onAuthSuccess()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimaryBlue.copy(alpha = 0.2f)),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("👤 Talaba", color = BrandPrimaryBlue, fontSize = 12.sp)
                            }
                        }
                    }
                }
            } else {
                // REGISTRATION FORM
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Yangi hisob yaratish",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )

                        // Ism & Familiya
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = regName,
                                onValueChange = { regName = it; errorMessage = null },
                                placeholder = { Text("Ism", color = DarkTextMuted) },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BrandPrimaryBlue,
                                    unfocusedBorderColor = DarkCardSecondary,
                                    focusedTextColor = DarkText,
                                    unfocusedTextColor = DarkText,
                                    focusedContainerColor = DarkCardSecondary,
                                    unfocusedContainerColor = DarkCardSecondary
                                ),
                                modifier = Modifier.weight(1f).testTag("reg_name_input")
                            )

                            OutlinedTextField(
                                value = regSurname,
                                onValueChange = { regSurname = it; errorMessage = null },
                                placeholder = { Text("Familiya", color = DarkTextMuted) },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BrandPrimaryBlue,
                                    unfocusedBorderColor = DarkCardSecondary,
                                    focusedTextColor = DarkText,
                                    unfocusedTextColor = DarkText,
                                    focusedContainerColor = DarkCardSecondary,
                                    unfocusedContainerColor = DarkCardSecondary
                                ),
                                modifier = Modifier.weight(1f).testTag("reg_surname_input")
                            )
                        }

                        // Foydalanuvchi nomi
                        OutlinedTextField(
                            value = regUsername,
                            onValueChange = { regUsername = it; errorMessage = null },
                            placeholder = { Text("Foydalanuvchi nomi (@username)", color = DarkTextMuted) },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandPrimaryBlue,
                                unfocusedBorderColor = DarkCardSecondary,
                                focusedTextColor = DarkText,
                                unfocusedTextColor = DarkText,
                                focusedContainerColor = DarkCardSecondary,
                                unfocusedContainerColor = DarkCardSecondary
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("reg_username_input")
                        )

                        // Manzil (Dropdown of regions)
                        ExposedDropdownMenuBox(
                            expanded = regionDropdownExpanded,
                            onExpandedChange = { regionDropdownExpanded = !regionDropdownExpanded }
                        ) {
                            OutlinedTextField(
                                value = regRegion,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Manzil", color = DarkTextMuted) },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Manzil tanlash",
                                        tint = DarkTextMuted
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BrandPrimaryBlue,
                                    unfocusedBorderColor = DarkCardSecondary,
                                    focusedTextColor = DarkText,
                                    unfocusedTextColor = DarkText,
                                    focusedContainerColor = DarkCardSecondary,
                                    unfocusedContainerColor = DarkCardSecondary
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = regionDropdownExpanded,
                                onDismissRequest = { regionDropdownExpanded = false },
                                modifier = Modifier.background(DarkCardSecondary)
                            ) {
                                UZBEKISTAN_REGIONS.forEach { regionItem ->
                                    DropdownMenuItem(
                                        text = { Text(regionItem, color = DarkText) },
                                        onClick = {
                                            regRegion = regionItem
                                            regionDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Yoshi & Telefon
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = regAge,
                                onValueChange = {
                                    if (it.length <= 2 && it.all { char -> char.isDigit() }) {
                                        regAge = it
                                        errorMessage = null
                                    }
                                },
                                placeholder = { Text("Yoshi (5-99)", color = DarkTextMuted) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BrandPrimaryBlue,
                                    unfocusedBorderColor = DarkCardSecondary,
                                    focusedTextColor = DarkText,
                                    unfocusedTextColor = DarkText,
                                    focusedContainerColor = DarkCardSecondary,
                                    unfocusedContainerColor = DarkCardSecondary
                                ),
                                modifier = Modifier.width(110.dp).testTag("reg_age_input")
                            )

                            OutlinedTextField(
                                value = regPhone,
                                onValueChange = {
                                    regPhone = it
                                    errorMessage = null
                                },
                                placeholder = { Text("+(998) __ ___ __ __", color = DarkTextMuted) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BrandPrimaryBlue,
                                    unfocusedBorderColor = DarkCardSecondary,
                                    focusedTextColor = DarkText,
                                    unfocusedTextColor = DarkText,
                                    focusedContainerColor = DarkCardSecondary,
                                    unfocusedContainerColor = DarkCardSecondary
                                ),
                                modifier = Modifier.weight(1f).testTag("reg_phone_input")
                            )
                        }

                        // Email
                        OutlinedTextField(
                            value = regEmail,
                            onValueChange = { regEmail = it; errorMessage = null },
                            placeholder = { Text("Email", color = DarkTextMuted) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandPrimaryBlue,
                                unfocusedBorderColor = DarkCardSecondary,
                                focusedTextColor = DarkText,
                                unfocusedTextColor = DarkText,
                                focusedContainerColor = DarkCardSecondary,
                                unfocusedContainerColor = DarkCardSecondary
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("reg_email_input")
                        )

                        // Parol 1 & 2
                        OutlinedTextField(
                            value = regPass1,
                            onValueChange = { regPass1 = it; errorMessage = null },
                            placeholder = { Text("Parol (kamida 6 belgi)", color = DarkTextMuted) },
                            singleLine = true,
                            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandPrimaryBlue,
                                unfocusedBorderColor = DarkCardSecondary,
                                focusedTextColor = DarkText,
                                unfocusedTextColor = DarkText,
                                focusedContainerColor = DarkCardSecondary,
                                unfocusedContainerColor = DarkCardSecondary
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("reg_pass1_input")
                        )

                        OutlinedTextField(
                            value = regPass2,
                            onValueChange = { regPass2 = it; errorMessage = null },
                            placeholder = { Text("Parolni qayta kiriting", color = DarkTextMuted) },
                            singleLine = true,
                            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandPrimaryBlue,
                                unfocusedBorderColor = DarkCardSecondary,
                                focusedTextColor = DarkText,
                                unfocusedTextColor = DarkText,
                                focusedContainerColor = DarkCardSecondary,
                                unfocusedContainerColor = DarkCardSecondary
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("reg_pass2_input")
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showPassword = !showPassword }
                        ) {
                            Checkbox(
                                checked = showPassword,
                                onCheckedChange = { showPassword = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = BrandPrimaryBlue,
                                    uncheckedColor = DarkTextMuted
                                )
                            )
                            Text(
                                text = "Parolni ko'rsatish",
                                color = DarkTextMuted,
                                fontSize = 13.sp
                            )
                        }

                        Button(
                            onClick = {
                                val ageVal = regAge.toIntOrNull()
                                val res = BeruniyRepository.register(
                                    name = regName,
                                    surname = regSurname,
                                    username = regUsername,
                                    region = regRegion,
                                    age = ageVal,
                                    phone = regPhone,
                                    email = regEmail,
                                    pass1 = regPass1,
                                    pass2 = regPass2
                                )
                                if (res.isSuccess) {
                                    onAuthSuccess()
                                } else {
                                    errorMessage = res.exceptionOrNull()?.message
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimaryBlue),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("register_submit_button")
                        ) {
                            Text(
                                text = "Ro'yxatdan o'tish",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        TextButton(
                            onClick = {
                                isRegisterMode = false
                                errorMessage = null
                            },
                            modifier = Modifier.fillMaxWidth().testTag("back_to_login_button")
                        ) {
                            Text(
                                text = "Bosh sahifaga qaytish",
                                color = DarkTextMuted,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
