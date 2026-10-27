package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.RewardTransaction
import com.example.data.model.UserProfile
import com.example.data.model.WithdrawalRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserProfile::class,
        RewardTransaction::class,
        WithdrawalRequest::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rewardDao(): RewardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ad_earn_database.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Populate default user profile and sample recent verified transactions
                            CoroutineScope(Dispatchers.IO).launch {
                                val dao = getInstance(context).rewardDao()
                                dao.insertOrUpdateProfile(UserProfile())
                                
                                // Seed some initial realistic transactions
                                val now = System.currentTimeMillis()
                                dao.insertTransaction(
                                    RewardTransaction(
                                        title = "AdMob Rewarded Video (30s)",
                                        points = 55,
                                        type = "AD_REWARD",
                                        networkProvider = "Google AdMob",
                                        ssvToken = "ssv_sig_admob_98a72b",
                                        timestamp = now - 3600_000 * 2,
                                        status = "VERIFIED"
                                    )
                                )
                                dao.insertTransaction(
                                    RewardTransaction(
                                        title = "Unity Playable Ad Bonus",
                                        points = 45,
                                        type = "AD_REWARD",
                                        networkProvider = "Unity Ads",
                                        ssvToken = "ssv_sig_unity_12ec84",
                                        timestamp = now - 3600_000 * 5,
                                        status = "VERIFIED"
                                    )
                                )
                                dao.insertTransaction(
                                    RewardTransaction(
                                        title = "Daily Streak Day 4 Multiplier",
                                        points = 100,
                                        type = "DAILY_BONUS",
                                        networkProvider = "System",
                                        ssvToken = "sys_bonus_streak_4",
                                        timestamp = now - 3600_000 * 8,
                                        status = "COMPLETED"
                                    )
                                )
                                dao.insertTransaction(
                                    RewardTransaction(
                                        title = "Referral Commission (Friend: Alex)",
                                        points = 120,
                                        type = "REFERRAL",
                                        networkProvider = "System",
                                        ssvToken = "ref_comm_alex_88",
                                        timestamp = now - 86400_000,
                                        status = "COMPLETED"
                                    )
                                )

                                // Seed a completed withdrawal proof
                                dao.insertWithdrawal(
                                    WithdrawalRequest(
                                        method = "PayPal",
                                        accountDestination = "user.cashout@gmail.com",
                                        pointsDeducted = 4000,
                                        amountUsd = 4.00,
                                        status = "PAID",
                                        referenceId = "PAY-2026-9812A",
                                        requestTimestamp = now - 86400_000 * 3,
                                        estimatedPayout = "Completed"
                                    )
                                )
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
