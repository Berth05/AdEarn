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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OfferwallTask
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.IndigoAccent

@Composable
fun OfferwallScreen(
    onCompleteTask: (title: String, points: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val allTasks = listOf(
        OfferwallTask(
            id = "task_survey_tech",
            title = "Fast Consumer Tech Opinion Survey",
            provider = "PollFish Research",
            pointsReward = 220,
            estimatedMinutes = 2,
            category = "Surveys",
            instructions = "Answer 5 brief multiple choice questions regarding your mobile gadget preferences."
        ),
        OfferwallTask(
            id = "task_app_crypto",
            title = "CryptoPulse Wallet: Install & Open",
            provider = "Tapjoy Offers",
            pointsReward = 350,
            estimatedMinutes = 1,
            category = "App Tests",
            instructions = "Download the demo app package, launch it once and explore the home screen for 30 seconds."
        ),
        OfferwallTask(
            id = "task_quiz_financial",
            title = "Daily Financial Literacy IQ Quiz",
            provider = "AdGem Sponsor",
            pointsReward = 180,
            estimatedMinutes = 2,
            category = "Quiz",
            instructions = "Score 80%+ on the 4-question budget and investment quiz to unlock points."
        ),
        OfferwallTask(
            id = "task_web_visit",
            title = "Browse Sponsor News Portal for 45s",
            provider = "RevContent Web",
            pointsReward = 120,
            estimatedMinutes = 1,
            category = "Web Visits",
            instructions = "Visit the sponsor's tech articles hub and scroll to the bottom of the article."
        ),
        OfferwallTask(
            id = "task_app_game",
            title = "Medieval Legends: Reach Level 3",
            provider = "IronSource Studio",
            pointsReward = 500,
            estimatedMinutes = 4,
            category = "App Tests",
            instructions = "Complete the game tutorial and defeat the first dungeon boss."
        )
    )

    var selectedCategory by remember { mutableStateOf("All") }
    var activeTaskForDialog by remember { mutableStateOf<OfferwallTask?>(null) }
    var completedTaskIds by remember { mutableStateOf(setOf<String>()) }

    val categories = listOf("All", "App Tests", "Surveys", "Quiz", "Web Visits")
    val filteredTasks = allTasks.filter {
        selectedCategory == "All" || it.category == selectedCategory
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("offerwall_screen"),
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
                        text = "High-Yield Offerwall",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Complete sponsor tasks to earn 100 - 500 Pts each",
                        color = Color(0xFF94A3B8),
                        fontSize = 13.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(GoldAccent.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = GoldAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Up to 500 Pts",
                            color = GoldAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
                        label = {
                            Text(
                                text = category,
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
        }

        items(filteredTasks) { task ->
            val isCompleted = completedTaskIds.contains(task.id)

            val icon: ImageVector = when (task.category) {
                "Surveys" -> Icons.Default.Assignment
                "App Tests" -> Icons.Default.Download
                "Quiz" -> Icons.Default.Quiz
                else -> Icons.Default.Language
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF16182E))
                    .border(1.dp, Color(0xFF2C3056), RoundedCornerShape(16.dp))
                    .clickable(enabled = !isCompleted) {
                        activeTaskForDialog = task
                    }
                    .padding(16.dp)
                    .testTag("task_card_${task.id}")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    when (task.category) {
                                        "App Tests" -> IndigoAccent.copy(alpha = 0.2f)
                                        "Surveys" -> CyanAccent.copy(alpha = 0.2f)
                                        "Quiz" -> GoldAccent.copy(alpha = 0.2f)
                                        else -> EmeraldPrimary.copy(alpha = 0.2f)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = when (task.category) {
                                    "App Tests" -> IndigoAccent
                                    "Surveys" -> CyanAccent
                                    "Quiz" -> GoldAccent
                                    else -> EmeraldPrimary
                                },
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = task.title,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${task.provider} • ~${task.estimatedMinutes} min",
                                color = Color(0xFF94A3B8),
                                fontSize = 12.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isCompleted) Color(0xFF2C3056) else EmeraldPrimary)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isCompleted) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Done",
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Done",
                                    color = Color(0xFFCBD5E1),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Text(
                                    text = "+${task.pointsReward} Pts",
                                    color = Color.Black,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Task Detail & Simulation Dialog
    if (activeTaskForDialog != null) {
        val task = activeTaskForDialog!!
        AlertDialog(
            onDismissRequest = { activeTaskForDialog = null },
            title = {
                Text(
                    text = task.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Provider: ${task.provider}", color = CyanAccent, fontSize = 12.sp)
                        Text("+${task.pointsReward} Points", color = GoldAccent, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Instructions:",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = task.instructions,
                        color = Color(0xFFCBD5E1),
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Estimated Time: ${task.estimatedMinutes} minute(s). Points credited instantly upon completion.",
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        completedTaskIds = completedTaskIds + task.id
                        onCompleteTask("Offerwall: ${task.title}", task.pointsReward)
                        activeTaskForDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("Complete & Claim", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Button(
                    onClick = { activeTaskForDialog = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C3056))
                ) {
                    Text("Cancel", color = Color.White)
                }
            },
            containerColor = Color(0xFF16182E),
            shape = RoundedCornerShape(16.dp)
        )
    }
}
