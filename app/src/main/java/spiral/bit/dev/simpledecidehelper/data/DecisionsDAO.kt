package spiral.bit.dev.simpledecidehelper.data

import androidx.lifecycle.LiveData
import androidx.room.*
import spiral.bit.dev.simpledecidehelper.models.CompletedDecision
import spiral.bit.dev.simpledecidehelper.models.Decision

@Dao
interface DecisionsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDecision(decision: Decision)

    @Delete
    suspend fun deleteDecision(decision: Decision)

    @Query("DELETE FROM completed_decisions_table")
    fun deleteAllCompletedDecisions()

    @Update
    suspend fun updateDecision(decision: Decision)

    @Query("SELECT * FROM decisions_table")
    fun getAllDecisions(): LiveData<List<Decision>>

    @Insert
    suspend fun insertCompletedDecision(completedDecision: CompletedDecision)

    @Delete
    suspend fun deleteCompletedDecision(completedDecision: CompletedDecision)

    @Update
    suspend fun updateCompletedDecision(completedDecision: CompletedDecision)

    @Query("SELECT * FROM completed_decisions_table")
    fun getAllCompletedDecisions(): LiveData<List<CompletedDecision>>
}