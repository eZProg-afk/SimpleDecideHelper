package spiral.bit.dev.simpledecidehelper.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import spiral.bit.dev.simpledecidehelper.models.CompletedDecision
import spiral.bit.dev.simpledecidehelper.models.CompletedProsConsItem
import spiral.bit.dev.simpledecidehelper.models.Decision
import spiral.bit.dev.simpledecidehelper.models.ProsConsItem
import spiral.bit.dev.simpledecidehelper.repo.MainRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val mainRepo: MainRepository,
) : ViewModel() {

    val allDecisions = mainRepo.getAllDecisions()
    val allCompletedDecisions = mainRepo.getAllCompletedDecisions()
    lateinit var allComplProsCons: LiveData<List<CompletedProsConsItem>>
    lateinit var allProsCons: LiveData<List<ProsConsItem>>

    fun insertDecision(decision: Decision) = viewModelScope.launch {
        mainRepo.insertDecision(decision)
    }

    fun deleteDecision(decision: Decision) = viewModelScope.launch {
        mainRepo.deleteDecision(decision)
    }

    fun updateDecision(decision: Decision) = viewModelScope.launch {
        mainRepo.updateDecision(decision)
    }

    fun deleteAllCompletedDecisions() = CoroutineScope(Dispatchers.IO).launch {
        mainRepo.deleteAllCompletedDecisions()
    }

    fun insertCompletedDecision(completedDecision: CompletedDecision) = viewModelScope.launch {
        mainRepo.insertCompletedDecision(completedDecision)
    }

    fun deleteCompletedDecision(completedDecision: CompletedDecision) = viewModelScope.launch {
        mainRepo.deleteCompletedDecision(completedDecision)
    }

    //ProsCons

    fun insertProsConsItem(prosConsItem: ProsConsItem) = viewModelScope.launch {
        mainRepo.insertProsConsItem(prosConsItem)
    }

    fun deleteProsConsItem(prosConsItem: ProsConsItem) = viewModelScope.launch {
        mainRepo.deleteProsConsItem(prosConsItem)
    }

    fun deleteAllProsConsByID(parentID: Int) = viewModelScope.launch {
        mainRepo.deleteAllProsConsByID(parentID)
    }

    fun insertComplProsConsItem(completedProsConsItem: CompletedProsConsItem) =
        viewModelScope.launch {
            mainRepo.insertComplProsConsItem(completedProsConsItem)
        }

    fun deleteComplProsConsItem(completedProsConsItem: CompletedProsConsItem) =
        viewModelScope.launch {
            mainRepo.deleteComplProsConsItem(completedProsConsItem)
        }

    fun deleteAllComplProsConsByID(parentID: Int) = viewModelScope.launch {
        mainRepo.deleteAllComplProsConsByID(parentID)
    }

    fun setParentId(parentId: Int) {
        allProsCons = mainRepo.getAllProsConsItems(parentId)
        allComplProsCons = mainRepo.getAllComplProsConsItems(parentId)
    }
}