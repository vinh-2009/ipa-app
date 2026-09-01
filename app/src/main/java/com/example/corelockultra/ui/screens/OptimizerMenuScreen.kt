package com.example.corelockultra.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.corelockultra.manager.OptimizerManager
import com.example.corelockultra.manager.OptimizerState
import com.example.corelockultra.optimizers.OptimizerStatus
import com.example.corelockultra.theme.*
import com.example.corelockultra.ui.main.MainViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptimizerMenuScreen(viewModel: MainViewModel, onNavigateBack: () -> Unit) {
    val states by OptimizerManager.optimizerStates.collectAsState()
    val expiresAt by viewModel.expiresAt.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menu Optimizer", color = TextWhite) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).padding(horizontal = 16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                // Info Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardDark),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp, top = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(10.dp).background(SuccessGreen, RoundedCornerShape(5.dp)))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Shizuku đã cấp quyền", color = SuccessGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        val formattedDate = try {
                            if (expiresAt != null) {
                                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply { timeZone = TimeZone.getTimeZone("UTC") }
                                val date = inputFormat.parse(expiresAt!!)
                                val outputFormat = SimpleDateFormat("HH:mm:ss - dd/MM/yyyy", Locale.getDefault())
                                outputFormat.format(date!!)
                            } else "Unknown"
                        } catch (e: Exception) {
                            "Unknown"
                        }
                        
                        Text("Thiết bị: ${viewModel.deviceId}", color = TextGray, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Thời hạn Key: $formattedDate", color = TextGray, fontSize = 12.sp)
                    }
                }
            }
            
            items(states) { state ->
                OptimizerCard(state = state, onToggle = { isEnabled ->
                    scope.launch {
                        OptimizerManager.toggleOptimizer(state.module.id, isEnabled)
                    }
                })
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { /* Could launch game here too */ },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Khởi chạy Game", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun OptimizerCard(state: OptimizerState, onToggle: (Boolean) -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(state.module.name, color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(state.module.description, color = TextGray, fontSize = 12.sp)
                
                Spacer(modifier = Modifier.height(4.dp))
                val statusColor = when (state.status) {
                    OptimizerStatus.SUCCESS -> SuccessGreen
                    OptimizerStatus.ERROR, OptimizerStatus.NOT_SUPPORTED -> ErrorRed
                    OptimizerStatus.APPLYING -> SecondaryBlue
                    OptimizerStatus.INACTIVE -> TextGray
                }
                val statusText = when (state.status) {
                    OptimizerStatus.SUCCESS -> "Thành công"
                    OptimizerStatus.ERROR -> "Lỗi"
                    OptimizerStatus.NOT_SUPPORTED -> "Thiết bị không hỗ trợ"
                    OptimizerStatus.APPLYING -> "Đang áp dụng..."
                    OptimizerStatus.INACTIVE -> ""
                }
                if (statusText.isNotEmpty()) {
                    Text(statusText, color = statusColor, fontSize = 10.sp)
                }
            }
            Switch(
                checked = state.isEnabled,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = TextWhite,
                    checkedTrackColor = SecondaryBlue,
                    uncheckedThumbColor = TextGray,
                    uncheckedTrackColor = BackgroundDark
                )
            )
        }
    }
}
