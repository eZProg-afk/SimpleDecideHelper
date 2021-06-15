package spiral.bit.dev.simpledecidehelper.repo

import androidx.lifecycle.LiveData
import spiral.bit.dev.simpledecidehelper.data.DecisionsDAO
import spiral.bit.dev.simpledecidehelper.data.ProsConsDao
import spiral.bit.dev.simpledecidehelper.models.CompletedDecision
import spiral.bit.dev.simpledecidehelper.models.CompletedProsConsItem
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

    suspend fun deleteAllCompletedDecisions() = decisionsDAO.deleteAllCompletedDecisions()

    suspend fun updateDecision(decision: Decision) = decisionsDAO.updateDecision(decision)

    fun getAllDecisions(): LiveData<List<Decision>> = decisionsDAO.getAllDecisions()

    suspend fun insertCompletedDecision(completedDecision: CompletedDecision) =
        decisionsDAO.insertCompletedDecision(completedDecision)

    suspend fun deleteCompletedDecision(completedDecision: CompletedDecision) =
        decisionsDAO.deleteCompletedDecision(completedDecision)

    suspend fun updateCompletedDecision(completedDecision: CompletedDecision) =
        decisionsDAO.updateCompletedDecision(completedDecision)

    fun getAllCompletedDecisions(): LiveData<List<CompletedDecision>> =
        decisionsDAO.getAllCompletedDecisions()


    //ProsCons

    suspend fun insertProsConsItem(prosConsItem: ProsConsItem) =
        prosConsDao.insertProsConsItem(prosConsItem)

    suspend fun deleteProsConsItem(prosConsItem: ProsConsItem) =
        prosConsDao.deleteProsConsItem(prosConsItem)

    suspend fun deleteAllProsConsByID(parentID: Int) = prosConsDao.deleteAllProsConsById(parentID)

    fun getAllProsConsItems(parentID: Int): LiveData<List<ProsConsItem>> =
        prosConsDao.getAllProsConsItems(parentID)

    suspend fun insertComplProsConsItem(completedProsConsItem: CompletedProsConsItem) =
        prosConsDao.insertComplProsConsItem(completedProsConsItem)

    suspend fun deleteComplProsConsItem(completedProsConsItem: CompletedProsConsItem) =
        prosConsDao.deleteComplProsConsItem(completedProsConsItem)

    suspend fun deleteAllComplProsConsByID(parentID: Int) =
        prosConsDao.deleteAllComplProsConsById(parentID)

    fun getAllComplProsConsItems(parentID: Int): LiveData<List<CompletedProsConsItem>> =
        prosConsDao.getAllComplProsConsItems(parentID)
}