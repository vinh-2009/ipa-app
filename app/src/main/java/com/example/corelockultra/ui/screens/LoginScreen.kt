package com.example.corelockultra.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.corelockultra.R
import com.example.corelockultra.theme.*
import com.example.corelockultra.ui.main.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: MainViewModel) {
    var keyText by remember { mutableStateOf("") }
    val authError by viewModel.authError.collectAsState()
    val isLoading by viewModel.isLoadingAuth.collectAsState()
    val context = LocalContext.current

    // Premium background gradient
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF0F0F0F), Color(0xFF161616))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .widthIn(max = 420.dp)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Glowing Logo effect
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(28.dp),
                        ambientColor = PrimaryOrange,
                        spotColor = PrimaryOrange
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .border(2.dp, Brush.linearGradient(listOf(PrimaryOrange, Color(0xFFFFB74D))), RoundedCornerShape(28.dp))
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Premium Title
            Text(
                text = "CORELOCK ULTRA",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                ),
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "SYSTEM OPTIMIZER PRO",
                color = TextGray,
                fontSize = 12.sp,
                letterSpacing = 4.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Premium Card
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E).copy(alpha = 0.9f)),
                border = BorderStroke(1.dp, Color(0xFF333333)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(32.dp)) {
                    
                    Text("Xác thực bản quyền", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 16.dp))

                    OutlinedTextField(
                        value = keyText,
                        onValueChange = { keyText = it },
                        placeholder = { Text("Nhập Key của bạn...", color = Color.Gray, fontSize = 14.sp) },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.VpnKey, 
                                contentDescription = "Key Icon",
                                tint = PrimaryOrange,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryOrange,
                            unfocusedBorderColor = Color(0xFF444444),
                            focusedContainerColor = Color(0xFF121212),
                            unfocusedContainerColor = Color(0xFF121212),
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            cursorColor = PrimaryOrange
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    if (authError != null) {
                        Text(
                            text = authError!!, 
                            color = ErrorRed, 
                            fontSize = 13.sp, 
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(ErrorRed.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                .padding(12.dp),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    // Login Button with Gradient
                    Button(
                        onClick = { viewModel.login(keyText) },
                        contentPadding = PaddingValues(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        enabled = !isLoading
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(Color(0xFFFF5722), Color(0xFFFF9800))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(color = TextWhite, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                            } else {
                                Text("KÍCH HOẠT", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Get Key Button
                    OutlinedButton(
                        onClick = { 
                            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://dntweakskey.netlify.app/"))
                            context.startActivity(i)
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = SecondaryBlue,
                            containerColor = SecondaryBlue.copy(alpha = 0.05f)
                        ),
                        border = BorderStroke(1.dp, SecondaryBlue.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth().height(54.dp)
                    ) {
                        Text("LẤY KEY (GET KEY)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
