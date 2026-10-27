package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.data.model.WithdrawalRequest
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.RoseError
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WithdrawScreen(
    profile: UserProfile,
    withdrawals: List<WithdrawalRequest>,
    onRequestWithdrawalClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedWithdrawalForReceipt by remember { mutableStateOf<WithdrawalRequest?>(null) }

    val minPointsThreshold = 2000L
    val progressToMin = (profile.balancePoints.toFloat() / minPointsThreshold).coerceIn(0f, 1f)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("withdraw_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp)
    ) {
        // Header
        item {
            Text(
                text = "Payouts & Cashout",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Convert your points to real money via PayPal, GCash, or Crypto",
                color = Color(0xFF94A3B8),
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Balance & Progress to Min Payout
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF16182E))
                    .border(1.dp, Color(0xFF2C3056), RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Available For Cashout",
                                color = Color(0xFF94A3B8),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$${String.format("%.2f", profile.balanceUsd)} USD",
                                color = EmeraldLight,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "${profile.balancePoints} reward points",
                                color = Color(0xFFCBD5E1),
                                fontSize = 13.sp
                            )
                        }

                        Button(
                            onClick = onRequestWithdrawalClicked,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EmeraldPrimary,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("request_payout_button")
                        ) {
                            Text("Cash Out", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress to Minimum Cashout ($2.00)
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Minimum Cashout ($2.00)",
                                color = Color(0xFFCBD5E1),
                                fontSize = 12.sp
                            )
                            Text(
                                text = if (profile.balancePoints >= minPointsThreshold) "Threshold Reached!" else "${profile.balancePoints} / $minPointsThreshold pts",
                                color = if (profile.balancePoints >= minPointsThreshold) EmeraldPrimary else GoldAccent,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { progressToMin },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = EmeraldPrimary,
                            trackColor = Color(0xFF2C3056)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Supported Payout Channels Banner
            Text(
                text = "Supported Payout Channels",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val methods = listOf(
                    Triple("PayPal", Icons.Default.Payment, "Min $2"),
                    Triple("GCash", Icons.Default.PhoneAndroid, "Min $2"),
                    Triple("USDT", Icons.Default.CurrencyBitcoin, "Min $5"),
                    Triple("Amazon", Icons.Default.CardGiftcard, "Min $5")
                )

                methods.forEach { (name, icon, min) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1E213D))
                            .border(1.dp, Color(0xFF2C3056), RoundedCornerShape(12.dp))
                            .padding(vertical = 12.dp, horizontal = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = icon,
                                contentDescription = name,
                                tint = CyanAccent,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = name,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = min,
                                color = Color(0xFF94A3B8),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Cashout History Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cashout History & Receipts",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${withdrawals.size} records",
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        if (withdrawals.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF16182E))
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No withdrawal requests yet",
                            color = Color(0xFF94A3B8),
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Watch ads to reach 2,000 points and cash out!",
                            color = Color(0xFF64748B),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        } else {
            items(withdrawals) { item ->
                val dateStr = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(Date(item.requestTimestamp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF16182E))
                        .border(1.dp, Color(0xFF2C3056), RoundedCornerShape(14.dp))
                        .clickable { selectedWithdrawalForReceipt = item }
                        .padding(14.dp)
                        .testTag("withdrawal_item_${item.id}")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (item.status) {
                                            "PAID" -> EmeraldPrimary.copy(alpha = 0.2f)
                                            "IN_REVIEW" -> GoldAccent.copy(alpha = 0.2f)
                                            else -> IndigoAccent.copy(alpha = 0.2f)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (item.status) {
                                        "PAID" -> Icons.Default.CheckCircle
                                        "IN_REVIEW" -> Icons.Default.HourglassEmpty
                                        else -> Icons.Default.ReceiptLong
                                    },
                                    contentDescription = null,
                                    tint = when (item.status) {
                                        "PAID" -> EmeraldPrimary
                                        "IN_REVIEW" -> GoldAccent
                                        else -> IndigoAccent
                                    },
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = "${item.method} Payout",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = item.accountDestination,
                                    color = Color(0xFF94A3B8),
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = dateStr,
                                    color = Color(0xFF64748B),
                                    fontSize = 10.sp
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "$${String.format("%.2f", item.amountUsd)} USD",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        when (item.status) {
                                            "PAID" -> EmeraldPrimary.copy(alpha = 0.15f)
                                            "IN_REVIEW" -> GoldAccent.copy(alpha = 0.15f)
                                            else -> IndigoAccent.copy(alpha = 0.15f)
                                        }
                                    )
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = item.status.replace("_", " "),
                                    color = when (item.status) {
                                        "PAID" -> EmeraldPrimary
                                        "IN_REVIEW" -> GoldAccent
                                        else -> IndigoAccent
                                    },
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Receipt Detail Modal
    if (selectedWithdrawalForReceipt != null) {
        val req = selectedWithdrawalForReceipt!!
        AlertDialog(
            onDismissRequest = { selectedWithdrawalForReceipt = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ReceiptLong,
                        contentDescription = null,
                        tint = CyanAccent
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Disbursement Receipt",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1E213D))
                            .padding(14.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Reference ID:", color = Color(0xFF94A3B8), fontSize = 12.sp)
                                Text(req.referenceId, color = Color.White, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Payout Channel:", color = Color(0xFF94A3B8), fontSize = 12.sp)
                                Text(req.method, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Destination:", color = Color(0xFF94A3B8), fontSize = 12.sp)
                                Text(req.accountDestination, color = Color(0xFFCBD5E1), fontSize = 12.sp)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Amount USD:", color = Color(0xFF94A3B8), fontSize = 12.sp)
                                Text("$${String.format("%.2f", req.amountUsd)}", color = EmeraldPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Points Deducted:", color = Color(0xFF94A3B8), fontSize = 12.sp)
                                Text("${req.pointsDeducted} pts", color = GoldAccent, fontSize = 12.sp)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Disbursement Status:", color = Color(0xFF94A3B8), fontSize = 12.sp)
                                Text(req.status, color = if (req.status == "PAID") EmeraldPrimary else GoldAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Anti-Fraud Audit: ${req.estimatedPayout}. All payout transactions are recorded permanently in the blockchain/ledger proof.",
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { selectedWithdrawalForReceipt = null },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("Close Receipt", color = Color.Black)
                }
            },
            containerColor = Color(0xFF16182E),
            shape = RoundedCornerShape(16.dp)
        )
    }
}
