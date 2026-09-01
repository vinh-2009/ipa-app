package com.example.corelockultra.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.corelockultra.R
import com.example.corelockultra.theme.*
import com.example.corelockultra.ui.main.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToOptimizer: () -> Unit
) {
    val isShizukuAvailable by viewModel.isShizukuAvailable.collectAsState()
    val hasShizukuPermission by viewModel.hasShizukuPermission.collectAsState()
    val isRootAvailable by viewModel.isRootAvailable.collectAsState()
    val selectedGame by viewModel.selectedGame.collectAsState()
    val isGameRunning by viewModel.isGameRunning.collectAsState()
    val isOptimizerStarted by viewModel.isOptimizerStarted.collectAsState()
    val expiresAt by viewModel.expiresAt.collectAsState()
    val context = LocalContext.current

    val hasPrivilege = hasShizukuPermission || isRootAvailable

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Top Card (Shizuku/Root)
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.app_logo),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(48.dp).padding(end = 8.dp)
                    )
                    Column {
                        Text("TWEAKS CORELOCK ULTRA", color = TextWhite, fontWeight = FontWeight.Bold)
                        if (isOptimizerStarted) {
                            Text("Trạng thái: Sẵn sàng", color = SuccessGreen, fontSize = 12.sp)
                        } else if (isRootAvailable) {
                            Text("Root đã cấp quyền ✓", color = SuccessGreen, fontSize = 12.sp)
                        } else if (hasShizukuPermission) {
                            Text("Shizuku đã kết nối ✓", color = SuccessGreen, fontSize = 12.sp)
                        } else {
                            Text("Chưa kết nối Shizuku", color = ErrorRed, fontSize = 12.sp)
                            Text("Vui lòng nhấn Kết nối", color = PrimaryOrange, fontSize = 12.sp)
                        }
                    }
                }
                
                Button(
                    onClick = { 
                        if (!hasPrivilege) {
                            val success = viewModel.requestShizuku()
                            if (!success) {
                                Toast.makeText(context, "Shizuku chưa chạy hoặc không tìm thấy!", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            viewModel.startOptimizer()
                            onNavigateToOptimizer()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = if (hasPrivilege) SecondaryBlue else PrimaryOrange),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (hasPrivilege) "Mở Menu" else "Kết nối", color = TextWhite)
                }
            }
        }

        // Game Selection
        Text("Lựa chọn phiên bản:", color = TextWhite, modifier = Modifier.padding(bottom = 8.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GameCard(
                title = "Free Fire MAX",
                imageRes = R.drawable.ffmax_icon,
                isSelected = selectedGame == "com.dts.freefiremax",
                onClick = { viewModel.selectGame("com.dts.freefiremax") },
                modifier = Modifier.weight(1f)
            )
            GameCard(
                title = "Free Fire",
                imageRes = R.drawable.ff_icon,
                isSelected = selectedGame == "com.dts.freefireth",
                onClick = { viewModel.selectGame("com.dts.freefireth") },
                modifier = Modifier.weight(1f)
            )
        }

        // Status Card
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier
                        .size(10.dp)
                        .background(if (isGameRunning) SuccessGreen else TextGray, RoundedCornerShape(5.dp)))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Trạng thái", color = TextWhite, fontWeight = FontWeight.Bold)
                        Text(if (isGameRunning) "Đã phát hiện game" else "Không phát hiện game", color = TextGray, fontSize = 12.sp)
                    }
                }
                Button(
                    onClick = { 
                        val intent = context.packageManager.getLaunchIntentForPackage(selectedGame)
                        if (intent != null) {
                            context.startActivity(intent)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Mở Game", color = TextWhite)
                }
            }
        }

        // Info Cards
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val formattedDate = try {
                if (expiresAt != null) {
                    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply { timeZone = TimeZone.getTimeZone("UTC") }
                    val date = inputFormat.parse(expiresAt!!)
                    val outputFormat = SimpleDateFormat("HH:mm:ss\ndd/MM/yyyy", Locale.getDefault())
                    outputFormat.format(date!!)
                } else "Unknown"
            } catch (e: Exception) {
                "Unknown"
            }
            InfoCard(title = "Thời hạn key", value = formattedDate, valueColor = SuccessGreen, modifier = Modifier.weight(1f))
            InfoCard(title = "Trạng thái Optimizer", value = if(isOptimizerStarted) "Đang hoạt động" else "Không\nhoạt động", valueColor = if(isOptimizerStarted) SuccessGreen else ErrorRed, modifier = Modifier.weight(1f))
        }

        // Progress Card
        if (isOptimizerStarted) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("✓ Đã cập nhật dữ liệu chức năng vào thiết bị", color = SuccessGreen, fontSize = 12.sp)
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), color = SuccessGreen, trackColor = BackgroundDark)
                    Text("✓ Sẵn sàng", color = TextGray, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        // Contact Card
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Liên hệ Hỗ trợ", color = TextWhite, fontWeight = FontWeight.Bold)
                    Text("Nhấn để nhận hỗ trợ", color = TextGray, fontSize = 12.sp)
                }
                Button(
                    onClick = { 
                        val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/dntweaks"))
                        context.startActivity(i)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SecondaryBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Join", color = TextWhite)
                }
            }
        }
    }
}

@Composable
fun GameCard(title: String, imageRes: Int, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        border = if (isSelected) BorderStroke(2.dp, BorderBlue) else null,
        modifier = modifier.clickable(onClick = onClick).height(80.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(56.dp).clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(title, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun InfoCard(title: String, value: String, valueColor: Color, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier.height(100.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Text(title, color = TextGray, fontSize = 12.sp)
            Text(value, color = valueColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}
