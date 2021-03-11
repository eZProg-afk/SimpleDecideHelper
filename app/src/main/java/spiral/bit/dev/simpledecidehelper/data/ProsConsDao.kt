package spiral.bit.dev.simpledecidehelper.data

import androidx.lifecycle.LiveData
import androidx.room.*
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
}