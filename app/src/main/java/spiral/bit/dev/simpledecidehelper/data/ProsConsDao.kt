package spiral.bit.dev.simpledecidehelper.data

import androidx.lifecycle.LiveData
import androidx.room.*
import spiral.bit.dev.simpledecidehelper.models.CompletedProsConsItem
import spiral.bit.dev.simpledecidehelper.models.ProsConsItem

@Dao
interface ProsConsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProsConsItem(prosConsItem: ProsConsItem)

    @Delete
    suspend fun deleteProsConsItem(prosConsItem: ProsConsItem)

    @Query("SELECT * FROM prof_cons_table WHERE decisionId == :parentID ORDER BY isProf DESC")
    fun getAllProsConsItems(parentID: Int): LiveData<List<ProsConsItem>>

    @Query("DELETE FROM prof_cons_table WHERE decisionId == :parentID")
    suspend fun deleteAllProsConsById(parentID: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplProsConsItem(completedProsConsItem: CompletedProsConsItem)

    @Delete
    suspend fun deleteComplProsConsItem(completedProsConsItem: CompletedProsConsItem)

    @Query("SELECT * FROM completed_prof_cons_table WHERE decisionId == :parentID ORDER BY isProf DESC")
    fun getAllComplProsConsItems(parentID: Int): LiveData<List<CompletedProsConsItem>>

    @Query("DELETE FROM completed_prof_cons_table WHERE decisionId == :parentID")
    suspend fun deleteAllComplProsConsById(parentID: Int)
}