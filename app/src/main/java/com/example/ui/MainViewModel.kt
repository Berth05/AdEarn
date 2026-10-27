package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.AdCampaign
import com.example.data.model.DailyQuest
import com.example.data.model.RewardTransaction
import com.example.data.model.UserProfile
import com.example.data.model.WithdrawalRequest
import com.example.data.repository.RewardRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RewardRepository

    val userProfile: StateFlow<UserProfile>
    val transactions: StateFlow<List<RewardTransaction>>
    val withdrawals: StateFlow<List<WithdrawalRequest>>

    val cooldownSeconds = MutableStateFlow(0)
    private var cooldownJob: Job? = null

    private val _eventMessage = MutableSharedFlow<String>()
    val eventMessage: SharedFlow<String> = _eventMessage.asSharedFlow()

    val quests = MutableStateFlow(
        listOf(
            DailyQuest("q1", "Daily Ad Explorer", "Watch 5 ads to unlock loyalty bonus", 5, 5, 50, true),
            DailyQuest("q2", "Ad Power User", "Watch 15 ads today for +150 Pts", 8, 15, 150, false),
            DailyQuest("q3", "Offerwall Pioneer", "Complete 1 offerwall task", 0, 1, 100, false),
            DailyQuest("q4", "Streak Champion", "Maintain 4-day daily login streak", 4, 4, 80, true)
        )
    )

    init {
        val db = AppDatabase.getInstance(application)
        repository = RewardRepository(db.rewardDao())

        userProfile = repository.userProfile.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfile()
        )

        transactions = repository.transactions.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        withdrawals = repository.withdrawals.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        viewModelScope.launch {
            repository.ensureProfileExists()
        }
    }

    fun onAdCompleted(ad: AdCampaign, ssvSignature: String) {
        viewModelScope.launch {
            val awarded = repository.awardAdReward(
                adTitle = ad.title,
                basePoints = ad.baseRewardPoints,
                network = ad.network,
                ssvSignature = ssvSignature
            )
            _eventMessage.emit("+$awarded Points credited via ${ad.network} SSV verification!")

            // Start 20s cooldown between ads
            startCooldown(20)
        }
    }

    private fun startCooldown(seconds: Int) {
        cooldownJob?.cancel()
        cooldownJob = viewModelScope.launch {
            var current = seconds
            while (current > 0) {
                cooldownSeconds.value = current
                delay(1000)
                current -= 1
            }
            cooldownSeconds.value = 0
        }
    }

    fun submitWithdrawal(method: String, destination: String, points: Long, usd: Double) {
        viewModelScope.launch {
            val result = repository.requestWithdrawal(method, destination, points, usd)
            if (result.isSuccess) {
                _eventMessage.emit("Withdrawal of $${String.format("%.2f", usd)} submitted! Audit in progress.")
            } else {
                _eventMessage.emit(result.exceptionOrNull()?.message ?: "Withdrawal error")
            }
        }
    }

    fun completeOfferwallTask(title: String, points: Long) {
        viewModelScope.launch {
            repository.claimBonus(title, points, type = "OFFERWALL")
            _eventMessage.emit("+$points Points credited for completing $title!")
        }
    }

    fun claimCaptchaBonus(bonusPoints: Long) {
        viewModelScope.launch {
            repository.claimBonus("Human Integrity Check Verified", bonusPoints, type = "DAILY_BONUS")
            _eventMessage.emit("+$bonusPoints Points awarded for Anti-Fraud verification!")
        }
    }

    fun claimDailyBonus() {
        viewModelScope.launch {
            repository.claimBonus("Daily Check-in Bonus", 50, type = "DAILY_BONUS")
            _eventMessage.emit("+50 Points Daily Check-in Bonus claimed!")
        }
    }

    fun simulateReferral() {
        viewModelScope.launch {
            repository.claimBonus("Referral Friend Bonus (Invited: Jordan)", 100, type = "REFERRAL")
            _eventMessage.emit("+100 Points referral signup bonus credited!")
        }
    }

    fun addDemoPoints(amount: Long) {
        viewModelScope.launch {
            repository.claimBonus("Evaluator Test Credits", amount, type = "DAILY_BONUS")
            _eventMessage.emit("+$amount Test Points added for instant withdrawal testing!")
        }
    }

    fun resetDemoData() {
        viewModelScope.launch {
            repository.resetDemoData()
            _eventMessage.emit("Demo data successfully reset to clean state.")
        }
    }
}
