package spiral.bit.dev.simpledecidehelper.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import spiral.bit.dev.simpledecidehelper.repo.MainRepository
import spiral.bit.dev.simpledecidehelper.models.Decision
import spiral.bit.dev.simpledecidehelper.models.ProsConsItem

class MainViewModel @ViewModelInject constructor(
    private val mainRepo: MainRepository,
    ): ViewModel() {

    lateinit var allProsCons: LiveData<List<ProsConsItem>>
    val allDecisions = mainRepo.getAllDecisions()

    fun insertDecision(decision: Decision) = viewModelScope.launch {
        mainRepo.insertDecision(decision)
    }

    fun deleteDecision(decision: Decision) = viewModelScope.launch {
        mainRepo.deleteDecision(decision)
    }

    fun updateDecision(decision: Decision) = viewModelScope.launch {
        mainRepo.updateDecision(decision)
    }

    fun insertProsConsItem(prosConsItem: ProsConsItem) = viewModelScope.launch {
        mainRepo.insertProsConsItem(prosConsItem)
    }

    fun deleteProsConsItem(prosConsItem: ProsConsItem) = viewModelScope.launch {
        mainRepo.deleteProsConsItem(prosConsItem)
    }

    fun deleteAllProsConsByID(parentID: Int) = viewModelScope.launch {
        mainRepo.deleteAllProsConsByID(parentID)
    }

    fun setParentId(parentId: Int) {
        allProsCons = mainRepo.getAllProsConsItems(parentId)
    }
}