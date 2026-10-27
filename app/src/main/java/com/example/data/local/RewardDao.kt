package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.RewardTransaction
import com.example.data.model.UserProfile
import com.example.data.model.WithdrawalRequest
import kotlinx.coroutines.flow.Flow

@Dao
interface RewardDao {

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)

    @Update
    suspend fun updateProfile(profile: UserProfile)

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<RewardTransaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: RewardTransaction)

    @Query("SELECT * FROM withdrawals ORDER BY requestTimestamp DESC")
    fun getAllWithdrawals(): Flow<List<WithdrawalRequest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWithdrawal(request: WithdrawalRequest)

    @Query("DELETE FROM transactions")
    suspend fun clearTransactions()

    @Query("DELETE FROM withdrawals")
    suspend fun clearWithdrawals()
}
