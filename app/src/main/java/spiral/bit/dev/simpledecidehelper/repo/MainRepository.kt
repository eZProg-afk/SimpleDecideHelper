package spiral.bit.dev.simpledecidehelper.repo

import androidx.lifecycle.LiveData
import spiral.bit.dev.simpledecidehelper.data.DecisionsDAO
import spiral.bit.dev.simpledecidehelper.data.ProsConsDao
import spiral.bit.dev.simpledecidehelper.models.Decision
import spiral.bit.dev.simpledecidehelper.models.ProsConsItem
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val decisionsDAO: DecisionsDAO,
    private val prosConsDao: ProsConsDao
) {

    //Decisions

    suspend fun insertDecision(decision: Decision) = decisionsDAO.insertDecision(decision)

    suspend fun deleteDecision(decision: Decision) = decisionsDAO.deleteDecision(decision)

    suspend fun updateDecision(decision: Decision) = decisionsDAO.updateDecision(decision)

    fun getAllDecisions(): LiveData<List<Decision>> = decisionsDAO.getAllDecisions()


    //ProsCons

    suspend fun insertProsConsItem(prosConsItem: ProsConsItem) =
        prosConsDao.insertProsConsItem(prosConsItem)

    suspend fun deleteProsConsItem(prosConsItem: ProsConsItem) =
        prosConsDao.deleteProsConsItem(prosConsItem)

    suspend fun deleteAllProsConsByID(parentID: Int) = prosConsDao.deleteAllProsConsById(parentID)

    fun getAllProsConsItems(parentID: Int): LiveData<List<ProsConsItem>> = prosConsDao.getAllProsConsItems(parentID)

}