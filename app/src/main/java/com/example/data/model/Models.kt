package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val username: String = "AdEarner_Pro",
    val balancePoints: Long = 1850L,
    val lifetimeEarnedPoints: Long = 6350L,
    val lifetimeWithdrawnPoints: Long = 4500L,
    val adsWatchedToday: Int = 8,
    val dailyAdLimit: Int = 30,
    val currentStreakDays: Int = 4,
    val lastActiveDate: String = "2026-10-27",
    val referralCode: String = "ADEARN99X",
    val referredFriends: Int = 3,
    val referralEarnings: Long = 420L,
    val fraudTrustScore: Int = 98, // 0 - 100
    val isVpnClean: Boolean = true,
    val deviceFingerprint: String = "DEV-7A8B-SECURE"
) {
    // 1,000 points = $1.00 USD
    val balanceUsd: Double
        get() = balancePoints / 1000.0

    val lifetimeEarnedUsd: Double
        get() = lifetimeEarnedPoints / 1000.0

    val remainingAdsToday: Int
        get() = (dailyAdLimit - adsWatchedToday).coerceAtLeast(0)

    val streakMultiplier: Double
        get() = when {
            currentStreakDays >= 7 -> 1.25 // +25%
            currentStreakDays >= 5 -> 1.15 // +15%
            currentStreakDays >= 3 -> 1.10 // +10%
            else -> 1.0
        }

    val tierName: String
        get() = when {
            lifetimeEarnedPoints >= 10000 -> "Diamond"
            lifetimeEarnedPoints >= 5000 -> "Gold"
            lifetimeEarnedPoints >= 2000 -> "Silver"
            else -> "Bronze"
        }
}

@Entity(tableName = "transactions")
data class RewardTransaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val points: Long,
    val type: String, // AD_REWARD, DAILY_BONUS, OFFERWALL, REFERRAL, WITHDRAWAL
    val networkProvider: String, // Google AdMob, Unity Ads, AppLovin MAX, System
    val ssvToken: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "VERIFIED" // VERIFIED, COMPLETED, PROCESSING
)

@Entity(tableName = "withdrawals")
data class WithdrawalRequest(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val method: String, // PayPal, GCash, USDT Crypto, Amazon Gift Card, Bank Wire
    val accountDestination: String,
    val pointsDeducted: Long,
    val amountUsd: Double,
    val feePoints: Long = 0,
    val status: String = "IN_REVIEW", // IN_REVIEW, PROCESSING, PAID, REJECTED
    val referenceId: String,
    val requestTimestamp: Long = System.currentTimeMillis(),
    val estimatedPayout: String = "Within 24-48 Hours"
)

data class AdCampaign(
    val id: String,
    val title: String,
    val sponsorName: String,
    val description: String,
    val durationSeconds: Int,
    val baseRewardPoints: Long,
    val network: String, // AdMob, Unity, AppLovin
    val adType: String, // HIGH_REWARD, QUICK_VIDEO, INTERACTIVE
    val tag: String,
    val brandColorHex: Long = 0xFF6366F1
)

data class OfferwallTask(
    val id: String,
    val title: String,
    val provider: String,
    val pointsReward: Long,
    val estimatedMinutes: Int,
    val category: String, // Install & Try, Fast Survey, Web Sponsor, Daily Quiz
    val instructions: String,
    val isCompleted: Boolean = false
)

data class DailyQuest(
    val id: String,
    val title: String,
    val description: String,
    val currentProgress: Int,
    val targetProgress: Int,
    val rewardPoints: Long,
    val isClaimed: Boolean = false
)
