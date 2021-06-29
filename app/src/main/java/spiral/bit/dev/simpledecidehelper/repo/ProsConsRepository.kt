package spiral.bit.dev.simpledecidehelper.repo

import androidx.lifecycle.LiveData
import spiral.bit.dev.simpledecidehelper.data.DecisionsDatabase
import spiral.bit.dev.simpledecidehelper.models.CompletedProsConsItem
import spiral.bit.dev.simpledecidehelper.models.ProsConsItem
import javax.inject.Inject

class ProsConsRepository @Inject constructor(
    private val database: DecisionsDatabase
) {

    suspend fun insertProsConsItem(prosConsItem: ProsConsItem) =
        database.getProsConsDao()
            .insertProsConsItem(prosConsItem)

    suspend fun deleteProsConsItem(prosConsItem: ProsConsItem) =
        database.getProsConsDao()
            .deleteProsConsItem(prosConsItem)

    suspend fun deleteAllProsConsByID(parentID: Int) = database.getProsConsDao()
        .deleteAllProsConsById(parentID)

    fun getAllProsConsItems(parentID: Int): LiveData<List<ProsConsItem>> =
        database.getProsConsDao()
            .getAllProsConsItems(parentID)

    suspend fun insertComplProsConsItem(completedProsConsItem: CompletedProsConsItem) =
        database.getProsConsDao()
            .insertComplProsConsItem(completedProsConsItem)

    suspend fun deleteComplProsConsItem(completedProsConsItem: CompletedProsConsItem) =
        database.getProsConsDao()
            .deleteComplProsConsItem(completedProsConsItem)

    suspend fun deleteAllComplProsConsByID(parentID: Int) =
        database.getProsConsDao()
            .deleteAllComplProsConsById(parentID)

    fun getAllComplProsConsItems(parentID: Int): LiveData<List<CompletedProsConsItem>> =
        database.getProsConsDao()
            .getAllComplProsConsItems(parentID)
}