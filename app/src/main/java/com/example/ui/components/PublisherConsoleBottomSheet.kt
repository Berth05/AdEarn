package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.IndigoAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublisherConsoleBottomSheet(
    onAddDemoPoints: (Long) -> Unit,
    onResetDemoData: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var ecpmSlider by remember { mutableFloatStateOf(4.50f) }
    var userSharePercent by remember { mutableFloatStateOf(60f) }

    val impressions = 1000f
    val grossRevenue = (ecpmSlider / 1000f) * impressions
    val userPayoutTotal = grossRevenue * (userSharePercent / 100f)
    val publisherProfit = grossRevenue - userPayoutTotal
    val perAdUserRewardCents = (grossRevenue / 1000f) * (userSharePercent / 100f) * 100f

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF121426),
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 36.dp)
                .verticalScroll(rememberScrollState())
                .testTag("publisher_console_sheet")
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(IndigoAccent.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = "Console",
                            tint = IndigoAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Publisher & Ad Network Inspector",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "eCPM Math, Server-Side Verification (SSV) & Testing",
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp
                        )
                    }
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

            // Section 1: Live eCPM Economics Calculator
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF1A1D36))
                    .border(1.dp, Color(0xFF2E335E), RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Calculate,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Business Model Economics",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "Per 1,000 Views",
                            color = CyanAccent,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "AdMob / Unity eCPM Rate: $${String.format("%.2f", ecpmSlider)} USD",
                        color = Color(0xFFCBD5E1),
                        fontSize = 12.sp
                    )
                    Slider(
                        value = ecpmSlider,
                        onValueChange = { ecpmSlider = it },
                        valueRange = 1.0f..15.0f,
                        colors = SliderDefaults.colors(
                            thumbColor = EmeraldPrimary,
                            activeTrackColor = EmeraldPrimary,
                            inactiveTrackColor = Color(0xFF2C3056)
                        )
                    )

                    Text(
                        text = "User Rev-Share Split: ${userSharePercent.toInt()}% Users / ${(100 - userSharePercent).toInt()}% Platform",
                        color = Color(0xFFCBD5E1),
                        fontSize = 12.sp
                    )
                    Slider(
                        value = userSharePercent,
                        onValueChange = { userSharePercent = it },
                        valueRange = 30f..80f,
                        colors = SliderDefaults.colors(
                            thumbColor = GoldAccent,
                            activeTrackColor = GoldAccent,
                            inactiveTrackColor = Color(0xFF2C3056)
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 3 Stats pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF131528))
                                .padding(8.dp)
                        ) {
                            Column {
                                Text("Gross Ad Revenue", color = Color(0xFF94A3B8), fontSize = 10.sp)
                                Text(
                                    "$${String.format("%.2f", grossRevenue)}",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF131528))
                                .padding(8.dp)
                        ) {
                            Column {
                                Text("User Cash Payouts", color = Color(0xFF94A3B8), fontSize = 10.sp)
                                Text(
                                    "$${String.format("%.2f", userPayoutTotal)}",
                                    color = GoldAccent,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF131528))
                                .padding(8.dp)
                        ) {
                            Column {
                                Text("Net Server Profit", color = Color(0xFF94A3B8), fontSize = 10.sp)
                                Text(
                                    "$${String.format("%.2f", publisherProfit)}",
                                    color = EmeraldPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Each completed ad rewards approx ${String.format("%.3f", perAdUserRewardCents)}¢ USD (${(perAdUserRewardCents * 10).toInt()} pts)",
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section 2: Server-Side Verification (SSV) Log Mock
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF090A14))
                    .border(1.dp, Color(0xFF272A4E), RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = CyanAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "SSV Server-to-Server Endpoint",
                                color = CyanAccent,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "STATUS 200 OK",
                            color = EmeraldPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = """POST /api/v1/admob/ssv_callback HTTP/1.1
Host: api.adearn.app
Content-Type: application/json

{
  "ad_network": "Google AdMob Rewarded",
  "ad_unit": "ca-app-pub-3940256099942544/5224354917",
  "user_id": "usr_99x_7a8b",
  "reward_amount": 55,
  "signature": "MEQCIDK38j4Vq_SSV_HASH_OK",
  "key_id": "394025609",
  "anti_fraud_vpn_status": "CLEAN"
}""",
                        color = Color(0xFFA5B4FC),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Quick Testing Actions for the Reviewer
            Text(
                text = "Developer & Evaluator Quick Actions",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { onAddDemoPoints(2000L) },
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoAccent),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("add_test_points_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("+2,000 Pts ($2)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onResetDemoData,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("reset_demo_data_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFFCBD5E1)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Reset Demo", color = Color(0xFFCBD5E1), fontSize = 12.sp)
                }
            }
        }
    }
}
