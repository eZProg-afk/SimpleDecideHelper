package spiral.bit.dev.simpledecidehelper.data

import androidx.lifecycle.LiveData
import androidx.room.*
import spiral.bit.dev.simpledecidehelper.models.Decision

@Dao
interface DecisionsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDecision(decision: Decision)

    @Delete
    suspend fun deleteDecision(decision: Decision)

    @Update
    suspend fun updateDecision(decision: Decision)

    @Query("SELECT * FROM decisions_table")
    fun getAllDecisions(): LiveData<List<Decision>>
}