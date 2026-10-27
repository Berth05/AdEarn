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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.UserProfile
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.RoseError

data class CashoutMethodInfo(
    val id: String,
    val name: String,
    val icon: ImageVector,
    val hint: String,
    val minAmountUsd: Double,
    val feeLabel: String = "0% Fee"
)

@Composable
fun WithdrawalModal(
    profile: UserProfile,
    onSubmitWithdrawal: (method: String, destination: String, points: Long, usd: Double) -> Unit,
    onDismiss: () -> Unit
) {
    val methods = listOf(
        CashoutMethodInfo("PayPal", "PayPal", Icons.Default.Payment, "Enter PayPal email address", 2.0),
        CashoutMethodInfo("GCash", "GCash Wallet", Icons.Default.PhoneAndroid, "Enter 11-digit GCash mobile (09XXXXXXXXX)", 2.0),
        CashoutMethodInfo("USDT", "USDT (TRC20)", Icons.Default.CurrencyBitcoin, "Enter TRC20 wallet address (T...)", 5.0),
        CashoutMethodInfo("Amazon", "Amazon Gift Card", Icons.Default.CardGiftcard, "Enter recipient email address", 5.0),
        CashoutMethodInfo("Bank", "Bank Transfer", Icons.Default.AccountBalance, "Enter Account Name & IBAN/Number", 10.0)
    )

    var selectedMethod by remember { mutableStateOf(methods[0]) }
    var selectedUsdAmount by remember { mutableDoubleStateOf(2.0) }
    var destinationInput by remember { mutableStateOf("") }
    var inputError by remember { mutableStateOf<String?>(null) }

    val requiredPoints = (selectedUsdAmount * 1000).toLong()
    val hasEnoughPoints = profile.balancePoints >= requiredPoints

    val amountOptions = when (selectedMethod.minAmountUsd) {
        10.0 -> listOf(10.0, 25.0, 50.0)
        5.0 -> listOf(5.0, 10.0, 25.0, 50.0)
        else -> listOf(2.0, 5.0, 10.0, 25.0)
    }

    // Ensure selected amount matches method minimum
    if (selectedUsdAmount < selectedMethod.minAmountUsd) {
        selectedUsdAmount = selectedMethod.minAmountUsd
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .testTag("withdrawal_dialog"),
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFF16182E),
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Cash Out Earnings",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Available: ${profile.balancePoints} Pts ($${String.format("%.2f", profile.balanceUsd)})",
                            color = EmeraldPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Select Method Title
                Text(
                    text = "1. Select Payout Method",
                    color = Color(0xFFCBD5E1),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Method Pills / Cards
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    methods.forEach { method ->
                        val isSelected = selectedMethod.id == method.id
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) IndigoAccent.copy(alpha = 0.2f) else Color(0xFF1E213D))
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) IndigoAccent else Color(0xFF2C3056),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    selectedMethod = method
                                    inputError = null
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                                .testTag("method_${method.id}"),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) IndigoAccent else Color(0xFF2C3056)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = method.icon,
                                        contentDescription = method.name,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = method.name,
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "Min: $${String.format("%.0f", method.minAmountUsd)} • ${method.feeLabel}",
                                        color = Color(0xFF94A3B8),
                                        fontSize = 11.sp
                                    )
                                }
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Select Amount
                Text(
                    text = "2. Select Cash Amount",
                    color = Color(0xFFCBD5E1),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    amountOptions.forEach { amount ->
                        val isSelected = selectedUsdAmount == amount
                        val pts = (amount * 1000).toLong()
                        val affordable = profile.balancePoints >= pts

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    when {
                                        isSelected -> GoldAccent.copy(alpha = 0.2f)
                                        !affordable -> Color(0xFF1E213D).copy(alpha = 0.5f)
                                        else -> Color(0xFF1E213D)
                                    }
                                )
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = when {
                                        isSelected -> GoldAccent
                                        else -> Color(0xFF2C3056)
                                    },
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    selectedUsdAmount = amount
                                }
                                .padding(vertical = 10.dp)
                                .testTag("amount_$amount"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$${amount.toInt()}",
                                    color = if (affordable) Color.White else Color(0xFF64748B),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${pts} pts",
                                    color = if (affordable) GoldAccent else Color(0xFF64748B),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Destination Account input
                Text(
                    text = "3. Payout Destination",
                    color = Color(0xFFCBD5E1),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = destinationInput,
                    onValueChange = {
                        destinationInput = it
                        inputError = null
                    },
                    placeholder = {
                        Text(
                            text = selectedMethod.hint,
                            color = Color(0xFF64748B),
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("destination_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = EmeraldPrimary,
                        unfocusedBorderColor = Color(0xFF2C3056),
                        focusedContainerColor = Color(0xFF1E213D),
                        unfocusedContainerColor = Color(0xFF1E213D)
                    ),
                    singleLine = true
                )

                if (inputError != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = inputError ?: "",
                        color = RoseError,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Security & Audit Guarantee Note
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF0F172A))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Safe",
                        tint = CyanAccent,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Anti-Fraud Audited: Fast automated disbursement within 24 hours. Rate: 1,000 pts = $1.00 USD.",
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (destinationInput.trim().length < 4) {
                            inputError = "Please enter a valid payout account or address."
                            return@Button
                        }
                        if (!hasEnoughPoints) {
                            inputError = "Insufficient points! You need $requiredPoints pts."
                            return@Button
                        }
                        onSubmitWithdrawal(
                            selectedMethod.name,
                            destinationInput.trim(),
                            requiredPoints,
                            selectedUsdAmount
                        )
                    },
                    enabled = hasEnoughPoints,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldPrimary,
                        disabledContainerColor = Color(0xFF2C3056),
                        contentColor = Color.Black,
                        disabledContentColor = Color(0xFF64748B)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("confirm_withdrawal_button")
                ) {
                    Text(
                        text = if (hasEnoughPoints) "Withdraw $${String.format("%.2f", selectedUsdAmount)} ($requiredPoints Pts)" else "Need ${requiredPoints - profile.balancePoints} More Points",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
