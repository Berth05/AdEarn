package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.AdCampaign
import com.example.ui.MainViewModel
import com.example.ui.components.AdVideoPlayerDialog
import com.example.ui.components.CaptchaVerificationDialog
import com.example.ui.components.PublisherConsoleBottomSheet
import com.example.ui.components.WithdrawalModal
import com.example.ui.screens.EarnDashboardScreen
import com.example.ui.screens.OfferwallScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.TransactionsScreen
import com.example.ui.screens.WithdrawScreen
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.flow.collectLatest

enum class NavigationScreen(val label: String, val icon: ImageVector) {
    EARN("Earn", Icons.Default.MonetizationOn),
    OFFERS("Offers", Icons.Default.Assignment),
    WITHDRAW("Cashout", Icons.Default.AccountBalanceWallet),
    LEDGER("Ledger", Icons.Default.ReceiptLong),
    PROFILE("Profile", Icons.Default.Person)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppScreen()
            }
        }
    }
}

@Composable
fun MainAppScreen(
    viewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val withdrawals by viewModel.withdrawals.collectAsStateWithLifecycle()
    val quests by viewModel.quests.collectAsStateWithLifecycle()
    val cooldownSeconds by viewModel.cooldownSeconds.collectAsStateWithLifecycle()

    var currentScreen by remember { mutableStateOf(NavigationScreen.EARN) }
    var activeAdCampaign by remember { mutableStateOf<AdCampaign?>(null) }
    var showWithdrawalModal by remember { mutableStateOf(false) }
    var showCaptchaDialog by remember { mutableStateOf(false) }
    var showPublisherConsole by remember { mutableStateOf(false) }

    // Listen to ViewModel toast/snackbar events
    LaunchedEffect(viewModel) {
        viewModel.eventMessage.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            snackbarHostState.showSnackbar(message)
        }
    }

    // Handle back button: return to EARN screen before exiting app
    BackHandler(enabled = currentScreen != NavigationScreen.EARN) {
        currentScreen = NavigationScreen.EARN
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .navigationBarsPadding()
                    .testTag("bottom_nav_bar"),
                containerColor = Color(0xFF101222),
                contentColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationScreen.values().forEach { screen ->
                    val isSelected = currentScreen == screen
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentScreen = screen },
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.label
                            )
                        },
                        label = {
                            Text(
                                text = screen.label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = EmeraldPrimary,
                            indicatorColor = EmeraldPrimary,
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        ),
                        modifier = Modifier.testTag("nav_tab_${screen.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                NavigationScreen.EARN -> EarnDashboardScreen(
                    profile = profile,
                    quests = quests,
                    cooldownSecondsRemaining = cooldownSeconds,
                    onWatchAdClicked = { ad -> activeAdCampaign = ad },
                    onOpenWithdrawal = { showWithdrawalModal = true },
                    onOpenPublisherConsole = { showPublisherConsole = true },
                    onOpenCaptchaTest = { showCaptchaDialog = true },
                    onClaimDailyBonus = { viewModel.claimDailyBonus() }
                )

                NavigationScreen.OFFERS -> OfferwallScreen(
                    onCompleteTask = { title, points ->
                        viewModel.completeOfferwallTask(title, points)
                    }
                )

                NavigationScreen.WITHDRAW -> WithdrawScreen(
                    profile = profile,
                    withdrawals = withdrawals,
                    onRequestWithdrawalClicked = { showWithdrawalModal = true }
                )

                NavigationScreen.LEDGER -> TransactionsScreen(
                    transactions = transactions
                )

                NavigationScreen.PROFILE -> ProfileScreen(
                    profile = profile,
                    onSimulateReferral = { viewModel.simulateReferral() },
                    onOpenPublisherConsole = { showPublisherConsole = true },
                    onResetDemoData = { viewModel.resetDemoData() }
                )
            }
        }
    }

    // Rewarded Video Ad Player Dialog
    if (activeAdCampaign != null) {
        AdVideoPlayerDialog(
            ad = activeAdCampaign!!,
            onRewardEarned = { points, ssvSignature ->
                viewModel.onAdCompleted(activeAdCampaign!!, ssvSignature)
                activeAdCampaign = null
            },
            onDismiss = {
                activeAdCampaign = null
            }
        )
    }

    // Cashout / Withdrawal Dialog
    if (showWithdrawalModal) {
        WithdrawalModal(
            profile = profile,
            onSubmitWithdrawal = { method, destination, points, usd ->
                viewModel.submitWithdrawal(method, destination, points, usd)
                showWithdrawalModal = false
            },
            onDismiss = {
                showWithdrawalModal = false
            }
        )
    }

    // Anti-Fraud Captcha Verification Dialog
    if (showCaptchaDialog) {
        CaptchaVerificationDialog(
            onVerificationSuccess = { bonus ->
                viewModel.claimCaptchaBonus(bonus)
                showCaptchaDialog = false
            },
            onDismiss = {
                showCaptchaDialog = false
            }
        )
    }

    // Publisher & Ad Network Inspector Console
    if (showPublisherConsole) {
        PublisherConsoleBottomSheet(
            onAddDemoPoints = { pts ->
                viewModel.addDemoPoints(pts)
            },
            onResetDemoData = {
                viewModel.resetDemoData()
            },
            onDismiss = {
                showPublisherConsole = false
            }
        )
    }
}
