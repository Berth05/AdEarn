package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.IndigoAccent

data class LeaderboardEntry(
    val rank: Int,
    val username: String,
    val points: Long,
    val adsWatched: Int,
    val badge: String
)

@Composable
fun LeaderboardScreen(
    profile: UserProfile,
    modifier: Modifier = Modifier
) {
    var selectedPeriod by remember { mutableStateOf("Today") }

    val todayLeaders = listOf(
        LeaderboardEntry(1, "ApexWatcher_99", 2850, 30, "Diamond"),
        LeaderboardEntry(2, "CashNinja_PH", 2420, 28, "Gold"),
        LeaderboardEntry(3, "Elena_Earns", 2150, 26, "Gold"),
        LeaderboardEntry(4, "AdHunter_X", 1920, 24, "Silver"),
        LeaderboardEntry(5, profile.username, profile.balancePoints, profile.adsWatchedToday, profile.tierName),
        LeaderboardEntry(6, "CryptoDave", 1680, 20, "Silver"),
        LeaderboardEntry(7, "LuckyStar_77", 1450, 18, "Bronze"),
        LeaderboardEntry(8, "VortexStream", 1320, 16, "Bronze"),
        LeaderboardEntry(9, "SammyG", 1150, 15, "Bronze"),
        LeaderboardEntry(10, "ZenithEarn", 980, 12, "Bronze")
    )

    val allTimeLeaders = listOf(
        LeaderboardEntry(1, "CryptoDave", 48500, 540, "Diamond"),
        LeaderboardEntry(2, "ApexWatcher_99", 42100, 480, "Diamond"),
        LeaderboardEntry(3, "Elena_Earns", 36400, 410, "Diamond"),
        LeaderboardEntry(4, "CashNinja_PH", 29800, 350, "Gold"),
        LeaderboardEntry(5, "AdHunter_X", 22100, 280, "Gold"),
        LeaderboardEntry(6, profile.username, profile.lifetimeEarnedPoints, 84, profile.tierName)
    )

    val currentList = if (selectedPeriod == "Today") todayLeaders else allTimeLeaders

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("leaderboard_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp)
    ) {
        item {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Earner Leaderboard",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Compete with top earners and win weekly pool bonus",
                        color = Color(0xFF94A3B8),
                        fontSize = 13.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(GoldAccent.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Trophy",
                        tint = GoldAccent,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Period Selector
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Today", "All Time").forEach { period ->
                    val isSelected = selectedPeriod == period
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedPeriod = period },
                        label = {
                            Text(
                                text = period,
                                color = if (isSelected) Color.Black else Color.White,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = EmeraldPrimary,
                            containerColor = Color(0xFF1E213D)
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = if (isSelected) EmeraldPrimary else Color(0xFF2C3056),
                            enabled = true,
                            selected = isSelected
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Weekly Pool Prize Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF1E213D))
                    .border(1.dp, GoldAccent.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = null,
                            tint = GoldAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "$150.00 Weekly Bonus Pool",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Distributed to Top 10 every Sunday 23:59 UTC",
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(GoldAccent)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "LIVE",
                            color = Color.Black,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        itemsIndexed(currentList) { index, item ->
            val isCurrentUser = item.username == profile.username

            val rankColor = when (item.rank) {
                1 -> GoldAccent
                2 -> Color(0xFFCBD5E1) // Silver
                3 -> Color(0xFFCD7F32) // Bronze
                else -> Color(0xFF64748B)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isCurrentUser) IndigoAccent.copy(alpha = 0.25f) else Color(0xFF16182E))
                    .border(
                        width = if (isCurrentUser) 1.5.dp else 1.dp,
                        color = if (isCurrentUser) IndigoAccent else Color(0xFF2C3056),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 12.dp)
                    .testTag("leaderboard_row_${item.rank}")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Rank Pill
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(rankColor.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "#${item.rank}",
                                color = rankColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = item.username,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = if (isCurrentUser) FontWeight.ExtraBold else FontWeight.SemiBold
                                )
                                if (isCurrentUser) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(EmeraldPrimary.copy(alpha = 0.2f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "YOU",
                                            color = EmeraldPrimary,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${item.adsWatched} ads watched • Tier: ${item.badge}",
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Points earned
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = GoldAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${item.points} pts",
                            color = GoldAccent,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
