package spiral.bit.dev.simpledecidehelper.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import spiral.bit.dev.simpledecidehelper.data.DecisionsDatabase
import spiral.bit.dev.simpledecidehelper.models.CompletedDecision
import spiral.bit.dev.simpledecidehelper.models.Decision
import javax.inject.Inject

class DecisionRepository @Inject constructor(
    private val database: DecisionsDatabase
) {

    suspend fun insertDecision(decision: Decision) = database.getDecisionsDao()
        .insertDecision(decision)

    suspend fun deleteDecision(decision: Decision) = database.getDecisionsDao()
        .deleteDecision(decision)

    suspend fun deleteAllCompletedDecisions() = database.getDecisionsDao()
        .deleteAllCompletedDecisions()

    suspend fun updateDecision(decision: Decision) = database.getDecisionsDao()
        .updateDecision(decision)

    fun getAllDecisions(): LiveData<List<Decision>> = database.getDecisionsDao()
        .getAllDecisions()

    suspend fun insertCompletedDecision(completedDecision: CompletedDecision) =
        database.getDecisionsDao()
            .insertCompletedDecision(completedDecision)

    suspend fun deleteCompletedDecision(completedDecision: CompletedDecision) =
        database.getDecisionsDao()
            .deleteCompletedDecision(completedDecision)

    suspend fun updateCompletedDecision(completedDecision: CompletedDecision) =
        database.getDecisionsDao()
            .updateCompletedDecision(completedDecision)

    fun getAllCompletedDecisions(): LiveData<List<CompletedDecision>> =
        database.getDecisionsDao()
            .getAllCompletedDecisions()
}