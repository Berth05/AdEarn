package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.RoseError

@Composable
fun CaptchaVerificationDialog(
    onVerificationSuccess: (bonusPoints: Long) -> Unit,
    onDismiss: () -> Unit
) {
    // 4 tiles: target is the golden coin (index 2)
    val targetIndex = 2
    var selectedIndex by remember { mutableIntStateOf(-1) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isVerified by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("captcha_dialog"),
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFF16182E),
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Shield Icon
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(CyanAccent.copy(alpha = 0.15f))
                        .border(1.5.dp, CyanAccent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Security Check",
                        tint = CyanAccent,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Human Security Verification",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Select the Golden Coin below to verify human attendance and unlock +25 Anti-Fraud Bonus points.",
                    color = Color(0xFF94A3B8),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Interactive 4 tiles grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val tiles = listOf(
                        Triple(0, Icons.Default.Lock, "Lock"),
                        Triple(1, Icons.Default.Star, "Star"),
                        Triple(2, Icons.Default.MonetizationOn, "Coin"),
                        Triple(3, Icons.Default.Shield, "Shield")
                    )

                    tiles.forEach { (index, icon, label) ->
                        val isSelected = selectedIndex == index
                        val isCorrect = index == targetIndex

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(72.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    when {
                                        isSelected && isCorrect -> EmeraldPrimary.copy(alpha = 0.2f)
                                        isSelected && !isCorrect -> RoseError.copy(alpha = 0.2f)
                                        else -> Color(0xFF1E213D)
                                    }
                                )
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = when {
                                        isSelected && isCorrect -> EmeraldPrimary
                                        isSelected && !isCorrect -> RoseError
                                        else -> Color(0xFF2C3056)
                                    },
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    selectedIndex = index
                                    if (index == targetIndex) {
                                        errorMessage = null
                                        isVerified = true
                                    } else {
                                        errorMessage = "Incorrect tile selected. Please tap the golden coin."
                                        isVerified = false
                                    }
                                }
                                .testTag("captcha_tile_$index"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = if (index == targetIndex) GoldAccent else Color(0xFFCBD5E1),
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = label,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = errorMessage ?: "",
                        color = RoseError,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }

                if (isVerified) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Device and Human Integrity Verified!",
                            color = EmeraldPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (isVerified) {
                            onVerificationSuccess(25L)
                        } else {
                            errorMessage = "Please tap the golden coin first to verify."
                        }
                    },
                    enabled = isVerified,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldPrimary,
                        disabledContainerColor = Color(0xFF2C3056),
                        contentColor = Color.Black,
                        disabledContentColor = Color(0xFF64748B)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_captcha_button")
                ) {
                    Text(
                        text = if (isVerified) "Confirm & Claim +25 Bonus" else "Tap Golden Coin Above",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
