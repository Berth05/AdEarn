package com.example.data.repository

import com.example.data.local.RewardDao
import com.example.data.model.RewardTransaction
import com.example.data.model.UserProfile
import com.example.data.model.WithdrawalRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.util.UUID

class RewardRepository(private val rewardDao: RewardDao) {

    val userProfile: Flow<UserProfile> = rewardDao.getUserProfile().map { profile ->
        profile ?: UserProfile()
    }

    val transactions: Flow<List<RewardTransaction>> = rewardDao.getAllTransactions()
    val withdrawals: Flow<List<WithdrawalRequest>> = rewardDao.getAllWithdrawals()

    suspend fun ensureProfileExists() {
        val current = rewardDao.getUserProfile().firstOrNull()
        if (current == null) {
            rewardDao.insertOrUpdateProfile(UserProfile())
        }
    }

    suspend fun awardAdReward(
        adTitle: String,
        basePoints: Long,
        network: String,
        ssvSignature: String
    ): Long {
        val current = rewardDao.getUserProfile().firstOrNull() ?: UserProfile()
        val multiplier = current.streakMultiplier
        val actualEarned = (basePoints * multiplier).toLong()

        val updated = current.copy(
            balancePoints = current.balancePoints + actualEarned,
            lifetimeEarnedPoints = current.lifetimeEarnedPoints + actualEarned,
            adsWatchedToday = current.adsWatchedToday + 1
        )
        rewardDao.insertOrUpdateProfile(updated)

        val tx = RewardTransaction(
            title = "$adTitle (${network})",
            points = actualEarned,
            type = "AD_REWARD",
            networkProvider = network,
            ssvToken = ssvSignature,
            timestamp = System.currentTimeMillis(),
            status = "VERIFIED"
        )
        rewardDao.insertTransaction(tx)
        return actualEarned
    }

    suspend fun claimBonus(title: String, points: Long, type: String = "DAILY_BONUS") {
        val current = rewardDao.getUserProfile().firstOrNull() ?: UserProfile()
        val updated = current.copy(
            balancePoints = current.balancePoints + points,
            lifetimeEarnedPoints = current.lifetimeEarnedPoints + points
        )
        rewardDao.insertOrUpdateProfile(updated)

        val tx = RewardTransaction(
            title = title,
            points = points,
            type = type,
            networkProvider = "System",
            ssvToken = "sys_claim_" + UUID.randomUUID().toString().take(8),
            timestamp = System.currentTimeMillis(),
            status = "COMPLETED"
        )
        rewardDao.insertTransaction(tx)
    }

    suspend fun requestWithdrawal(
        method: String,
        destination: String,
        points: Long,
        usd: Double
    ): Result<WithdrawalRequest> {
        val current = rewardDao.getUserProfile().firstOrNull() ?: UserProfile()
        if (current.balancePoints < points) {
            return Result.failure(Exception("Insufficient balance points!"))
        }

        val updated = current.copy(
            balancePoints = current.balancePoints - points,
            lifetimeWithdrawnPoints = current.lifetimeWithdrawnPoints + points
        )
        rewardDao.insertOrUpdateProfile(updated)

        val refId = "PAY-" + System.currentTimeMillis().toString().takeLast(6) + "-" + method.take(2).uppercase()
        val withdrawal = WithdrawalRequest(
            method = method,
            accountDestination = destination,
            pointsDeducted = points,
            amountUsd = usd,
            status = "IN_REVIEW",
            referenceId = refId,
            requestTimestamp = System.currentTimeMillis(),
            estimatedPayout = "Audited & processed within 24h"
        )
        rewardDao.insertWithdrawal(withdrawal)

        // Also add debit record in transaction ledger
        val tx = RewardTransaction(
            title = "Cashout Request: $method ($${String.format("%.2f", usd)})",
            points = -points,
            type = "WITHDRAWAL",
            networkProvider = method,
            ssvToken = "payout_req_$refId",
            timestamp = System.currentTimeMillis(),
            status = "PROCESSING"
        )
        rewardDao.insertTransaction(tx)

        return Result.success(withdrawal)
    }

    suspend fun resetDemoData() {
        rewardDao.clearTransactions()
        rewardDao.clearWithdrawals()
        rewardDao.insertOrUpdateProfile(
            UserProfile(
                balancePoints = 1500L,
                lifetimeEarnedPoints = 1500L,
                adsWatchedToday = 0,
                currentStreakDays = 1
            )
        )
    }
}
